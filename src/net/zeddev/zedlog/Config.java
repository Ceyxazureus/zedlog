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

package net.zeddev.zedlog;

import java.net.URL;

/**
 * Configuration for ZedLog.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class Config {

	/** Singleton instance. */
	public static final Config INSTANCE = new Config();

	/** Major version number. */
	public final int VERSION_MAJOR = 0;

	/** Minor version number.  */
	public final int VERSION_MINOR = 1;

	/** The phase of version development (alpha, beta, rc etc.). */
	public final String VERSION_PHASE = "alpha";

	/** The version as a string. */
	public final String VERSION = "v" + VERSION_MAJOR + "." + VERSION_MINOR + VERSION_PHASE;

	/** Official program name. */
	public final String NAME = "ZedLog";

	/** The full program name including version. */
	public final String FULL_NAME = NAME + " " + VERSION;

	/** A short description about the program. */
	public final String DESCRIPTION = "A small yet powerful input logger tool.";

	/**
	 * The help documentation resource Url.
	 * i.e. Needs to be loaded using <code>Class.getResource()</code>.
	 *
	 */
	public static final String HELPDOC = "/net/zeddev/zedlog/doc/helpdoc.html";

	private Config() {
	}

}