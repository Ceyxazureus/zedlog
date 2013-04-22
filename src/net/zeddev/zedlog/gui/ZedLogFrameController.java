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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import net.zeddev.zedlog.HelpDoc;
import net.zeddev.zedlog.gui.dialog.NewLoggerDialog;
import net.zeddev.zedlog.gui.dialog.SimpleDialog;
import net.zeddev.zedlog.gui.dialog.AboutDialog;
import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.impl.CompositeDataLogger;

/**
 * Controller for the <code>ZedLogFrame</code>.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class ZedLogFrameController {

	private static final Logger LOGGER = Logger.getLogger(ZedLogFrameController.class.getName());

	// the view
	private final ZedLogFrame frame;

	// the data
	private final CompositeDataLogger loggers;

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

	private void btnAddActionPerformed(final ActionEvent event) {

		NewLoggerDialog dialog = new NewLoggerDialog(frame, true);
		DataLogger logger = dialog.getLoggerFromUser();

		// dont add if logger not specified
		if (logger == null)
			return;

		getLoggers().addLogger(logger);

		addLoggerTab(logger);

		LOGGER.info(String.format("Added logger %s", logger.type()));

    }

	private void btnRemoveActionPerformed(final ActionEvent event) {

		JTabbedPane tabs = frame.getTabs();

		if (tabs.getSelectedIndex() > 0) {

			DataLogger logger = loggers.getLogger(tabs.getSelectedIndex() - 1);

			// catch attempt to remove CompositeDataLogger
			if (logger instanceof CompositeDataLogger) {

				SimpleDialog.error(frame, "Cannot remove composite logger!");

			} else {

				loggers.removeLogger(logger);
				tabs.remove(tabs.getSelectedIndex());

			}

		}

    }

	private void mitemAboutActionPerformed(java.awt.event.ActionEvent evt) {
        AboutDialog aboutBox = new AboutDialog(frame, true);
		aboutBox.setVisible(true);
    }

	private void mitemHelpActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			HelpDoc.INSTANCE.showInBrowser();
		} catch (IOException ex) {
			Logger.getLogger(ZedLogFrameController.class.getName()).log(Level.SEVERE, null, ex);
		}

    }

}
