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

import java.io.IOException;

import net.zeddev.zedlog.installer.Installer;
import net.zeddev.zedlog.installer.Installer.InstallerException;
import net.zeddev.zedlog.installer.InstallerUi;
import net.zeddev.zedlog.installer.Logger;
import net.zeddev.zedlog.installer.ui.ConsoleUi;

/**
 * The Java based installer for ZedLog.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class InstallerMain {
	
	// the ZedLog configuration
	private static final Config CONFIG = Config.INSTANCE;
	
	// CONFIG Installation configuration.
	
	public static final String NAME = CONFIG.FULL_NAME;
	public static final String DESC = CONFIG.DESCRIPTION;
	
	public static final String COPYRIGHT = "Copyright (C) 2013, Zachary Scott";
	
	/** The resource path to the installation files. */
	public static final String INSTALL_RSRC = "/net/zeddev/zedlog/installrsrc/";
	
	public static final String LICENSE_NAME = "GNU GPL";
	public static final String LICENSE_PATH = INSTALL_RSRC + "COPYING_GPL.txt";
	
	public static final String SHORTCUT_LINK = "/path/to/link/to"; // FIXME
	
	/** Installer entry point. */
	public static void main(String[] args) {

		// initialise the installer
		InstallerUi ui = null;
		try {
			
			ui = new ConsoleUi(
				NAME,
				DESC,
				COPYRIGHT,
				LICENSE_NAME,
				LICENSE_PATH
			);
			
		} catch (IOException ex) {
			
			// NOTE should not happen if configured properly
			
			Logger.error("Failed to initialise the installer!");
			Logger.error(ex.getMessage());
			
			System.exit(1);
			
		}
		
		// warn user about installing without root privileges
		if (CONFIG.isUnix() || CONFIG.isOSX()) {
			
			if (CONFIG.userName() != "root") 
				Logger.warn("Installer must be run as root to install for all users!");
			
		}
		
		// run the installer
		try {
			
			ui.showSplash();
			
			ui.acceptLicense();
			
			Installer.Builder builder = ui.buildInstaller();
			builder.shortcut(CONFIG.NAME, SHORTCUT_LINK);
			
			ui.install(builder.instance());
			
			ui.showFinished();
			
		} catch (InstallerException ex) {
			Logger.error(ex.getMessage());
			Logger.error("Installation failed!");
		}
		
	}
	
}
