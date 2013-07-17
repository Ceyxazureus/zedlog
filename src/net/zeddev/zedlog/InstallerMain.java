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
import java.util.regex.Pattern;

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
	
	public static final String SHORTCUT_LINK;
	
	/** The files to be installed. */
	public static final String[] INSTALL_FILES = {
		// TODO list files automatically from package name
		
		// JAR binaries
		INSTALL_RSRC + "zedlog-" + CONFIG.VERSION + ".jar",
		// libraries
		INSTALL_RSRC + "lib/JNativeHook.jar",
		INSTALL_RSRC + "lib/litelogger-v0.1beta.jar",
		
		// executable scripts
		INSTALL_RSRC + "zedlog.sh",
		INSTALL_RSRC + "zedlog.bat",
		INSTALL_RSRC + "zedlog.vbs",
		
		// documentation
		INSTALL_RSRC + "README.html",
		INSTALL_RSRC + "COPYING_GPL.html",
		INSTALL_RSRC + "CHANGES.html"
		
	};
	
	static {
		
		// set the shortcut link
		if (CONFIG.isWindows()) {
			SHORTCUT_LINK = INSTALL_RSRC + "zedlog.vbs"; // visual basic script
		} else { // assume unix/linux/macosx
			SHORTCUT_LINK = INSTALL_RSRC + "zedlog.sh"; // bash script
		}
		
	}
	
	// adds installation files to the installer
	private static void addFiles(Installer.Builder builder) {
		
		// add shortcut
		builder.shortcut(CONFIG.NAME, SHORTCUT_LINK);
		
		// add the installation files
		for (String path : INSTALL_FILES) {
			
			String basename = path.replaceFirst(INSTALL_RSRC, "");
			
			builder.addFile(basename, path);
			
		}
		
	}
	
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
			
			if (CONFIG.userName().equals("root")) 
				Logger.warn("Installer must be run as root to install for all users!");
			
		}
		
		// TODO warn about unsupported OS
		
		// run the installer
		try {
			
			ui.showSplash();
			
			ui.acceptLicense();
			
			Installer.Builder builder = ui.buildInstaller();
			
			// add the install files to the installer
			addFiles(builder);
			
			ui.install(builder.instance());
			
			ui.showFinished();
			
		} catch (InstallerException ex) {
			Logger.error(ex.getMessage());
			Logger.error("Installation failed!");
		}
		
	}
	
}
