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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.logger.AbstractDataLogger;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.DataLoggerObserver;
import net.zeddev.zedlog.logger.LogEntry;

/**
 * A collection of multiple <code>DataLogger</code>'s.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class CompositeDataLogger extends AbstractDataLogger implements DataLoggerObserver {

	private final Logger logger = Logger.getLogger(this);

	// each log entry made by the children loggers
	private List<LogEntry> logEntries = new ArrayList<>();

	private List<DataLogger> loggers = new ArrayList<>();

	private File logDirectory = null;

	// the log writers for each logger
	private Map<DataLogger, DataLoggerWriter> logWriters = new HashMap<>();

	/**
	 * Creates a new <code>CompositeDataLogger</code>.
	 *
	 */
	public CompositeDataLogger() {
	}

	// adds a logger to logWriters
	private void addLogWriter(final DataLogger logger) throws IOException {

		assert(logger != null);
		assert(logDirectory != null);

		// dont bother if no log dir is set
		if (logDirectory == null)
			return;

		String logFile = String.format("%s.log", logger.type());

		final FileWriter fileWriter = new FileWriter(
			new File(logDirectory, logFile)
		);

		DataLoggerWriter dataWriter = new DataLoggerWriter(fileWriter);
		logger.addObserver(dataWriter);

		logWriters.put(logger, dataWriter);

	}

	// removes the log writer for the given logger
	private void removeLogWriter(final DataLogger logger) throws IOException {

		assert(logger != null);

		DataLoggerWriter writer = logWriters.get(logger);

		// remove the log writer
		if (writer != null) {

			writer.close();
			logger.removeObserver(writer);

			logWriters.remove(logger);

		}

	}

	// removes all writers from logWriters
	private void removeAllLogWriters() throws IOException {

		// close all writer streams
		for (DataLoggerWriter writer : logWriters.values())
			writer.close();

		logWriters.clear();

	}

	/**
	 * Sets the directory where each loggers log will be written.
	 *
	 * @param dir The log directory (must be a directory).  Can alternatively be
	 * <code>null</code> to remove the existing log directory setting.
	 * @throws IOException If error occurs while creating log files for
	 *         existing loggers.
	 * @throws IOException If error occurs while removing existing log directory.
	 */
	public void setLogDirectory(final File dir) throws IOException {

		assert(dir != null && dir.isDirectory());

		this.logDirectory = dir;

		// clear the existing log writers
		removeAllLogWriters();

		if (dir != null) {

			// add the logger writers for each existing logger
			for (DataLogger logger : loggers)
				addLogWriter(logger);

			// add the composite logger itself
			addLogWriter(this);

		}

	}

	/**
	 * Returns the directory in which the log files reside.
	 *
	 * @return The log directory (<code>null</code> if not set).
	 */
	public File getLogDirectory() {
		return logDirectory;
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
		return loggers.get(index);
	}

	/**
	 * Adds a new <code>DataLogger</code>.
	 *
	 * @param logger The logger to add.
	 * @throws IOException If exception occurs while establishing log file.
	 */
	public void addLogger(final DataLogger logger) throws IOException {

		assert(logger != null);

		logger.addObserver(this);
		loggers.add(logger);

		addLogWriter(logger);

	}

	/**
	 * Removes the given <code>DataLogger</code>.
	 *
	 * @param logger The logger to be removed.
	 * @throws IOException If error occured when closing log file for the logger.
	 */
	public void removeLogger(final DataLogger logger) throws IOException {

		assert(logger != null);

		logger.removeObserver(this);

		removeLogWriter(logger);

		loggers.remove(logger);

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

	@Override
	public void notifyLog(final DataLogger logger, final LogEntry logEntry) {

		assert(logger != null);
		assert(logEntry != null);

		if (isRecording()) {

			logEntries.add(logEntry); // TODO optimise using fast(er) list implementation

			notifyDataLoggerObservers(logger, logEntry);

		}

	}

	@Override
	public final void setRecording(boolean recording) {

		// set for all children
		for (DataLogger logger : loggers)
			logger.setRecording(recording);

		super.setRecording(recording);

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
