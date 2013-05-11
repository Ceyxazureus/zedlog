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
public enum HelpDoc {

	/** The singleton instance. */
	INSTANCE;

	public static HelpDoc getInstance() {
		return INSTANCE;
	}

	private final Logger logger = Logger.getLogger(this);

	// the main helpdoc html filename
	private final String HELPDOC = Config.HELPDOC;

	// all help documentation files
	private final String[] HELP_FILES = {
		HELPDOC,
		"About.png",
		"AddLogger.png",
		"CharTyped.png",
		"CharTypedTab.png",
		"ClearButton.png",
		"CompositeLogger.png",
		"Hide.png",
		"icon.png",
		"MouseClickTab.png",
		"OpenLogFile.png",
		"PauseButton.png",
		"RecordButton.png",
		"RemoveLogger.png",
		"ReplayTool.png",
		"Save.png",
		"SetLogFile.png",
		"StandardConfig.png",
		"StartScreen.png",
	};

	private File TMP_HELP_FILE = new File(tempFile(HELPDOC));

	// returns a new temporary filename for the given filename
	private String tempFile(final String filename) {

		StringBuilder tmpfile = new StringBuilder();
		
		tmpfile.append(System.getProperty("java.io.tmpdir"));
		tmpfile.append(File.separator);
		tmpfile.append(filename);
	
		return tmpfile.toString();

	}

	private HelpDoc() {

		// create each temp help file
		for (String helpFile : HELP_FILES) {

			String tmpfile = tempFile(helpFile);

			// attempt to copy the given resource file to the temporary file on disk
			try {

				String resource = String.format("%s/%s", Config.DOCDIR, helpFile);

				// write the resource to temp file
				FileOutputStream out = new FileOutputStream(tmpfile);
				out.write(
					IOUtils.readFully(
						getClass().getResourceAsStream(resource), -1, false
					)
				);

				out.flush();
				out.close();

			} catch (IOException ex) {
				logger.error(String.format("Cannot create temp file %s", tmpfile), ex);
			} catch (Exception ex) {
				logger.error(String.format("Failed to copy resource file %s", helpFile), ex);
			}

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
