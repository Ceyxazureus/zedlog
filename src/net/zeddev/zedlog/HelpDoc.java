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

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import net.zeddev.litelogger.Logger;
import sun.misc.IOUtils;

/**
 * A simple wrapper for the ZedLog help documentation.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class HelpDoc {

	private final Logger logger = Logger.getLogger(this);

	/** Singleton instance. */
	public static final HelpDoc INSTANCE = new HelpDoc();

	private File TMP_HELP_FILE;

	private HelpDoc() {

		try {

			TMP_HELP_FILE = File.createTempFile("helpdoc.", ".html");

			// write the resource to temp file
			FileOutputStream out = new FileOutputStream(TMP_HELP_FILE);
			out.write(
				IOUtils.readFully(
					getClass().getResourceAsStream(Config.HELPDOC), -1, false
				)
			);

			out.close();

		} catch (IOException ex) {
			logger.error(String.format("Cannot create temp file %s", TMP_HELP_FILE.getPath()), ex);
		}

	}

	/**
	 * Returns the temporary file containing the ZedLog documentation.
	 *
	 * @return The ZedLog documentation file.
	 */
	public File getTempFile() {
		return TMP_HELP_FILE;
	}

	/**
	 * Displays the help documentation in a web-browser.
	 *
	 * @throws IOException If the documentation cannot be displayed in the web browser.
	 */
	public void showInBrowser() throws IOException {
		logger.info("Opening help documentation in web browser.");
		Desktop.getDesktop().browse(getTempFile().toURI());
	}

}
