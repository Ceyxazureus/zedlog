package net.zeddev.zedlog.installer;
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

/**
 * A basic logger for exclusive use with the installer.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class Logger {

	public static void log(String label, String fmt, Object... args) {
		
		assert(label != null);
		assert(fmt != null);
		
		String msg = String.format(fmt, args);
		System.err.println(String.format("%s: %s", label, msg));
		
	}
	
	public static void info(String fmt, Object... args) {
		log("info", fmt, args);
	}
	
	public static void warn(String fmt, Object... args) {
		log("warning", fmt, args);
	}
	
	public static void error(String fmt, Object... args) {
		log("error", fmt, args);
	}
	
}
