/* Copyright (C) 2013  Zachary Scott <zscott.dev@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.zeddev.zedlog.logger.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Pattern;
import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.logger.AbstractDataLogger;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.DataLoggerObserver;
import net.zeddev.zedlog.logger.LogEntry;
import net.zeddev.zedlog.logger.LogEvent;

/**
 * A collection of multiple <code>DataLogger</code>'s.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class CompositeDataLogger extends AbstractDataLogger implements DataLoggerObserver {

	private final Logger logger = Logger.getLogger(this);

	// each log entry made by the children loggers
	private final List<LogEntry> logEntries = new ArrayList<>();

	private final List<DataLogger> loggers = new ArrayList<>();

	// the output stream in which to write log entries
	private Writer logstream = null;

	/**
	 * Creates a new <code>CompositeDataLogger</code>.
	 *
	 */
	public CompositeDataLogger() {
	}

	@Override
	public void shutdown() {

		try {
			closeLogStream();
		} catch (IOException ex) {
			logger.warning("Failed to close log file/stream properly!", ex);
		}

		logger.debug("CompositeLogger shutdown.");

	}

	@Override
	public String type() {
		return "CompositeLogger";
	}

	/**
	 * Returns the <code>DataLogger</code> at the given index in the
	 * <code>CompositeDataLogger</code>.
	 *
	 * @param index The index of the logger to get.
	 * @return The <code>DataLogger</code> at the given index.
	 */
	public DataLogger getLogger(int index) {

		assert(index >= 0 && index < loggers.size());

		synchronized (loggers) {
			return loggers.get(index);
		}

	}

	/**
	 * Adds a new <code>DataLogger</code>.
	 *
	 * @param logger The logger to add.
	 * @throws IOException If exception occurs while establishing log file.
	 */
	public void addLogger(final DataLogger logger) throws IOException {

		assert(logger != null);

		synchronized (loggers) {

			logger.setRecording(isRecording());

			logger.addObserver(this);
			loggers.add(logger);

		}

	}

	/**
	 * Removes the given <code>DataLogger</code>.
	 *
	 * @param logger The logger to be removed.
	 * @throws IOException If error occured when closing log file for the logger.
	 */
	public void removeLogger(final DataLogger logger) throws IOException {

		assert(logger != null);

		synchronized (loggers) {

			logger.removeObserver(this);
			loggers.remove(logger);

		}

	}

	/**
	 * Removes the <code>DataLogger</code> at the given index.
	 *
	 * @param index The index of the <code>DataLogger</code> to be removed.
	 */
	public void removeLogger(int index) {
		assert(index >= 0 && index < loggers.size());
		loggers.remove(index);
	}

	/**
	 * Clears all log entries and the log files.
	 *
	 * @throws IOException If an error occurs when the log files are closed.
	 */
	public void clearAll() throws IOException {

		logEntries.clear();

	}

	/**
	 * Returns the children <code>DataLogger</code>s.
	 *
	 * @return The children <code>DataLogger</code>s.
	 */
	public List<DataLogger> getLoggers() {
		return new ArrayList<>(loggers);
	}

	/**
	 * Returns a list of all entries made by children loggers.
	 *
	 * @return A list of all entries made by children loggers.
	 */
	public List<LogEntry> logEntries() {
		return new ArrayList<>(logEntries);
	}

	/**
	 * Returns the output stream in which log entries are written.
	 *
	 * @return The log output stream (may be <code>null</code>).
	 */
	public Writer getLogStream() {
		return logstream;
	}

	/**
	 * Sets the output stream in which log entries are written.
	 *
	 * @param logstream The new output stream (<code>null</code> indicates none).
	 */
	public void setLogStream(Writer logstream) {
		this.logstream = logstream;
	}

	/**
	 * Closes the output log stream.
	 *
	 * @throws IOException
	 */
	public void closeLogStream() throws IOException {

		if (logstream != null) {
			logstream.flush();
			logstream.close();
		}

	}

	/**
	 * Sets the log file to which log entries are stored.
	 *
	 * @param file The log file (must be a valid filename and
	 * cannot be <code>null</code>).
	 */
	public void setLogFile(File file) throws IOException {
		assert(file != null);
		setLogStream(new FileWriter(file));
	}

	/**
	 * Opens the given log file and reads the log entries.
	 * Implies clearing of the currently held log entries.
	 *
	 * @param file The file in which to read (file must exist and
	 * cannot be <code>null</code>).
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public void openLogFile(File file)
			throws FileNotFoundException, IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, Exception {

		assert(file != null);

		// first thing, close the old log stream
		closeLogStream();

		try (BufferedReader input = new BufferedReader(new FileReader(file))) {

			// read each log entry one-by-one
			String line = input.readLine();
			while (line != null) {

				Scanner scanner = new Scanner(line);
				scanner.useDelimiter(Pattern.quote("|"));

				// read the single log entry
				LogEntry logEntry = new LogEntry();
				logEntry.read(scanner);

				notifyLog(null, logEntry);

				line = input.readLine();

			}

		}

	}

	// writes the log entry to the log stream
	private void writeLogEntry(LogEntry logEntry) {

		Writer logstream = getLogStream();

		if (logstream != null) {

			try {

				// write the log entry
				logEntry.write(getLogStream());
				logstream.write("\n");

				logstream.flush();

			} catch (Exception ex) {
				logger.error("Failed to write log entry!", ex);
			}

		}

	}

	@Override
	public void notifyLog(final DataLogger logger, final LogEntry logEntry) {

		assert(logEntry != null);

		if (isRecording()) {

			logEntries.add(logEntry); // TODO optimise using fast(er) list implementation

			writeLogEntry(logEntry);

			notifyDataLoggerObservers(logger, logEntry);

		}

	}

	@Override
	public final void setRecording(boolean recording) {

		synchronized (loggers) {

			// set for all children
			for (DataLogger logger : loggers)
				logger.setRecording(recording);

			super.setRecording(recording);

		}

	}

	@Override
	public String toString() {

		final StringBuilder log = new StringBuilder();
		DataLogger lastLogger = null;

		for (LogEntry logEntry : logEntries) {

			// add newline to separate different logger messages
			if (lastLogger == null) {
				lastLogger = logEntry.getParent();
			} else if (logger != lastLogger) {

				// dont append if already a newline
				if (log.charAt(log.length()-1) != '\n')
					log.append("\n");

				lastLogger = logEntry.getParent();

			}

			log.append(logEntry);

		}

		return log.toString();

	}

}
