package net.zeddev.zedlog;
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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.zeddev.zedlog.installer.InstallerUi;
import net.zeddev.zedlog.installer.Logger;

/**
 * ZedLog installer main class. 
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class ConsoleInstaller {
	
	private static void init() {
		
		Logger.info("Initialising ... ");
		
		// enable anti-aliases fonts
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
		
		// initialise the GUI look and feel
		try {

			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				
				if ("Nimbus".equals(info.getName())) {
					
					UIManager.setLookAndFeel(info.getClassName());
					Logger.info("Nimbus look-and-feel set.");
					
					break;
				}
				
			}

		} catch (ClassNotFoundException |
				 InstantiationException |
				 IllegalAccessException |
				 UnsupportedLookAndFeelException ex) {

			Logger.error("Unable to set GUI look and feel.", ex);

		}
		
	}
	
	private static void startUi() {
		
		/* TODO
		InstallerUi gui = ...;
		
		gui.showSplash();
		
		Installer installer = gui.getInstaller();
		gui.install(installer);
		
		gui.showFinished();
		*/
		
	}
	
	public static void main(String[] args) {
		
		init();
		//startUi();
		
	}
	
}
