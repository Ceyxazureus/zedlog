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

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.DataLoggerObserver;
import net.zeddev.zedlog.logger.LogEntry;

/**
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class LoggerPanelController implements DataLoggerObserver {

	// the view
	private final LoggerPanel loggerPanel;

	// the data
	private final DataLogger logger;

	public LoggerPanelController(final LoggerPanel loggerPanel, final DataLogger logger) {

		assert(loggerPanel != null);
		assert(logger != null);

		this.loggerPanel = loggerPanel;
		this.logger = logger;

		logger.addObserver(this);

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

		StringBuilder logEntries = new StringBuilder(loggerPanel.getTxtLogEntries().getText());

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

		loggerPanel.getTxtLogEntries().setText(logEntries.toString());

	}

	@Override
	public void notifyLog(final DataLogger logger, final LogEntry logEntry) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				addLog(logger, logEntry);
			}
		});

	}

}
