package net.zeddev.zedlog.installer.ui;
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

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

import net.zeddev.zedlog.installer.Installer;
import net.zeddev.zedlog.installer.Installer.InstallerException;
import net.zeddev.zedlog.installer.InstallerUi;

/**
 * A simple console based installer user interface.
 * Intended to be used until a suitable GUI installer UI has been created.
 * Will also prove handy in console only environments (such as a server or 
 * a telnet/ssh connection).
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class ConsoleUi implements InstallerUi {

	// the input /output streams (for convienience only)
	private static final Scanner in = new Scanner(System.in);
	private static final PrintStream out = System.out;
	
	private final String name;
	private final String desc;
	private final String copyright;
	private final String licenseName;
	private final String license;
	
	public ConsoleUi(String name,
	                 String desc, 
	                 String copyright,
	                 String licenseName,
	                 String licenseLocation) {
		
		this.name = name;
		this.desc = desc;
		this.copyright = copyright;
		this.licenseName = licenseName;
		
		// TODO read in the license resource file
		this.license = "PLACEHOLDER LICENSE TEXT";
		
	}
	
	private static void print(String fmt, Object... args) {
		out.print(String.format(fmt, args));
	}
	
	private static void println(String fmt, Object... args) {
		out.println(String.format(fmt, args));
	}
	
	private static void println() {
		out.println();
	}
	
	// asks the user a question (i,.e. a prompt)
	private static String ask(String fmt, Object... args) {
		
		print(fmt, args);
		print(" ");
		
		String ans = in.nextLine();
		return ans.trim();
		
	}
	
	// asks the user a yes/no question
	private static boolean yesno(boolean def, String fmt, Object... args) {
	
		print(String.format(fmt, args));
		print("? ");
		print(def ? "(Y/n)" : "(y/N)");
		print(" ");
		
		String ans = in.nextLine().trim().toLowerCase();
		
		if (ans.equals("")) { // use default 
			return def;
		} else if (ans.matches("y|yes"))  {
			return true;
		} else if (ans.matches("n|no"))  {
			return false;
		} else {
			
			println("Answer must be yes or no (y/n for short).");
			
			// try again
			return yesno(def, fmt, args);
			
		}
		
	}
	
	@Override
	public void showSplash() {
		
		println();
		println(">>>>>  %s Installer  <<<<<", name);
		println();
		println(name);
		println(desc);
		println();
		println(copyright);
		println();
		
	}

	@Override
	public void acceptLicense() throws InstallerException {
		
		println(
			"%s is licensed under the terms and conditions of %s.",
			name, licenseName
		);
		
		boolean show = yesno(false, "Do you wish to view the full license");
		if (show) {
			println();
			println(license);
			// TODO use less utility when available
		}
		
		boolean accepts = yesno(
			false, 
			"Do you accept the license terms & conditions"
		);
		
		if (!accepts) {
			
			throw new InstallerException(
				"License terms MUST be accepted to install the software!"
			);
			
		}
		
		// NOTE all good, user accepted license terms
		
	}
	
	@Override
	public Installer.Builder buildInstaller() throws InstallerException {
		
		Installer.Builder installer = new Installer.Builder(name);
		
		println(
			"Installation directory is:\n %s", 
			installer.getInstallDir().getPath()
		);
		
		boolean changeInstallDir = yesno(
			false, "Do you want to change the installation directory"
		);
		
		// let user change installation directory
		if (changeInstallDir) {
			
			String newPath = ask("Enter new installation directory: \n");
			installer.setInstallDir(newPath);
			
		}
		
		println();
		
		println(
			"Directory to install shortcut is:\n %s", 
			installer.getShortcutDir().toPath()
		);
		
		boolean changeShortcutDir = yesno(
			false, "Do you want to change the shortcut directory"
		);
		
		// let user change shortcut installation directory
		if (changeShortcutDir) {
			
			String newPath = ask("Enter the new shortcut directory: \n");
			installer.setShortcutDir(newPath);
			
		}
		
		return installer;
		
	}

	@Override
	public void install(Installer installer) throws InstallerException {
		
		println();
		println(">>>>>  Installing Files  <<<<<");
		
		// install the files
		for (File file : installer.installFiles()) {
			
			println("Installing %s", file.getPath());
			
			installer.install(file);
			
		}
		
		println();
		println(">>>>>  Installing Shortcut  <<<<<");
		
		println("Installing %s", installer.shortcutFile());
		
	}

	@Override
	public void showFinished() {
		
		println();
		println(">>>>>  Install Successful  <<<<<");
		println("%s has been installed successfully!", name);
		println();
		
		// TODO add installation report
		
	}

}
