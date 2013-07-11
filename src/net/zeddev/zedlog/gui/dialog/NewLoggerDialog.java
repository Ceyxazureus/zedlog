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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.impl.DataLoggers;

/**
 * Dialog to create {@code DataLogger} from user input.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class NewLoggerDialog extends JDialog {

	private boolean cancelledFlag = false;

	/** Creates new form {@code NewLoggerDialog}. */
	public NewLoggerDialog(Frame parent, boolean modal) {

		super(parent, modal);

		initComponents();
		buildForm();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		
		setTitle("Add Logger");
		setName("");
		
		// center on screen
		setLocationRelativeTo(null);

	}
	
	/* --------  GUI INITIALISATION  -------- */
	
	// initialises the GUI components
	private void initComponents() {
		
		lblType.setText("Logger Type");
		
		initTypes();
		
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});
		
		btnOkay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOkayActionPerformed(evt);
			}
		});
		
	}

	// initialise the types combo box
	private void initTypes() {

		cboType.addItem("-- Select logger type --");

		// add the available data logger types
		for (String type : DataLoggers.typeList())
			cboType.addItem(type);

		cboType.setSelectedIndex(0);

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
		
		buttons.add(btnOkay);
		buttons.add(Box.createHorizontalStrut(SPACING));
		buttons.add(btnCancel);
		
		return buttons;
		
	}
	
	// builds the form 
	private void buildForm() {
		
		Box outer = new Box(BoxLayout.LINE_AXIS);
		outer.add(Box.createHorizontalStrut(SPACING));
		
			Box inner = new Box(BoxLayout.PAGE_AXIS);
			inner.add(Box.createVerticalStrut(SPACING));
			
			addLine(inner, lblType);
			addLine(inner, cboType);
			addLine(inner, buttonBox());
			
			outer.add(inner);
		
		outer.add(Box.createHorizontalStrut(SPACING));
		add(outer);
		
		pack();
		
	}

	/* --------  END GUI INITIALISATION  -------- */
	
	/**
	 * Builds a {@code DataLogger} from the user input.
	 *
	 * @return The {@code DataLogger} instance built from the user input.
	 */
	public DataLogger getLoggerFromUser() {

		setVisible(true);
			// wait for user to enter data on the dialog
			// ...

		if (cancelledFlag) {
			return null;
		} else {
			return DataLoggers.newDataLogger(cboType.getSelectedItem().toString());
		}

	}

	/* --------  EVENT HANDLING  -------- */
	
	private void btnOkayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkayActionPerformed
		setVisible(false);
	}

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		
		setVisible(false);
		dispose();
		
		cancelledFlag = true;

	}
	
	/* --------  END EVENT HANDLING  -------- */

	// the spacing between components
	private final static int SPACING = 10;
	
	/* --------  FORM ELEMENTS  -------- */
	
	private final JButton btnCancel = new JButton("Cancel");
	private final JButton btnOkay = new JButton("Okay");
	private final JComboBox cboType = new JComboBox();
	private final JLabel lblType = new JLabel();

	/* --------  END FORM ELEMENTS  -------- */
	
}
