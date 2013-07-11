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
import javax.swing.JComponent;
import javax.swing.JLabel;

import net.zeddev.zedlog.Config;
import net.zeddev.zedlog.gui.Icons;

/**
 * <p>
 * A dialog which provides some basic information about ZedLog.
 * </p>
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class AboutDialog extends javax.swing.JDialog {

	/** Creates new form {@code AboutDialog}. */
	public AboutDialog(Frame parent, boolean modal) {

		super(parent, modal);
		
		initComponents();
		buildForm();

		setTitle("About " + Config.INSTANCE.NAME);
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
        
		// centre on screen
		setLocationRelativeTo(null);

	}
	
	// initialises the GUI components
	private void initComponents() {
		
		lblTitle.setText(Config.INSTANCE.FULL_NAME);
		
		lblIcon.setIcon(Icons.getInstance().getIcon("zedlog"));
		
		lblDesc.setText(Config.INSTANCE.DESCRIPTION);
		
		lblCopyright.setText(
			"Copyright (C) 2013  Zachary Scott <zscott.dev@gmail.com>"
		);
		
		btnOkay.setMnemonic('k');
		
		// add okay button listener
		btnOkay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                btnOkayActionPerformed(event);
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
	
	// builds the form 
	private void buildForm() {
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		Box outer = new Box(BoxLayout.LINE_AXIS);
		outer.add(Box.createHorizontalStrut(SPACING));
		
			Box inner = new Box(BoxLayout.PAGE_AXIS);
			
			inner.add(Box.createVerticalStrut(SPACING));
			addLine(inner, lblIcon);
			addLine(inner, lblTitle);
			addLine(inner, lblCopyright);
			addLine(inner, lblDesc);
			
			inner.add(Box.createVerticalStrut(SPACING));
			addLine(inner, btnOkay);
			
			outer.add(inner);
		
		outer.add(Box.createHorizontalStrut(SPACING));
		add(outer);
		
		pack();
		
	}

    private void btnOkayActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }
    
    // the spacing between components
    private final static int SPACING = 10;

    // form elements
    private JButton btnOkay = new JButton("Okay");
    private JLabel lblCopyright = new JLabel();
    private JLabel lblDesc = new JLabel();
    private JLabel lblIcon = new JLabel();
    private JLabel lblTitle = new JLabel();

}
