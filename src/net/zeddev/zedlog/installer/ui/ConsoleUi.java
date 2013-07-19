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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Scanner;

import net.zeddev.zedlog.installer.Installer;
import net.zeddev.zedlog.installer.Installer.InstallerException;
import net.zeddev.zedlog.installer.InstallerUi;
import net.zeddev.zedlog.util.IOUtil;

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
	
	/**
	 * Creates a new {@code ConsoleUi} installer user interface.
	 * 
	 * @param name The name of the software package being installed (must not be {@code null}).
	 * @param desc A short one-line description of the software (must not be {@code null}).
	 * @param copyright A short one-line copyright statement (must not be {@code null}).
	 * @param licenseName The name of the copying license the software is released under (must not be {@code null}).
	 * @param licenseLocation The resource path (URL) to the full license terms & conditions (must not be {@code null}).  
	 * @throws IOException If failed to load license resource.
	 */
	public ConsoleUi(String name,
	                 String desc, 
	                 String copyright,
	                 String licenseName,
	                 String licenseLocation) throws IOException {
		
		assert(name != null && !name.equals(""));
		assert(desc != null && !desc.equals(""));
		assert(copyright != null && !copyright.equals(""));
		assert(licenseName != null && !licenseName.equals(""));
		assert(licenseLocation != null && !licenseLocation.equals(""));
		
		this.name = name;
		this.desc = desc;
		this.copyright = copyright;
		this.licenseName = licenseName;
		
		// read in the license resource file		
		this.license = readLicense(licenseLocation);
		
	}
	
	// reads the license from resource
	private String readLicense(String licenseLocation) throws IOException {
		
		StringWriter licenseText = new StringWriter();
		
		// get the license input stream
		InputStream licenseStream = getClass().getResourceAsStream(licenseLocation);
		if (licenseStream == null) {
			
			throw new IOException(String.format(
				"Cannot load license from %s", licenseLocation
			));
			
		}
		
		Reader licenseInput = new BufferedReader(
			new InputStreamReader(licenseStream)
		);
		
		// read in the license file
		IOUtil.readTo(licenseInput, licenseText);
		
		return licenseText.toString();
		
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
	
	// print without formatting
	private static void printraw(String str) {
		out.print(str);
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
			printraw(license);
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
		
		println("Installing %s", installer.shortcutFile().getPath());
		
		installer.installShortcut();
		
	}

	@Override
	public void showFinished() {
		
		println();
		println(">>>>>  Install Successful  <<<<<");
		println("%s has been installed successfully!", name);
		println();
		
	}

}
