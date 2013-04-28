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

package net.zeddev.zedlog.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import net.zeddev.litelogger.Logger;
import net.zeddev.litelogger.handlers.WindowLogHandler;
import net.zeddev.litelogger.handlers.WriterLogHandler;
import net.zeddev.zedlog.HelpDoc;
import net.zeddev.zedlog.gui.dialog.NewLoggerDialog;
import net.zeddev.zedlog.gui.dialog.AboutDialog;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.impl.CompositeDataLogger;

/**
 * Controller for the <code>ZedLogFrame</code>.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class ZedLogFrameController {

	private final Logger logger = Logger.getLogger(this);

	// the view
	private final ZedLogFrame frame;

	// the data
	private final CompositeDataLogger loggers;

	private final WindowLogHandler logWindow = new WindowLogHandler();

	private WriterLogHandler logFile = null;

	/**
	 * Creates a new <code>ZedLogFrameController</code> for the given <code>ZedLogFrame</code>.
	 *
	 * @param frame The <code>ZedLogFrame</code> to control.
	 */
	public ZedLogFrameController(final ZedLogFrame frame, final CompositeDataLogger loggers) {

		assert(frame != null);
		assert(loggers != null);

		this.frame = frame;
		this.loggers = loggers;

		Logger.addHandler(logWindow);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

		frame.getBtnAdd().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnAddActionPerformed(event);
            }
        });

		frame.getBtnRemove().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnRemoveActionPerformed(event);
            }
        });

		frame.getMItemSave().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemSaveActionPerformed(evt);
            }
        });

		frame.getMItemMsgLogFile().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemMsgLogFileActionPerformed(evt);
            }
        });

		frame.getMItemLogWindow().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemLogWindowActionPerformed(evt);
            }
        });

        frame.getMItemQuit().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemQuitActionPerformed(evt);
            }
        });

		frame.getMItemHelp().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                mitemHelpActionPerformed(event);
            }
        });

		frame.getMItemAbout().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                mitemAboutActionPerformed(event);
            }
        });

		addLoggerTab(loggers);

	}

	public ZedLogFrame getFrame() {
		return frame;
	}

	public CompositeDataLogger getLoggers() {
		return loggers;
	}

	private void addLoggerTab(final DataLogger logger) {

		assert(logger != null);

		// create logger view panel (and view controller)
		LoggerPanel loggerPanel = new LoggerPanel();
		LoggerPanelController loggerPanelController = new LoggerPanelController(loggerPanel, logger);

		// add a logger panel tab for the new logger
		frame.getTabs().add(logger.type(), loggerPanel);

	}

	private void closeWindow() {

		logger.info("User quit.");

		frame.setVisible(false);
		System.exit(0);

	}

	private void formWindowClosing(WindowEvent evt) {
		closeWindow();
	}

	private void btnAddActionPerformed(final ActionEvent event) {

		NewLoggerDialog dialog = new NewLoggerDialog(frame, true);
		DataLogger dataLogger = dialog.getLoggerFromUser();

		// dont add if logger not specified
		if (logger == null)
			return;

		getLoggers().addLogger(dataLogger);

		addLoggerTab(dataLogger);

		logger.info(String.format("Added logger %s", dataLogger.type()));

    }

	private void btnRemoveActionPerformed(final ActionEvent event) {

		JTabbedPane tabs = frame.getTabs();

		if (tabs.getSelectedIndex() > 0) {

			DataLogger dataLogger = loggers.getLogger(tabs.getSelectedIndex() - 1);

			// catch attempt to remove CompositeDataLogger
			if (dataLogger instanceof CompositeDataLogger) {
				logger.error("Cannot remove composite logger!");
			} else {

				loggers.removeLogger(dataLogger);
				tabs.remove(tabs.getSelectedIndex());

			}

			logger.info(String.format("Removed %s logger.", dataLogger.type()));

		}

    }

	private void saveToFile(File saveFile) throws IOException {

		FileWriter output = new FileWriter(saveFile);
		output.write(loggers.toString());
		output.close();

	}

	private void mitemSaveActionPerformed(ActionEvent evt) {

		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showSaveDialog(frame);

		if (ret == JFileChooser.APPROVE_OPTION) {

			File saveFile = fileChooser.getSelectedFile();

			logger.info(String.format("Saving to %s.", saveFile.getPath()));

			// save the log output to file
			try {
				saveToFile(saveFile);
			} catch (IOException ex) {
				logger.error(String.format("Error saving log to file '%s'.", saveFile.getPath()), ex);
				return;
			}

			logger.info(String.format("Log saved successfully to '%s'.", saveFile.getPath()));

		} else if (ret == JFileChooser.ERROR_OPTION) {
			logger.warning("Error occured with file chooser when saving.");
		} else {
			logger.info("User cancelled saving log to file.");
		}

	}

	private void mitemMsgLogFileActionPerformed(ActionEvent evt) {

		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showSaveDialog(frame);

		if (ret == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();

			logger.info(String.format("Saving to %s.", file.getPath()));

			// set the log file output
			try {

				// close the old log file
				if (logFile != null) {
					Logger.removeHandler(logFile);
					logFile.close();
				}

				// add the log handler
				logFile = new WriterLogHandler(new FileWriter(file));
				Logger.addHandler(logFile);

			} catch (IOException ex) {
				logger.error(String.format("Error setting log file '%s'.", file.getPath()), ex);
				return;
			}

			logger.info(String.format("Log file set to '%s'.", file.getPath()));

		} else if (ret == JFileChooser.ERROR_OPTION) {
			logger.warning("Error occured with file chooser when saving.");
		} else {
			logger.info("User cancelled setting log file.");
		}

	}

	private void mitemQuitActionPerformed(ActionEvent evt) {
		frame.setVisible(false);
		System.exit(0);
	}

	private void mitemAboutActionPerformed(java.awt.event.ActionEvent evt) {
        AboutDialog aboutBox = new AboutDialog(frame, true);
		aboutBox.setVisible(true);
    }

	private void mitemHelpActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			HelpDoc.INSTANCE.showInBrowser();
		} catch (IOException ex) {
			logger.error("Cannot open help documentation in the web browser!", ex);
		}

    }

	private void mitemLogWindowActionPerformed(ActionEvent evt) {
		logWindow.setVisible(true);
	}

}
