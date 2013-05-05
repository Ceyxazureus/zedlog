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

package net.zeddev.zedlog.gui.dialog;

import java.awt.Frame;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.zeddev.litelogger.Logger;

/**
 * Provides some common, simple dialogs.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class SimpleDialog {

	private static final Logger logger = Logger.getLogger(SimpleDialog.class);

	private SimpleDialog() {
	}

	/**
	 * Shows a info message box.
	 *
	 * @param parent The parent of the message box.
	 * @param title The title of the message box.
	 * @param msg The message to be displayed.
	 */
	public static void message(Frame parent, String title, String msg) {
		JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Shows a warning message box.
	 *
	 * @param parent The parent of the message box.
	 * @param msg The message to be displayed.
	 */
	public static void warning(Frame parent, String msg) {
		JOptionPane.showMessageDialog(parent, msg, "WARNING", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Shows a error message box.
	 *
	 * @param parent The parent of the message box.
	 * @param msg The message to be displayed.
	 */
	public static void error(Frame parent, String msg) {
		JOptionPane.showMessageDialog(parent, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Displays a confirmation box with <code>Okay</code> and <code>Cancel</code>
	 * options.
	 *
	 * @param parent The parent of the message box.
	 * @param title The title of the message box.
	 * @param msg The question to be confirmed to be displayed.
	 * @return Whether the user pressed <code>Okay</code> or not.
	 */
	public static boolean okcancel(Frame parent, String title, String msg) {

		return JOptionPane.showConfirmDialog(
			parent, msg, title,
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.PLAIN_MESSAGE
		) == JOptionPane.OK_OPTION;

	}

	/**
	 * Displays a confirmation box with <code>Yes</code> and <code>No</code>
	 * options.
	 *
	 * @param parent The parent of the message box.
	 * @param title The title of the message box.
	 * @param msg The question to be confirmed to be displayed.
	 * @return Whether the user pressed <code>Yes</code> or not.
	 */
	public static boolean yesno(Frame parent, String title, String msg) {

		return JOptionPane.showConfirmDialog(
			parent, msg, title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.PLAIN_MESSAGE
		) == JOptionPane.YES_OPTION;

	}

	/**
	 * Opens a save-file chooser dialog.
	 *
	 * @param parent The parent frame (can be <code>null</code>).
	 * @return The selected file (or <code>null</code> if error).
	 */
	public static File saveFile(final Frame parent) {

		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showSaveDialog(parent);

		if (ret == JFileChooser.APPROVE_OPTION) {

			return fileChooser.getSelectedFile();

		} else if (ret == JFileChooser.ERROR_OPTION) {
			logger.warning("Error occured with file chooser when saving.");
			return null;
		} else {
			logger.info("User cancelled saving file.");
			return null;
		}

	}

	/**
	 * Opens a directory chooser dialog.
	 *
	 * @param parent The parent frame (can be <code>null</code>).
	 * @return The selected directory (or <code>null</code> if error).
	 */
	public static File selectDir(final Frame parent) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int ret = fileChooser.showDialog(parent, "Select");
		if (ret == JFileChooser.APPROVE_OPTION) {

			return fileChooser.getSelectedFile();

		} else if (ret == JFileChooser.ERROR_OPTION) {
			logger.warning("Error occured with file chooser when selecting directory.");
			return null;
		} else {
			logger.info("User cancelled selecting directory.");
			return null;
		}

	}

}
