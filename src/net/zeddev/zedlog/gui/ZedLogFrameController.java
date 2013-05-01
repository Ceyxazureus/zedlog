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
import net.zeddev.zedlog.gui.dialog.SimpleDialog;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.impl.CompositeDataLogger;
import net.zeddev.zedlog.logger.impl.DataLoggerWriter;

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

	// the program log output file
	private WriterLogHandler msgLogFile = null;

	// the data logger output log file
	private DataLoggerWriter logFile = null;

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

		frame.getBtnHide().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnHideActionPerformed(event);
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

		frame.getBtnNext().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnNextActionPerformed(event);
            }
        });

		frame.getBtnPrev().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnPrevActionPerformed(event);
            }
        });

		frame.getMItemHide().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemHideActionPerformed(evt);
            }
        });

		frame.getMItemAdd().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemAddActionPerformed(evt);
            }
        });

		frame.getMItemRemove().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemRemoveActionPerformed(evt);
            }
        });

		frame.getMItemSave().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemSaveActionPerformed(evt);
            }
        });

		frame.getMItemLogFile().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemLogFileActionPerformed(evt);
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

	private void addLoggerTab(final DataLogger logger) {

		assert(logger != null);

		// create logger view panel (and view controller)
		LoggerPanel loggerPanel = new LoggerPanel();
		new LoggerPanelController(loggerPanel, logger);

		// add a logger panel tab for the new logger
		frame.getTabs().add(logger.type(), loggerPanel);

	}

	private void userQuit() {

		if (logFile == null) {

			boolean quit = SimpleDialog.yesno(
				frame, "Really Quit?",
				"There is unsaved log data which could be lost.\n"
				+ "Are you sure you want to quit?"
			);

			if (!quit) {
				logger.info("Quit cancelled.");
				return;
			}

		}

		logger.info("User quit.");

		frame.setVisible(false);
		System.exit(0);

	}

	private void formWindowClosing(WindowEvent evt) {
		userQuit();
	}

	// show or hide the ZedLog frame
	private void showHide() {
		boolean visible = frame.isVisible();
		frame.setVisible(!visible);
	}

	private void addDataLogger() {

		NewLoggerDialog dialog = new NewLoggerDialog(frame, true);
		DataLogger dataLogger = dialog.getLoggerFromUser();

		// dont add if logger not specified
		if (dataLogger == null)
			return;

		loggers.addLogger(dataLogger);

		addLoggerTab(dataLogger);

		logger.info(String.format("Added logger %s", dataLogger.type()));

	}

	private void removeDataLogger() {

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

	private void btnHideActionPerformed(final ActionEvent event) {
		showHide();
    }

	private void btnAddActionPerformed(final ActionEvent event) {
		addDataLogger();
    }

	private void btnRemoveActionPerformed(final ActionEvent event) {
		removeDataLogger();
    }

	private void btnNextActionPerformed(final ActionEvent event) {

		int current = frame.getTabs().getSelectedIndex();

		if (current < frame.getTabs().getTabCount()-1)
			frame.getTabs().setSelectedIndex(current + 1);

    }

	private void btnPrevActionPerformed(final ActionEvent event) {

		int current = frame.getTabs().getSelectedIndex();

		if (current > 0)
			frame.getTabs().setSelectedIndex(current - 1);

    }

	private void saveToFile(File saveFile) throws IOException {

		FileWriter output = new FileWriter(saveFile);
		output.write(loggers.toString());
		output.close();

	}

	private void mitemHideActionPerformed(ActionEvent evt) {
		showHide();
	}

	private void mitemAddActionPerformed(ActionEvent evt) {
		addDataLogger();
	}

	private void mitemRemoveActionPerformed(ActionEvent evt) {
		removeDataLogger();
	}

	private File openSaveDialog() {

		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showSaveDialog(frame);

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

	private void mitemSaveActionPerformed(ActionEvent evt) {

		File saveFile = openSaveDialog();
		if (saveFile != null) {

			logger.info(String.format("Saving to %s.", saveFile.getPath()));

			// save the log output to file
			try {
				saveToFile(saveFile);
			} catch (IOException ex) {
				logger.error(String.format("Error saving log to file '%s'.", saveFile.getPath()), ex);
				return;
			}

			logger.info(String.format("Log saved successfully to '%s'.", saveFile.getPath()));

		}

	}

	private void mitemLogFileActionPerformed(ActionEvent evt) {

		File saveFile = openSaveDialog();
		if (saveFile != null) {

			logger.info(String.format("Setting log file to '%s'.", saveFile.getPath()));

			// set the log file output
			try {

				// close the old log file
				if (logFile != null) {
					loggers.removeObserver(logFile);
					logFile.close();
				}

				// add the log handler
				logFile = new DataLoggerWriter(new FileWriter(saveFile));
				loggers.addObserver(logFile);

			} catch (IOException ex) {
				logger.error(String.format("Error setting log file to '%s'.", saveFile.getPath()), ex);
				return;
			}

			logger.info(String.format("Log file set to '%s'.", saveFile.getPath()));

		}

	}

	private void mitemLogWindowActionPerformed(ActionEvent evt) {
		logWindow.setVisible(true);
	}

	private void mitemMsgLogFileActionPerformed(ActionEvent evt) {

		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showSaveDialog(frame);

		if (ret == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();

			logger.info(String.format("Setting program log file to '%s'.", file.getPath()));

			// set the log file output
			try {

				// close the old log file
				if (msgLogFile != null) {
					Logger.removeHandler(msgLogFile);
					msgLogFile.close();
				}

				// add the log handler
				msgLogFile = new WriterLogHandler(new FileWriter(file));
				Logger.addHandler(msgLogFile);

			} catch (IOException ex) {
				logger.error(String.format("Error setting program log file to '%s'.", file.getPath()), ex);
				return;
			}

			logger.info(String.format("Program log file set to '%s'.", file.getPath()));

		} else if (ret == JFileChooser.ERROR_OPTION) {
			logger.warning("Error occured with file chooser when saving.");
		} else {
			logger.info("User cancelled setting program log file.");
		}

	}

	private void mitemQuitActionPerformed(ActionEvent evt) {
		userQuit();
	}

	private void mitemAboutActionPerformed(java.awt.event.ActionEvent evt) {

		logger.info("Displaying 'About' infobox.");

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

}
