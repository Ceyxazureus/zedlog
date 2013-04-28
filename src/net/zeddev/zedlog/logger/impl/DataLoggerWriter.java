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

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.DataLoggerObserver;
import net.zeddev.zedlog.logger.LogEntry;

/**
 * Writes data logger entries to the given output <code>Writer</code>.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class DataLoggerWriter implements DataLoggerObserver, Closeable {

	private final Logger logger = Logger.getLogger(this);

	private final Writer output;

	public DataLoggerWriter(final Writer output) {
		this.output = output;
	}

	private DataLogger lastToNotify = null;

	@Override
	public void notifyLog(DataLogger dataLogger, LogEntry logEntry) {

		try {

			// add newline to separate different logger messages
			if (lastToNotify == null) {
				lastToNotify = dataLogger;
			} else if (dataLogger != lastToNotify) {
				output.write("\n");
				lastToNotify = dataLogger;
			}

			output.write(logEntry.toString());
			output.flush();

		} catch (IOException ex) {
			logger.error("Failed to write data logger output to file!", ex);
			logger.info("Ignoring exception in DataLoggerWriter.notifyLog().");
		}

	}

	@Override
	public void close() throws IOException {
		output.close();
	}

}
