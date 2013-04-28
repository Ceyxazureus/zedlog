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

import java.util.ArrayList;
import java.util.List;
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

	private StringBuilder log = new StringBuilder();

	private List<DataLogger> loggers = new ArrayList<>();

	/**
	 * Creates a new <code>CompositeDataLogger</code>.
	 *
	 */
	public CompositeDataLogger() {
	}

	@Override
	public String type() {
		return "logger composite";
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
	 */
	public void addLogger(final DataLogger logger) {

		assert(logger != null);

		logger.addObserver(this);
		loggers.add(logger);

	}

	/**
	 * Removes the given <code>DataLogger</code>.
	 *
	 * @param logger The logger to be removed.
	 */
	public void removeLogger(final DataLogger logger) {

		assert(logger != null);

		logger.removeObserver(this);
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

	@Override
	public List<LogEntry> logEntries() {
		throw new UnsupportedOperationException("CompositeDataLogger.logEntries not supported.");
	}

	private DataLogger lastToNotify = null;

	@Override
	public void notifyLog(final DataLogger logger, final LogEntry logEntry) {


		// add newline to separate different logger messages
		if (lastToNotify == null) {
			lastToNotify = logger;
		} else if (logger != lastToNotify) {

			// dont append if already a newline
			if (log.charAt(log.length()-1) != '\n')
				log.append("\n");

			lastToNotify = logger;

		}

		log.append(logEntry);

		notifyDataLoggerObservers(logger, logEntry);

	}

	@Override
	public String toString() {
		return log.toString();
	}

}
