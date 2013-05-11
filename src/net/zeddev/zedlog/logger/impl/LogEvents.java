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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.logger.LogEvent;
import net.zeddev.zedlog.logger.impl.event.*;

/**
 * A simple factory to produce <code>LogEvent</code>.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class LogEvents {

	private static final Logger logger = Logger.getLogger(LogEvents.class);

	// the available log event types
	private static final List<String> TYPES = new ArrayList<>();

	// the classes for the available log events types
	private static final Map<String, Class> CLASSES = new HashMap<>();

	static {

		// the list of available log event instances
		final LogEvent[] instances = {
			new KeyEvent(),
			new MouseClickedEvent(),
			new MouseDraggedEvent(),
			new MouseMovedEvent(),
			new MousePressedEvent(),
			new MouseReleasedEvent(),
			new MouseWheelMovedEvent()
		};

		// set all available loggers
		for (LogEvent eventInstance : instances) {
			CLASSES.put(eventInstance.type(), eventInstance.getClass());
			TYPES.add(eventInstance.type());
		}

	}

	private LogEvents() {
	}

	/**
	 * Creates a new <code>LogEvent</code> of the given type.
	 * for a full list of types see <code>typeList()</code>.
	 *
	 * @param type The type of the <code>LogEvent</code> (must be one of those given by
	 * <code>typeList()</code>.
	 * @return The new <code>LogEvent</code> instance.  Or <code>null</code> if
	 * the given type is not known.
	 */
	public static LogEvent newLogEvent(String type) {

		assert(type != null);

		if (CLASSES.containsKey(type)) {

			try {
				return (LogEvent) CLASSES.get(type).newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				logger.warning("Failed to instantiate LogEntry instance of type %s.", ex, type);
				return null;
			}

		} else {
			return null;
		}

	}

	/**
	 * Returns the list of available <code>LogEvent</code> types producable by
	 * <code>newLogEvent()</code>.
	 * The currently supported types are;
	 * <ul>
	 *	<li>"KeyEvent" - <code>KeyEvent</code>.</li>
	 *	<li>"MouseClicked" - <code>MouseClickedEvent</code>.</li>
	 *  <li>"MouseDragged" - <code>MouseDraggedEvent</code>.</li>
	 *  <li>"MouseMoved" - <code>MouseMovedEvent</code>.</li>
	 *  <li>"MousePressed" - <code>MousePressedEvent</code>.</li>
	 *  <li>"MouseReleased" - <code>MouseReleasedEvent</code>.</li>
	 *  <li>"MouseWheelMoved" - <code>MouseWheelMovedEvent</code>.</li>
	 * </ul>
	 *
	 * @return The list of available <code>LogEvent</code> types.
	 */
	public static List<String> typeList() {
		return new ArrayList<>(TYPES);
	}

}
