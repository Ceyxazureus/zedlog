
package net.zeddev.zedlog.gui;

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

	@Override
	public void notifyLog(final DataLogger logger, final LogEntry logEntry) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				loggerPanel.getTxtLogEntries().append(logEntry.getMessage());
			}
		});

	}

}
