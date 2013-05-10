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

import java.io.Writer;
import java.io.Reader;
import java.util.Scanner;

/**
 * A single log record by a <code>DataLogger</code>.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class LogEntry {

	private final DataLogger parent;
	private final String message;
	private final LogEvent event;
	private final long timestamp = System.currentTimeMillis();

	/**
	 * Creates a new <code>LogEntry</code> with the given detail message.
	 *
	 * @param message The logged message.
	 */
	public LogEntry(final DataLogger parent, final String message, final LogEvent event) {
		this.parent = parent;
		this.message = message;
		this.event = event;
	}

	public DataLogger getParent() {
		return parent;
	}

	public final String getMessage() {
		return message;
	}

	public LogEvent getEvent() {
		return event;
	}

	public final long getTimestamp() {
		return timestamp;
	}

	/**
	 * Writes the <code>LogEntry</code> to a <code>Tuple</code>.
	 *
	 * @return A <code>Tuple</code> containing the data in the <code>LogEntry</code>.
	 */
	public void write(final Writer output) {

		assert(output != null);

		

	}

	/**
	 * Reads the <code>LogEntry</code> from the data <code>Tuple</code>.
	 *
	 * @param tuple The <code>Tuple</code> which contains the <code>LogEntry</code>
	 */
	public void read(final Reader input) {

		assert(input != null);

		Scanner scanner = new Scanner(input);

		scanner.next(message);

	}

	@Override
	public String toString() {
		return getMessage();
	}

}
