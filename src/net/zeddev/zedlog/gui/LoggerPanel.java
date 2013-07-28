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

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.DataLoggerObserver;
import net.zeddev.zedlog.logger.LogEntry;
import static net.zeddev.zedlog.util.Assertions.*;

/**
 * {@code DataLogger} view component.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class LoggerPanel extends JPanel implements DataLoggerObserver {

	private final DataLogger logger;

	/** Creates new form {@code LoggerPanel}. */
	public LoggerPanel(final DataLogger logger) {

		super();
		
		requireNotNull(logger);

		this.logger = logger;
		logger.addObserver(this);

		initComponents();
		buildForm();
		
	}
	
	/* --------  GUI INITIALISATION -------- */

	// initialises the GUI components
	private void initComponents() {

		txtLogEntries.setEditable(false);
		
		txtLogEntries.setColumns(20);
		txtLogEntries.setRows(5);
		txtLogEntries.setLineWrap(true);
		
	}
	
	// builds the form 
	private void buildForm() {
		
		setLayout(new GridLayout(0, 1));
		
		JScrollPane scrollLogEntries = new javax.swing.JScrollPane();

		scrollLogEntries.setDoubleBuffered(true);
		scrollLogEntries.setEnabled(false);
		
		scrollLogEntries.setViewportView(txtLogEntries);
		
		add(scrollLogEntries);
		
	}
	
	/* --------  END GUI INITIALISATION -------- */
	
	public JTextArea getTxtLogEntries() {
		return txtLogEntries;
	}

	public void shutdown() {
		logger.removeObserver(this);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		shutdown();
	}

	private DataLogger lastToNotify = null;

	private void addLog(final DataLogger logger, final LogEntry logEntry) {

		StringBuilder logEntries = new StringBuilder(getTxtLogEntries().getText());

		// add newline to separate different logger messages
		if (lastToNotify == null) {
			lastToNotify = logger;
		} else if (logger != lastToNotify) {

			// dont append if already a newline
			if (logEntries.charAt(logEntries.length()-1) != '\n')
				logEntries.append("\n");

			lastToNotify = logger;

		}

		logEntries.append(logEntry.getMessage());

		getTxtLogEntries().setText(logEntries.toString());

		// move the end of the text area
		getTxtLogEntries().setCaretPosition(
			getTxtLogEntries().getText().length() - 1
		);


	}

	@Override
	public void notifyLog(final DataLogger logger, final LogEntry logEntry) {

		// Removed 2013-05-07
		//SwingUtilities.invokeLater(new Runnable() {
		//	public void run(){
		//		addLog(logger, logEntry);
		//	}
		//});

		addLog(logger, logEntry);

	}

	// form elements
	private JTextArea txtLogEntries = new JTextArea();
	
}
