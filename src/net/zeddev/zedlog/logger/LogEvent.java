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

/**
 * A single event which is logged by a {@code DataLogger}.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public abstract class LogEvent {

	/**
	 * Writes the {@code LogEvent} to the given output stream.
	 * The format should be as follows:
	 * <ul>
	 * <li>Fields should be separated by a single vertical bar ({@code |}</li>
	 * <li>Should contain only readable characters.</li>
	 * <li>Fields should not contain the following characters;
	 *	<ul>
	 *		<li>Vertical bars {@code |}</li>
	 *		<li>Newlines (both {@code \n} and {@code \r}</li>
	 *	</ul>
	 * </li>
	 * </ul>
	 *
	 * @param output The output stream to write to.
	 * @throws Exception
	 */
	@Deprecated
	public abstract void write(final Writer output) throws Exception;

	/**
	 * Parses the {@code LogEntry} from the given input scanner.
	 * The delimiter is preset and should not be modified.
	 *
	 * @param scanner The scanner to be read from.
	 * @throws Exception
	 */
	@Deprecated
	public abstract void read(final Scanner scanner) throws Exception;

	/**
	 * Returns a human-readable name for the event type.
	 * Must be unique among all events.
	 *
	 * @return The type of the event.
	 */
	public abstract String type();

}
