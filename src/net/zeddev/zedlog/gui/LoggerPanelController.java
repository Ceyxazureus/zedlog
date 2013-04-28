
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
