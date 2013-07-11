package net.zeddev.zedlog.gui.dialog;
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

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.logger.LogEntry;
import net.zeddev.zedlog.logger.LogEvent;
import net.zeddev.zedlog.logger.impl.CompositeDataLogger;
import net.zeddev.zedlog.logger.tools.ReplayTool;
import net.zeddev.zedlog.logger.tools.ReplayToolObserver;

/**
 * Windows for running the {@code RepeatTool}.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class ReplayToolDialog extends JDialog implements ReplayToolObserver {

	private final Logger logger = Logger.getLogger(this);

	private List<LogEntry> logEntries;
	
	private final ReplayTool tool;

	/**
	 * Creates new form {@code ReplayToolDialog}.
	 *
	 * @param loggers The
	 */
	public ReplayToolDialog(final Frame parent, final CompositeDataLogger loggers) {

		super(parent, true);

		logEntries = loggers.logEntries();
		
		tool = new ReplayTool(logEntries);
		tool.addObserver(this);

		initComponents();
		buildForm();
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
        setTitle("Replay Events Tool");
		
		setLocationRelativeTo(null);

	}

	// initialises the GUI components
	private void initComponents() {
		
		// add window listener
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
		
		// setup the progress bar limits
		progressBar.setMaximum(logEntries.size());
		progressBar.setMinimum(0);
		
        progressBar.setEnabled(false);

        lblCurrent.setText(" ");
		
        btnRunTimed.setMnemonic('T');
        btnRunTimed.setToolTipText("Replay the events, with original timing.");
        btnRunTimed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunTimedActionPerformed(evt);
            }
        });
        
        btnClose.setMnemonic('C');
        btnClose.setToolTipText("Close the replay tool.");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        
        btnRun.setMnemonic('R');
        btnRun.setToolTipText("Replay events as-fast-as-possible.");
        btnRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunActionPerformed(evt);
            }
        });
        
	}
	
	// adds a component to the next 'line' on the GUI
	private void addLine(Box box, JComponent comp) {
		
		// add the component
		box.add(comp);
		comp.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// space it out a bit
		box.add(Box.createVerticalStrut(SPACING));
		
	}
	
	// builds the box which contains the command buttons
	private Box buttonBox() {
		
		Box buttons = new Box(BoxLayout.LINE_AXIS);
		
		buttons.add(btnRun);
		buttons.add(Box.createHorizontalStrut(SPACING));
		
		buttons.add(btnRunTimed);
		buttons.add(Box.createHorizontalStrut(SPACING));
		
		buttons.add(btnClose);
		
		return buttons;
		
	}
	
	// builds the form 
	private void buildForm() {
		
		Box outer = new Box(BoxLayout.LINE_AXIS);
		outer.add(Box.createHorizontalStrut(SPACING));
		
			Box inner = new Box(BoxLayout.PAGE_AXIS);
			inner.add(Box.createVerticalStrut(SPACING));
			
			addLine(inner, lblCurrent);
			addLine(inner, progressBar);
			addLine(inner, buttonBox());
			
			outer.add(inner);
		
		outer.add(Box.createHorizontalStrut(SPACING));
		add(outer);
		
		pack();
		
	}
	
    private void btnRunTimedActionPerformed(ActionEvent event) {
    	
		if (btnRunTimed.isSelected()) {

			progressBar.setValue(0);

			// start the simulation
			Thread toolThread = new Thread(tool.replayTimed());
			toolThread.start();

			logger.info("Timed replay started.");

		} else {
			tool.stop();
		}

    }

	private void shutdown() {
		
		tool.removeObserver(this);
		tool.stop();
		
		dispose();
		
	}

    private void btnCloseActionPerformed(ActionEvent event) {
		shutdown();
    }

    private void formWindowClosing(WindowEvent event) {
		shutdown();
    }

    private void btnRunActionPerformed(ActionEvent event) {
    	
        if (btnRun.isSelected()) {

			progressBar.setValue(0);

			// start the simulation
			Thread toolThread = new Thread(tool.replayFast());
			toolThread.start();

			logger.info("Fast replay started.");

		} else {
			tool.stop();
		}

    }

	@Override
	public void replayedEvent(LogEvent event) {

		lblCurrent.setText(event.toString());

		// update the progress bar
		int progress = progressBar.getValue();
		progressBar.setValue(progress + 1);

	}

	@Override
	public void replayFinished() {

		btnRunTimed.setSelected(false);
		btnRun.setSelected(false);

		logger.info("Replay tool finished.");

	}
	
	// the spacing between components
    private final static int SPACING = 10;

    // form elements
    private JButton btnClose = new JButton("Close");
    private JToggleButton btnRun = new JToggleButton("Run");
    private JToggleButton btnRunTimed = new JToggleButton("Run Timed");
    private JLabel lblCurrent = new JLabel();
    private JProgressBar progressBar = new JProgressBar();

}
