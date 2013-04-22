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

import java.util.Arrays;
import java.util.List;
import net.zeddev.zedlog.logger.DataLogger;

/**
 * A simple factory which produces concrete <code>DataLogger</code>s.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class DataLoggers {

	private DataLoggers() {
	}

	/**
	 * Produces new <code>DataLogger<code> instances based on the given type.
	 * See <code>typeList()</code> for full list.
	 *
	 * @param type The type of the new <code>DataLogger</code>.
	 * @return The new <code>DataLogger</code> instance.
	 */
	public static DataLogger newDataLogger(String type) {

		if (type.equalsIgnoreCase("keyboard")) {
			return new KeyLogger();
		} else if (type.equalsIgnoreCase("mouse click")) {
			return new MouseClickLogger();
		} else if (type.equalsIgnoreCase("mouse movement")) {
			return new MouseMovementLogger();
		} else if (type.equalsIgnoreCase("mouse dragged")) {
			return new MouseDraggedLogger();
		} else if (type.equalsIgnoreCase("mouse wheel")) {
			return new MouseWheelLogger();
		} else {
			return null;
		}

	}

	/**
	 * Returns all available <code>DataLogger</code> types that can be used with
	 * <code>newDataLogger()</code>.
	 * The currently supported types are;
	 * <ul>
	 *	<li>"keyboard" - <code>KeyLogger</code>.</li>
	 *  <li>"mouse click" - <code>MouseClickLogger</code>.</li>
	 *  <li>"mouse movement" - <code>MouseMovementLogger</code>.</li>
	 *  <li>"mouse dragged" - <code>MouseDraggedLogger</code>.</li>
	 *  <li>"mouse wheel" - <code>MouseWheelLogger</code>.</li>
	 * </ul>
	 *
	 * @return The list of available <code>DataLogger</code> types.
	 */
	public static List<String> typeList() {

		final String[] types = {
			"keyboard",
			"mouse click",
			"mouse movement",
			"mouse dragged",
			"mouse wheel"
		};

		return Arrays.asList(types);

	}

}
