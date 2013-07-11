package net.zeddev.zedlog.gui;
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

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

	/** Creates new form {@code ZedLogFrame}. */
	public ZedLogFrame() {

		initComponents();
		buildForm();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle(Config.INSTANCE.FULL_NAME);
		
		setMinimumSize(new Dimension(400, 300));
		
		// set frame icon
		ImageIcon ico = (ImageIcon) Icons.getInstance().getIcon("zedlog");
		setIconImage(ico.getImage());

		// center on screen
		setLocationRelativeTo(null);

		initLoggers();

		// add the gui logger handlers
		Logger.addHandler(new MsgBoxLogHandler(LogLevel.WARNING));
		Logger.addHandler(logWindow);

		GlobalScreen.getInstance().addNativeMouseListener(this);

	}
	
	/** Shuts-down the ZedLog frame. */
	public void shutdown() {

		logger.info("Shutting down GUI.");

		loggers.shutdown();
		
		setVisible(false);
		dispose();

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

	// removes the logger tabs from the frame
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

	/* --------  GUI INITIALISATION  -------- */
	
	// initialises the GUI components
	private void initComponents() {

		btnSave.setIcon(Icons.getInstance().getIcon("save"));
		btnSave.setMnemonic('S');
		btnSave.setToolTipText("Set the log file to which log entries are automatically stored.");
		btnSave.setFocusable(false);

		btnOpen.setIcon(Icons.getInstance().getIcon("open"));
		btnOpen.setMnemonic('O');
		btnOpen.setToolTipText("Open a previously created log file.");
		btnOpen.setFocusable(false);

		btnAdd.setIcon(Icons.getInstance().getIcon("add"));
		btnAdd.setMnemonic('A');
		btnAdd.setToolTipText("Add a new logger to ZedLog.");

		btnRemove.setIcon(Icons.getInstance().getIcon("remove"));
		btnRemove.setMnemonic('R');
		btnRemove.setToolTipText("Remove the selected logger from ZedLog.");

		btnClearAll.setIcon(Icons.getInstance().getIcon("clear"));
		btnClearAll.setMnemonic('C');
		btnClearAll.setToolTipText("Clears all log entries from all loggers.");

		btnPause.setIcon(Icons.getInstance().getIcon("pause"));
		btnPause.setToolTipText("Pause/resume recording events.");
		btnPause.setFocusable(false);

		btnHide.setIcon(Icons.getInstance().getIcon("hide"));
		btnHide.setMnemonic('H');
		btnHide.setToolTipText("Hide the ZedLog window.");

		mitemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		mitemSave.setMnemonic('S');
		mitemSave.setText("Save");
		mitemSave.setToolTipText("Save the composite logger output.");
		
		mitemSetLogFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
		mitemSetLogFile.setIcon(Icons.getInstance().getIcon("save"));
		mitemSetLogFile.setMnemonic('L');
		mitemSetLogFile.setText("Set Log File");
		mitemSetLogFile.setToolTipText("Set the log file to which log entries are automatically stored.");
		
		mitemOpenLogFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		mitemOpenLogFile.setIcon(Icons.getInstance().getIcon("open"));
		mitemOpenLogFile.setMnemonic('O');
		mitemOpenLogFile.setText("Open Log File");
		mitemOpenLogFile.setToolTipText("Open a previously created log file.");
		
		mitemQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
		mitemQuit.setMnemonic('q');
		mitemQuit.setText("Quit");
		mitemQuit.setToolTipText("Quit ZedLog.");

		mitemAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.CTRL_MASK));
		mitemAdd.setIcon(Icons.getInstance().getIcon("add"));
		mitemAdd.setMnemonic('A');
		mitemAdd.setText("Add Logger");
		mitemAdd.setToolTipText("Add a new logger to ZedLog.");
		
		mitemRemove.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
		mitemRemove.setIcon(Icons.getInstance().getIcon("remove"));
		mitemRemove.setMnemonic('R');
		mitemRemove.setText("Remove Logger");
		mitemRemove.setToolTipText("Remove the selected logger from ZedLog.");
		
		mitemClearAll.setIcon(Icons.getInstance().getIcon("clear"));
		mitemClearAll.setText("Clear All");
		
		mitemHide.setIcon(Icons.getInstance().getIcon("hide"));
		mitemHide.setMnemonic('H');
		mitemHide.setText("Hide");
		mitemHide.setToolTipText("Hide the ZedLog window.");
		
		mitemReplay.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
		mitemReplay.setMnemonic('R');
		mitemReplay.setText("Replay Events");
		mitemReplay.setToolTipText("Replay/simulate the events recorded by ZedLog.");

		mitemMsgLogFile.setText("Set Log File");
		mitemMsgLogFile.setToolTipText("Set the program log file.");

		mitemLogWindow.setText("Show Log Window");
		mitemLogWindow.setToolTipText("Show the program log window.");

		mitemHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		mitemHelp.setMnemonic('H');
		mitemHelp.setText("Help");
		mitemHelp.setToolTipText("Display the program documentation.");

		mitemAbout.setIcon(Icons.getInstance().getIcon("about"));
		mitemAbout.setMnemonic('A');
		mitemAbout.setText("About");
		mitemAbout.setToolTipText("Display information about ZedLog.");

		initListeners();
		
	}
		
	// initialises the GUI listeners
	private void initListeners() {
		
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});
		
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSaveActionPerformed(evt);
			}
		});
		
		btnOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOpenActionPerformed(evt);
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
		
		btnHide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnHideActionPerformed(event);
			}
		});
		
		mitemSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mitemSaveActionPerformed(evt);
			}
		});
		
		mitemOpenLogFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mitemOpenLogFileActionPerformed(evt);
			}
		});
		
		mitemSetLogFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mitemSetLogFileActionPerformed(evt);
			}
		});
		
		mitemQuit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mitemQuitActionPerformed(evt);
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
		
		mitemHide.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mitemHideActionPerformed(evt);
			}
		});

		mitemReplay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mitemReplayActionPerformed(evt);
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
	
	// builds the menu bar
	private void buildMenuBar() {
		
		JMenuBar menubar = new JMenuBar();
		
		// ---- file menu ----
		
		menuFile.setText("File");
		
		menuFile.add(mitemSave);

		menuFile.add(mitemSetLogFile);

		menuFile.add(mitemOpenLogFile);
		menuFile.add(new JPopupMenu.Separator());

		menuFile.add(mitemQuit);

		menubar.add(menuFile);
		
		// ---- loggers menu ----

		menuLoggers.setMnemonic('L');
		menuLoggers.setText("Loggers");

		menuLoggers.add(mitemAdd);

		menuLoggers.add(mitemRemove);

		menuLoggers.add(mitemClearAll);

		menubar.add(menuLoggers);

		// ---- tools menu ----
		
		menuTools.setMnemonic('T');
		menuTools.setText("Tools");

		menuTools.add(mitemHide);

		menuTools.add(mitemReplay);
		menuTools.add(new JPopupMenu.Separator());

		menuProgramLog.setText("Program Log");
		menuProgramLog.setToolTipText(null);

		menuProgramLog.add(mitemMsgLogFile);

		mitemLogWindow.setText("Show Log Window");
		mitemLogWindow.setToolTipText("Show the program log window.");
		menuProgramLog.add(mitemLogWindow);

		menuTools.add(menuProgramLog);

		menubar.add(menuTools);

		// ---- help menu ----
		
		menuHelp.setMnemonic('H');
		menuHelp.setText("Help");

		menuHelp.add(mitemHelp);
		menuHelp.add(new JPopupMenu.Separator());

		menuHelp.add(mitemAbout);

		menubar.add(menuHelp);

		// set the frames menubar
		setJMenuBar(menubar);
		
	}
	
	// builds the toolbar
	private JPanel buildToolbar() {
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(true);
		toolbar.setRollover(true);
		
		toolbar.add(btnSave);
		toolbar.add(btnOpen);
		
		toolbar.add(new JToolBar.Separator());
		
		toolbar.add(btnAdd);
		toolbar.add(btnRemove);
		toolbar.add(btnClearAll);
		
		toolbar.add(new JToolBar.Separator());
		
		toolbar.add(btnPause);
		
		toolbar.add(new JToolBar.Separator());
		
		toolbar.add(btnHide);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		panel.add(toolbar);
		
		return panel;
		
	}
	
	// builds the form 
	private void buildForm() {
		
		setLayout(new BorderLayout());
		
		buildMenuBar();
		
		add(buildToolbar(), BorderLayout.NORTH);
		add(tabs, BorderLayout.CENTER);
		
		pack();
		
	}
	
	/* --------  END GUI INITIALISATION  -------- */

	private void setLogFile() {

		File logFile = SimpleDialog.saveFile(this);

		// select and set the new log file
		try {

			if (logFile == null) {
				logger.error("Failed to set log file.");
			} else {
				loggers.setLogFile(logFile);
			}

		} catch (IOException ex) {

			logger.error("Failed to set log file %s.", ex, logFile.getPath());

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
	
	private void clearAll() {

		try {
			loggers.clearAll();
		} catch (IOException ex) {
			logger.error("Failed to clear data loggers!", ex);
		}

		reInitLoggersView();

		logger.info("Cleared data loggers.");

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
	
	private void addLoggerTab(final DataLogger logger) {

		assert(logger != null);

		// create logger panel view
		LoggerPanel loggerPanel = new LoggerPanel(logger);

		// add a logger panel tab for the new logger
		tabs.add(logger.type(), loggerPanel);

	}
	
	private void saveToFile(File saveFile) throws IOException {

		FileWriter output = new FileWriter(saveFile);
		output.write(loggers.toString());
		output.close();

	}
	
	/* --------  EVENT HANDLERS  -------- */
	
	private void formWindowClosing(WindowEvent evt) {
		userQuit();
	}
	
	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
		setLogFile();
	}

	private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {
		openLogFile();
	}
	
	private void btnAddActionPerformed(final ActionEvent event) {
		addDataLogger();
	}

	private void btnRemoveActionPerformed(final ActionEvent event) {
		removeDataLogger();
	}
	
	private void btnClearAllActionPerformed(final ActionEvent event) {
		clearAll();
	}
	
	private void btnHideActionPerformed(final ActionEvent event) {
		showHide();
	}
	
	private void btnPauseActionPerformed(final ActionEvent event) {

		loggers.setRecording(!loggers.isRecording());

		if (loggers.isRecording()) {

			btnPause.setIcon(Icons.getInstance().getIcon("pause"));

			logger.info("Recording resumed.");

		} else {

			btnPause.setIcon(Icons.getInstance().getIcon("record"));

			logger.info("Recording paused.");

		}

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

	private void mitemSetLogFileActionPerformed(java.awt.event.ActionEvent evt) {
		setLogFile();
	}
	
	private void mitemOpenLogFileActionPerformed(java.awt.event.ActionEvent evt) {
		openLogFile();

	}
	
	private void mitemQuitActionPerformed(ActionEvent evt) {
		userQuit();
	}
	
	private void mitemAddActionPerformed(ActionEvent evt) {
		addDataLogger();
	}
	
	private void mitemRemoveActionPerformed(ActionEvent evt) {
		removeDataLogger();
	}

	private void mitemClearAllActionPerformed(final ActionEvent event) {
		clearAll();
	}

	private void mitemHideActionPerformed(ActionEvent evt) {
		showHide();
	}

	private void mitemReplayActionPerformed(ActionEvent evt) {
		ReplayToolDialog replayTool = new ReplayToolDialog(this, loggers);
		replayTool.setVisible(true);
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

	private void mitemLogWindowActionPerformed(ActionEvent evt) {
		logWindow.setVisible(true);
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

	/* --------  END EVENT HANDLERS  -------- */
	
	/* --------  FORM ELEMENTS  -------- */
	
	private JButton btnSave = new JButton();
	private JButton btnOpen = new JButton();
	private JButton btnAdd = new JButton();
	private JButton btnRemove = new JButton();
	private JButton btnClearAll = new JButton();
	private JButton btnHide = new JButton();
	private JButton btnPause = new JButton();
	
	private JMenu menuFile = new JMenu();
	private JMenu menuLoggers = new JMenu();
	private JMenu menuTools = new JMenu();
	private JMenu menuProgramLog = new JMenu();
	private JMenu menuHelp = new JMenu();
	
	private JMenuItem mitemSave = new JMenuItem();
	private JMenuItem mitemSetLogFile = new JMenuItem();
	private JMenuItem mitemOpenLogFile = new JMenuItem();
	private JMenuItem mitemQuit = new JMenuItem();
	
	private JMenuItem mitemAdd = new JMenuItem();
	private JMenuItem mitemRemove = new JMenuItem();
	private JMenuItem mitemClearAll = new JMenuItem();
	
	private JMenuItem mitemHide = new JMenuItem();
	private JMenuItem mitemReplay = new JMenuItem();
	private JMenuItem mitemMsgLogFile = new JMenuItem();
	private JMenuItem mitemLogWindow = new JMenuItem();
	
	private JMenuItem mitemAbout = new JMenuItem();
	private JMenuItem mitemHelp = new JMenuItem();
	
	private JTabbedPane tabs = new JTabbedPane();
	
	/* --------  END FORM ELEMENTS  -------- */
	
}
