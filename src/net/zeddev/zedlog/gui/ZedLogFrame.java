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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import net.zeddev.litelogger.LogLevel;
import net.zeddev.litelogger.Logger;
import net.zeddev.litelogger.handlers.MsgBoxLogHandler;
import net.zeddev.litelogger.handlers.WindowLogHandler;
import net.zeddev.litelogger.handlers.WriterLogHandler;
import net.zeddev.zedlog.Config;
import net.zeddev.zedlog.HelpDoc;
import net.zeddev.zedlog.gui.dialog.AboutDialog;
import net.zeddev.zedlog.gui.dialog.NewLoggerDialog;
import net.zeddev.zedlog.gui.dialog.ReplayToolDialog;
import net.zeddev.zedlog.gui.dialog.SimpleDialog;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.impl.CompositeDataLogger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

/**
 * The main GUI frame.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class ZedLogFrame extends javax.swing.JFrame implements NativeMouseListener {

	private final Logger logger = Logger.getLogger(this);
	private final WindowLogHandler logWindow = new WindowLogHandler();

	private CompositeDataLogger loggers;

	// the program log output file
	private WriterLogHandler msgLogFile = null;

	/**
	 * Creates new form <code>ZedLogFrame</code>.
	 *
	 */
	public ZedLogFrame() {

		initComponents();

		// set frame icon
		ImageIcon ico = (ImageIcon) Icons.getInstance().getIcon("zedlog");
		setIconImage(ico.getImage());

		initListeners();

		// center on screen
		setLocationRelativeTo(null);

		initLoggers();

		// add the gui logger handlers
		Logger.addHandler(new MsgBoxLogHandler(LogLevel.WARNING));
		Logger.addHandler(logWindow);

		GlobalScreen.getInstance().addNativeMouseListener(this);

	}

	/**
	 * Shuts-down the ZedLog frame.
	 *
	 */
	public void shutdown() {

		logger.info("Shutting down GUI.");

		loggers.shutdown();
		setVisible(false);

	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		shutdown();
	}

	private void initLoggers() {
		loggers = new CompositeDataLogger();
		addLoggerTab(loggers);
	}

	// initialise the event listeners
	private void initListeners() {

		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

		btnHide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnHideActionPerformed(event);
            }
        });

		btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnAddActionPerformed(event);
            }
        });

		btnClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllActionPerformed(evt);
            }
        });

		btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnRemoveActionPerformed(event);
            }
        });

		btnPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                btnPauseActionPerformed(event);
            }
        });

		mitemHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemHideActionPerformed(evt);
            }
        });

		mitemAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemAddActionPerformed(evt);
            }
        });

		mitemRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemRemoveActionPerformed(evt);
            }
        });

		mitemClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemClearAllActionPerformed(evt);
            }
        });

		mitemReplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemReplayActionPerformed(evt);
            }
        });

		mitemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemSaveActionPerformed(evt);
            }
        });

		mitemMsgLogFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemMsgLogFileActionPerformed(evt);
            }
        });

		mitemLogWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemLogWindowActionPerformed(evt);
            }
        });

        mitemQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemQuitActionPerformed(evt);
            }
        });

		mitemHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                mitemHelpActionPerformed(event);
            }
        });

		mitemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                mitemAboutActionPerformed(event);
            }
        });

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	//@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        tabs = new javax.swing.JTabbedPane();
        javax.swing.JToolBar toolbar = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        javax.swing.JToolBar.Separator jSeparator4 = new javax.swing.JToolBar.Separator();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnClearAll = new javax.swing.JButton();
        javax.swing.JToolBar.Separator jSeparator2 = new javax.swing.JToolBar.Separator();
        btnPause = new javax.swing.JButton();
        javax.swing.JToolBar.Separator jSeparator3 = new javax.swing.JToolBar.Separator();
        btnHide = new javax.swing.JButton();
        menubar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        mitemSave = new javax.swing.JMenuItem();
        mitemSetLogFile = new javax.swing.JMenuItem();
        mitemOpenLogFile = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mitemQuit = new javax.swing.JMenuItem();
        menuLoggers = new javax.swing.JMenu();
        mitemAdd = new javax.swing.JMenuItem();
        mitemRemove = new javax.swing.JMenuItem();
        mitemClearAll = new javax.swing.JMenuItem();
        menuTools = new javax.swing.JMenu();
        mitemHide = new javax.swing.JMenuItem();
        mitemReplay = new javax.swing.JMenuItem();
        sep = new javax.swing.JPopupMenu.Separator();
        menuProgramLog = new javax.swing.JMenu();
        mitemMsgLogFile = new javax.swing.JMenuItem();
        mitemLogWindow = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        mitemHelp = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator sep1 = new javax.swing.JPopupMenu.Separator();
        mitemAbout = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(Config.INSTANCE.FULL_NAME);
        setPreferredSize(new java.awt.Dimension(500, 400));

        toolbar.setFloatable(false);
        toolbar.setRollover(true);

        btnSave.setIcon(Icons.getInstance().getIcon("save"));
        btnSave.setMnemonic('S');
        btnSave.setText("Set Log");
        btnSave.setToolTipText("Set the log file to which log entries are automatically stored.");
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        toolbar.add(btnSave);

        btnOpen.setIcon(Icons.getInstance().getIcon("open"));
        btnOpen.setMnemonic('O');
        btnOpen.setText("Open Log");
        btnOpen.setToolTipText("Open a previously created log file.");
        btnOpen.setFocusable(false);
        btnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        toolbar.add(btnOpen);
        toolbar.add(jSeparator4);

        btnAdd.setIcon(Icons.getInstance().getIcon("add"));
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add ");
        btnAdd.setToolTipText("Add a new logger to ZedLog.");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(btnAdd);

        btnRemove.setIcon(Icons.getInstance().getIcon("remove"));
        btnRemove.setMnemonic('R');
        btnRemove.setText("Remove");
        btnRemove.setToolTipText("Remove the selected logger from ZedLog.");
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(btnRemove);

        btnClearAll.setIcon(Icons.getInstance().getIcon("clear"));
        btnClearAll.setMnemonic('C');
        btnClearAll.setText("Clear");
        btnClearAll.setToolTipText("Clears all log entries from all loggers.");
        btnClearAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClearAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(btnClearAll);
        toolbar.add(jSeparator2);

        btnPause.setIcon(Icons.getInstance().getIcon("pause"));
        btnPause.setText("Pause");
        btnPause.setToolTipText("Pause/resume recording events.");
        btnPause.setFocusable(false);
        btnPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(btnPause);
        toolbar.add(jSeparator3);

        btnHide.setIcon(Icons.getInstance().getIcon("hide"));
        btnHide.setMnemonic('H');
        btnHide.setText("Hide");
        btnHide.setToolTipText("Hide the ZedLog window.");
        btnHide.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHide.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(btnHide);

        menuFile.setText("File");

        mitemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitemSave.setMnemonic('S');
        mitemSave.setText("Save");
        mitemSave.setToolTipText("Save the composite logger output.");
        menuFile.add(mitemSave);

        mitemSetLogFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mitemSetLogFile.setIcon(Icons.getInstance().getIcon("save"));
        mitemSetLogFile.setMnemonic('L');
        mitemSetLogFile.setText("Set Log File");
        mitemSetLogFile.setToolTipText("Set the log file to which log entries are automatically stored.");
        mitemSetLogFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemSetLogFileActionPerformed(evt);
            }
        });
        menuFile.add(mitemSetLogFile);

        mitemOpenLogFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mitemOpenLogFile.setIcon(Icons.getInstance().getIcon("open"));
        mitemOpenLogFile.setMnemonic('O');
        mitemOpenLogFile.setText("Open Log File");
        mitemOpenLogFile.setToolTipText("Open a previously created log file.");
        mitemOpenLogFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemOpenLogFileActionPerformed(evt);
            }
        });
        menuFile.add(mitemOpenLogFile);
        menuFile.add(jSeparator1);

        mitemQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        mitemQuit.setMnemonic('q');
        mitemQuit.setText("Quit");
        mitemQuit.setToolTipText("Quit ZedLog.");
        menuFile.add(mitemQuit);

        menubar.add(menuFile);

        menuLoggers.setMnemonic('L');
        menuLoggers.setText("Loggers");

        mitemAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.CTRL_MASK));
        mitemAdd.setIcon(Icons.getInstance().getIcon("add"));
        mitemAdd.setMnemonic('A');
        mitemAdd.setText("Add Logger");
        mitemAdd.setToolTipText("Add a new logger to ZedLog.");
        menuLoggers.add(mitemAdd);

        mitemRemove.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        mitemRemove.setIcon(Icons.getInstance().getIcon("remove"));
        mitemRemove.setMnemonic('R');
        mitemRemove.setText("Remove Logger");
        mitemRemove.setToolTipText("Remove the selected logger from ZedLog.");
        menuLoggers.add(mitemRemove);

        mitemClearAll.setIcon(Icons.getInstance().getIcon("clear"));
        mitemClearAll.setText("Clear All");
        menuLoggers.add(mitemClearAll);

        menubar.add(menuLoggers);

        menuTools.setMnemonic('T');
        menuTools.setText("Tools");

        mitemHide.setIcon(Icons.getInstance().getIcon("hide"));
        mitemHide.setMnemonic('H');
        mitemHide.setText("Hide");
        mitemHide.setToolTipText("Hide the ZedLog window.");
        menuTools.add(mitemHide);

        mitemReplay.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        mitemReplay.setMnemonic('R');
        mitemReplay.setText("Replay Events");
        mitemReplay.setToolTipText("Replay/simulate the events recorded by ZedLog.");
        menuTools.add(mitemReplay);
        menuTools.add(sep);

        menuProgramLog.setText("Program Log");
        menuProgramLog.setToolTipText(null);

        mitemMsgLogFile.setText("Set Log File");
        mitemMsgLogFile.setToolTipText("Set the program log file.");
        menuProgramLog.add(mitemMsgLogFile);

        mitemLogWindow.setText("Show Log Window");
        mitemLogWindow.setToolTipText("Show the program log window.");
        menuProgramLog.add(mitemLogWindow);

        menuTools.add(menuProgramLog);

        menubar.add(menuTools);

        menuHelp.setMnemonic('H');
        menuHelp.setText("Help");

        mitemHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mitemHelp.setMnemonic('H');
        mitemHelp.setText("Help");
        mitemHelp.setToolTipText("Display the program documentation.");
        menuHelp.add(mitemHelp);
        menuHelp.add(sep1);

        mitemAbout.setIcon(Icons.getInstance().getIcon("about"));
        mitemAbout.setMnemonic('A');
        mitemAbout.setText("About");
        mitemAbout.setToolTipText("Display information about ZedLog.");
        menuHelp.add(mitemAbout);

        menubar.add(menuHelp);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("zedlog");

        pack();
    }// </editor-fold>//GEN-END:initComponents

	// removes teh logger tabs from the frame
	private void removeLoggerTabs() {

		// remove the logger tabs
		while (tabs.getTabCount() > 0)
			tabs.remove(0);

	}

	// re-initialise the loggers display
	private void reInitLoggersView() {

		removeLoggerTabs();

		// re-add the composite logger tab
		addLoggerTab(loggers);

		// re-add the composite loggers children loggers
		for (DataLogger logger : loggers.getLoggers())
			addLoggerTab(logger);

	}

	private void setLogFile() {

		File logFile = SimpleDialog.saveFile(this);

		// select and set the new log file
		try {

			if (logFile != null)
				loggers.setLogFile(logFile);

		} catch (IOException ex) {

			if (logFile == null) {
				logger.error("Failed to set log file.", ex);
			} else {
				logger.error("Failed to set log file %s.", ex, logFile.getPath());
			}

		}

	}

    private void mitemSetLogFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitemSetLogFileActionPerformed
		setLogFile();

    }//GEN-LAST:event_mitemSetLogFileActionPerformed

	private void openLogFile() {

		File logFile = SimpleDialog.openFile(this);
		if (logFile == null) return;

		// select and set the new log file
		try {

			logger.info("Opening log file %s.", null, logFile.toString());

			// re-initialise the composite logger
			removeLoggerTabs();
			initLoggers();

			loggers.openLogFile(logFile);

		} catch (FileNotFoundException ex) {
			logger.error("Log file %s does not exist!", ex, logFile.getPath());
		} catch (IOException ex) {
			logger.error("Failed to open log file %s.", ex, logFile.getPath());
		} catch (Exception ex) {

			logger.error(
				"Failed to read the log file %s.  It may be corrupt.",
				ex, logFile.getPath()
			);

		}

		logger.info("Log file %s opened successfully.", null, logFile.getPath());

	}

    private void mitemOpenLogFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitemOpenLogFileActionPerformed
        openLogFile();

    }//GEN-LAST:event_mitemOpenLogFileActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
		setLogFile();

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        openLogFile();

    }//GEN-LAST:event_btnOpenActionPerformed

	private void addLoggerTab(final DataLogger logger) {

		assert(logger != null);

		// create logger panel view
		LoggerPanel loggerPanel = new LoggerPanel(logger);

		// add a logger panel tab for the new logger
		tabs.add(logger.type(), loggerPanel);

	}

	private void userQuit() {

		if (loggers.getLogStream() == null) {

			boolean quit = SimpleDialog.yesno(
				this, "Really Quit?",
				"The log file has not been set, log data may be lost.\n"
				+ "Are you sure you want to quit?"
			);

			if (!quit) {
				logger.info("Quit cancelled.");
				return;
			}

		}

		logger.info("User quit.");

		setVisible(false);
		System.exit(0);

	}

	private void formWindowClosing(WindowEvent evt) {
		userQuit();
	}

	// show or hide the ZedLog frame
	private void showHide() {

		boolean show = !isVisible();

		setVisible(show);

		// give the window focus
		if (show) {
			toFront();
			requestFocus();
		}

	}

	private void addDataLogger() {

		NewLoggerDialog dialog = new NewLoggerDialog(this, true);
		DataLogger dataLogger = dialog.getLoggerFromUser();

		// dont add if logger not specified
		if (dataLogger == null)
			return;

		// add the logger
		try {
			loggers.addLogger(dataLogger);
		} catch (IOException ex) {

			String msg =
				String.format("Failed to add %s logger!", dataLogger.type());
			logger.error(msg, ex);

			return;

		}

		addLoggerTab(dataLogger);

		logger.info(String.format("Added logger %s", dataLogger.type()));

	}

	private void removeDataLogger() {

		if (tabs.getSelectedIndex() > 0) {

			DataLogger dataLogger = loggers.getLogger(tabs.getSelectedIndex() - 1);

			// catch attempt to remove CompositeDataLogger
			if (dataLogger instanceof CompositeDataLogger) {
				logger.error("Cannot remove composite logger!");
			} else {

				// remove the logger from the composite logger
				try {
					loggers.removeLogger(dataLogger);
				} catch (IOException ex) {

					String msg =
						String.format("Failed to remove %s logger!", dataLogger.type());
					logger.error(msg,ex);

					// XXX continue despite error and remove from GUI

				}

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

	private void clearAll() {

		try {
			loggers.clearAll();
		} catch (IOException ex) {
			logger.error("Failed to clear data loggers!", ex);
		}

		reInitLoggersView();

		logger.info("Cleared data loggers.");

	}

	private void btnClearAllActionPerformed(final ActionEvent event) {
		clearAll();
    }

	private void mitemClearAllActionPerformed(final ActionEvent event) {
		clearAll();
    }

	private void btnPauseActionPerformed(final ActionEvent event) {

		loggers.setRecording(!loggers.isRecording());

		if (loggers.isRecording()) {

			btnPause.setIcon(Icons.getInstance().getIcon("pause"));
			btnPause.setText("Pause");

			logger.info("Recording resumed.");

		} else {

			btnPause.setIcon(Icons.getInstance().getIcon("record"));
			btnPause.setText("Record");

			logger.info("Recording paused.");

		}

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

	private void mitemReplayActionPerformed(ActionEvent evt) {
		ReplayToolDialog replayTool = new ReplayToolDialog(this, loggers);
		replayTool.setVisible(true);
	}

	private void mitemSaveActionPerformed(ActionEvent evt) {

		File saveFile = SimpleDialog.saveFile(this);
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

	private void mitemLogWindowActionPerformed(ActionEvent evt) {
		logWindow.setVisible(true);
	}

	private void mitemMsgLogFileActionPerformed(ActionEvent evt) {

		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showSaveDialog(this);

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

        AboutDialog aboutBox = new AboutDialog(this, true);
		aboutBox.setVisible(true);

    }

	private void mitemHelpActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			HelpDoc.getInstance().showInBrowser();
		} catch (Exception ex) {
			logger.error("Cannot open help documentation in the web browser!", ex);
		}

    }

	@Override
	public void nativeMouseClicked(NativeMouseEvent event) {

		if (event.getButton() == 3 && event.getPoint().equals(new Point(0, 0))) {
			// FIXME This really needs to be done better.

			showHide();

		}

	}

	@Override
	public void nativeMousePressed(NativeMouseEvent event) {
		// IGNORED
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent nme) {
		// IGNORED
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClearAll;
    private javax.swing.JButton btnHide;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenu menuLoggers;
    private javax.swing.JMenu menuProgramLog;
    private javax.swing.JMenu menuTools;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JMenuItem mitemAbout;
    private javax.swing.JMenuItem mitemAdd;
    private javax.swing.JMenuItem mitemClearAll;
    private javax.swing.JMenuItem mitemHelp;
    private javax.swing.JMenuItem mitemHide;
    private javax.swing.JMenuItem mitemLogWindow;
    private javax.swing.JMenuItem mitemMsgLogFile;
    private javax.swing.JMenuItem mitemOpenLogFile;
    private javax.swing.JMenuItem mitemQuit;
    private javax.swing.JMenuItem mitemRemove;
    private javax.swing.JMenuItem mitemReplay;
    private javax.swing.JMenuItem mitemSave;
    private javax.swing.JMenuItem mitemSetLogFile;
    private javax.swing.JPopupMenu.Separator sep;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables

}
