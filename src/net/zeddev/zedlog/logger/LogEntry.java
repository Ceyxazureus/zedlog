package net.zeddev.zedlog.logger;
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

import java.io.Writer;
import java.util.Scanner;

import net.zeddev.zedlog.logger.impl.LogEvents;

/**
 * A single log record by a {@code DataLogger}.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class LogEntry {

	private DataLogger parent = null;
	private String message = null;
	private LogEvent event = null;
	private long timestamp = System.currentTimeMillis();

	/**
	 * Creates a new {@code LogEntry} with the given details.
	 *
	 * @param parent the parent logger, which created the {@code LogEntry}.
	 * @param message The logged message.
	 */
	public LogEntry(final DataLogger parent, final String message, final LogEvent event) {
		this.parent = parent;
		this.message = message;
		this.event = event;
	}

	/** Creates a new, empty {@code LogEntry}. */
	public LogEntry() {
	}

	public DataLogger getParent() {
		return parent;
	}

	public void setParent(DataLogger parent) {
		this.parent = parent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LogEvent getEvent() {
		return event;
	}

	public void setEvent(LogEvent event) {
		this.event = event;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Writes the {@code LogEntry} to a {@code Tuple}.
	 *
	 * @return A {@code Tuple} containing the data in the {@code LogEntry}.
	 */
	public void write(final Writer output) throws Exception {

		assert(output != null);

		output.write(getMessage().replace("\n", ""));
		output.write("|");
		output.write(Long.toString(getTimestamp()));
		output.write("|");
		output.write(getEvent().type());
		output.write("|");
		getEvent().write(output);

	}

	/**
	 * Reads the {@code LogEntry} from the given input.
	 *
	 * @param scanner The input scanner in which to be parsed.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public void read(final Scanner scanner)
			throws ClassNotFoundException, InstantiationException,
			       IllegalAccessException, Exception {

		setMessage(String.format("%s\n", scanner.next()));
		setTimestamp(scanner.nextLong());

		// read the log event
		String eventType = scanner.next();
		LogEvent event = LogEvents.newLogEvent(eventType);
		event.read(scanner);

		setEvent(event);

		// NOTE The parent is not stored/read.

	}

	@Override
	public String toString() {
		return getMessage();
	}

}
