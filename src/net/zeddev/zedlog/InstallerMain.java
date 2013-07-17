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

import net.zeddev.zedlog.installer.Installer;
import net.zeddev.zedlog.installer.Installer.InstallerException;
import net.zeddev.zedlog.installer.InstallerUi;
import net.zeddev.zedlog.installer.Logger;
import net.zeddev.zedlog.installer.ui.ConsoleUi;

/**
 * Console based installer for ZedLog.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class InstallerMain {
	
	public static void main(String[] args) {

		Config CONFIG = Config.INSTANCE;
		
		InstallerUi ui = new ConsoleUi(
			CONFIG.FULL_NAME,
			CONFIG.DESCRIPTION,
			"Copyright (C) 2013, Zachary Scott",
			"GNU GPL",
			"/path/to/license.txt" // FIXME change to actual license text
		);
		
		// run the installer
		try {
			
			ui.showSplash();
			
			ui.acceptLicense();
			
			Installer.Builder builder = ui.buildInstaller();
			builder.shortcut(CONFIG.NAME, "/path/to/link/to"); // FIXME
			
			ui.install(builder.instance());
			
			ui.showFinished();
			
		} catch (InstallerException ex) {
			Logger.error(ex.getMessage());
			Logger.error("Installation failed!");
		}
		
	}
	
}
