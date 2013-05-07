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

package net.zeddev.zedlog.logger;

/**
 * A generic data log interface.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public interface DataLogger {

	/**
	 * Returns the type name of the <code>DataLogger</code>.
	 * Must not have any spaces, for example "KeyLogger" or "MouseLogger".
	 *
	 * @return The type name of the <code>DataLogger</code>.
	 */
	public String type();

	// Depreciated 2013.05.01
	// /**
	//  * Returns all log entries in this <code>DataLog</code>.
	//  *
	//  * @return All current entries in this log (never <code>null</code>).
	//  */
	// public List<LogEntry> logEntries();

	/**
	 * Shuts-down the <code>DataLog</code> (and any resources it may consume).
	 *
	 *
	 */
	public void shutdown();

	/**
	 * Adds a <code>DataLoggerObserver</code>.
	 *
	 * @param observer The observer to be added to the list.
	 */
	public void addObserver(final DataLoggerObserver observer);

	/**
	 * Removes a <code>DataLoggerObserver</code>.
	 *
	 * @param observer The observer to remove from the list.
	 */
	public void removeObserver(final DataLoggerObserver observer);

	/**
	 * Returns whether the log entries are being recorded.
	 *
	 * @return Whether the log entries are being recorded.
	 */
	public boolean isRecording();

	/**
	 * Sets whether to record the log entries (or not).
	 *
	 * @param record Whether to record the log entries (or not).
	 */
	public void setRecording(boolean record);

	@Override
	public String toString();

}
