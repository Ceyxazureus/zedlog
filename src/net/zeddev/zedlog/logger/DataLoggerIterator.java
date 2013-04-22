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

import java.util.Iterator;

/**
 * An <code>Iterator</code> for <code>DataLog</code>'s.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class DataLoggerIterator implements Iterator<LogEntry> {

	// the backing iterator for the given data log
	private Iterator<LogEntry> iterator;

	/**
	 * Creates a new <code>DataLogIterator</code> for the given <code>DataLog</code>.
	 *
	 * @param log The <code>DataLog</code> to be iterated (must not be
	 * <code>null</code>.
	 */
	public DataLoggerIterator(final DataLogger logger) {
		iterator = logger.logEntries().iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public LogEntry next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		iterator.remove();
	}

}
