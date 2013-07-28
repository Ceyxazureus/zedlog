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
import static net.zeddev.zedlog.util.Assertions.*;

import org.w3c.dom.*;

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
		
		requireNotNull(parent);
		requireNotNull(message);
		requireNotEquals(message, "");
		requireNotNull(event);
		
		this.parent = parent;
		this.message = message;
		this.event = event;
		
	}

	/** Creates a new, empty {@code LogEntry}. */
	public LogEntry() {
	}

	public DataLogger getParent() {
		
		ensureNotNull(parent);
		
		return parent;
		
	}

	public void setParent(DataLogger parent) {
		
		requireNotNull(parent);
		
		this.parent = parent;
		
	}

	public String getMessage() {
		
		ensureNotNull(message);
		ensureNotEquals(message, "");
		
		return message;
		
	}

	public void setMessage(String message) {
		
		requireNotNull(message);
		requireNotEquals(message, "");
		
		this.message = message;
		
	}

	public LogEvent getEvent() {
		
		ensureNotNull(event);
		
		return event;
		
	}

	public void setEvent(LogEvent event) {
		
		requireNotNull(event);
		
		this.event = event;
		
	}

	public long getTimestamp() {
		
		ensure(timestamp >= 0);
		
		return timestamp;
		
	}

	public void setTimestamp(long timestamp) {
		
		require(timestamp >= 0);
		
		this.timestamp = timestamp;
		
	}
	
	public void toXML(Element parent) throws Exception {
		
		requireNotNull(parent);

		Document doc = parent.getOwnerDocument();
		Element entry = doc.createElement("entry");
		
		entry.setAttribute("msg", getMessage().replace("\n", ""));
		entry.setAttribute("timestamp", Long.toString(getTimestamp()));
		entry.setAttribute("type", getEvent().type());
		
		// add the logged event
		getEvent().toXML(entry);
		
		parent.appendChild(entry);
		
	}

	public void fromXML(Element parent) throws Exception {
		
		requireNotNull(parent);

		// TODO implement me
		
	}
	
	/**
	 * Writes the {@code LogEntry} to a {@code Tuple}.
	 *
	 * @return A {@code Tuple} containing the data in the {@code LogEntry}.
	 */
	@Deprecated
	public void write(final Writer output) throws Exception {

		requireNotNull(output);

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
	@Deprecated
	public void read(final Scanner scanner)
			throws ClassNotFoundException, InstantiationException,
				   IllegalAccessException, Exception {

		requireNotNull(scanner);
		
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
