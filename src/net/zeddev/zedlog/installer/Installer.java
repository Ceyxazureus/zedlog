package net.zeddev.zedlog.installer;
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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.zeddev.zedlog.Config;
import net.zeddev.zedlog.util.IOUtil;

/**
 * An installer.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class Installer {
	
	// the files to be installed
	private Map<File, String> files = new HashMap<File, String>();
	//          Dest, Source
	
	// binary shortcut details
	private File shortcutFile = null;
	private File shortcutLink = null;
	
	// NOTE instantiated using builder below
	private Installer() {		
	}
	
	// handles executable file types
	private void handleExecutable(File file) {
		
		//if (file.getPath().matches("\\.(sh|bat|vbs)$"))
			file.setExecutable(true);
		
	}
	
	/** 
	 * Installs the given file.
	 * @throws InstallerException If an error occurs during installation of the file.
	 */
	public void install(File file) throws InstallerException {
		
		assert(file != null);
		assert(files.containsKey(file));
		
		String path = files.get(file);
		
		try {
		
			// create the parent directory (if does not already exist)
			File parent = new File(file.getParent());
			parent.mkdirs();
			
			// the output file stream
			OutputStream outFile = new BufferedOutputStream(
				new FileOutputStream(file)
			);

			// write the file
			IOUtil.readTo(
				getClass().getResourceAsStream(path),
				outFile
			);
			
			outFile.close();
		
		} catch (IOException ex) {
			
			throw new InstallerException(
				"Failed to install %s; %s",
				file.getPath(), ex.getMessage()
			);
			
		}
		
		handleExecutable(file);
		
	}
	
	/** 
	 * Installs all files and the shortcut. 
	 * @throws InstallerException If an error occurs during installation of the files.
	 */
	public void installAll() throws InstallerException {
		
		for (File file : installFiles())
			install(file);
		
		installShortcut();
				
	}
	
	// creates a vbs script shortcut (Windows)
	private String vbsShortcut() {
		
		StringBuilder script = new StringBuilder();
		
		File link = new File(shortcutLink.getAbsolutePath());
		
		script.append("Set shell = CreateObject(\"Wscript.Shell\") \n");
		
		// move to scripts directory
		script.append("shell.CurrentDirectory = \"");
		script.append(link.getParent());
		script.append("\" \n");
		
		// execute the script
		script.append("shell.Run \"");
		script.append(link.getName());
		script.append("\", 0, true \n");
		script.append(" \n");
		
		return script.toString();
		
	}
	
	// creates a bash script shortcut (Unix/Linux/Mac OSX)
	private String shShortcut() {
		
		StringBuilder script = new StringBuilder();
		
		script.append("#!/bin/bash \n");
		
		script.append("cd '");
		script.append(shortcutLink.getParent());
		script.append("'");
		script.append(" \n");
		
		script.append("'");
		script.append(shortcutLink.getAbsolutePath());
		script.append("'");
		script.append(" \n");
		
		return script.toString();
		
	}
	
	/** Installs the binary shortcut. */
	public void installShortcut() throws InstallerException {
		
		String script = "";
		
		if (Config.INSTANCE.isWindows()) {
			script = vbsShortcut();
		} else { // assume unix/linux/macosx etc.
			script = shShortcut();
		}
		
		// create the parent directory (if does not already exist)
		File parent = new File(shortcutFile.getParent());
		parent.mkdirs();
		
		// write the shortcut script to the shortcut file
		try {
			
			shortcutFile.createNewFile();
			
			Writer writer = new BufferedWriter(
				new FileWriter(shortcutFile)
			);
			
			writer.write(script);
		
			writer.close();
			
		} catch (IOException ex) {
			
			throw new InstallerException(
				"Failed to create shortcut %s; %s", 
				shortcutFile.getPath(), ex.getMessage()
			);
			
		}
		
		shortcutFile.setExecutable(true);
		
	}
	
	/** The files to be installed. */
	public Set<File> installFiles() {
		return files.keySet();
	}
	
	/** The shortcut file to be installed. */
	public File shortcutFile() {
		return shortcutFile;
	}
	
	/** Builds a installer instance for the current environment. */
	public static class Builder {
	
		// the installer being constructed
		private final Installer instance = new Installer();
	
		private File installDir = null;
		
		// shortcut details
		private String shortcutName = null;
		private File shortcutDir = null;
		
		/** Builder installer using the default directories. */
		public Builder(String name) {
			
			assert(name != null && !name.equals(""));
			name = name.trim();
			
			Config CONFIG = Config.INSTANCE;
			
			// set the default directories
			if (CONFIG.isUnix() || CONFIG.isOSX()) {
				
				if (CONFIG.userName().equals("root")) { // install for all users
					
					installDir = new File(
						"/usr/local", name
					);
					
					shortcutDir = new File("/usr/bin");
					
				} else { // install to users home dir
				
					installDir = new File(
						System.getProperty("user.home"), 
						String.format(".%s", name) // make hidden dir on unix
					);
					
					shortcutDir = new File(
						System.getProperty("user.home"), "Desktop" // works on most Linux X systems
					);
					
				}
				
			} else if (CONFIG.isWindows()) {
				
				installDir = new File(
					new File(System.getProperty("user.home"), "Apps"), name
					// NOTE Cannot use ProgamFiles as requires admin privileges
					//      which cannot be gained from Java (without JNI or something)
				);
				
				shortcutDir = new File(
					System.getProperty("user.home"), "Desktop"
				);
				
			} else {
				
				Logger.error("Unsupported operating system %s!", CONFIG.OS);
				
			}
			
		}
		
		/** 
		 * Add a file to install. 
		 * 
		 * @param dest The destination file (must not be {@code null}).
		 * @param path The source path (resource URL).  Must not be {@code null}.
		 */
		public void addFile(String dest, String src) {
			
			assert(dest != null);
			assert(src != null);
			
			instance.files.put(
				new File(installDir, dest), src
			);
			
		}
		
		/** Sets the installation directory. */
		public void setInstallDir(String path) {
			
			assert(path != null && !path.equals(""));
			
			installDir = new File(path);
			
		}
		
		/** The installation directory. */
		public File getInstallDir() {
			return installDir;
		}
		
		/** Sets the shortcut details. */
		public void shortcut(String name, String src) {
			
			assert(name != null && !name.equals(""));
			assert(src != null && !src.equals(""));
			
			shortcutName = name;
			instance.shortcutLink = new File(installDir, src);
			
		}
		
		/** Sets the shortcut installation directory. */
		public void setShortcutDir(String path) {
			
			assert(path != null && !path.equals(""));
			
			shortcutDir = new File(path);
			
		}
		
		/** The shortcut installation directory. */
		public File getShortcutDir() {
			return shortcutDir;
		}
		
		// checks if valid directory
		private void checkDir(File dir) throws InstallerException {
			
			if (dir.exists() && !dir.isDirectory()) {
				
				throw new InstallerException(
					"%s is not a directory!",
					dir.getPath()
				);
				
			}
			
		}
		
		// makes the given directory tree
		private void mkdirs(File dir) throws InstallerException {
			
			if (!dir.exists()) {
				
				dir.mkdirs();
				
				if (!dir.isDirectory()) {
					
					throw new InstallerException(
						"Could not create directory; %s.",
						dir.getPath()
					);
					
				} else {
					
					Logger.info(
						"Created %s directory.",
						dir.getPath()
					);
					
				}
				
			}
			
		}
		
		/** 
		 * Returns the built installer instance.
		 *  
		 * @return the built installer instance (will NOT be {@code null}).
		 * @throws InstallerException If an error occurred when building the installer.
		 */
		public Installer instance() throws InstallerException {
			
			checkDir(installDir);
			mkdirs(installDir);
			
			checkDir(shortcutDir);
			mkdirs(shortcutDir);
		
			// get the shortcut name with the appropriate extension (based on OS)
			String nameWithExt = shortcutName;
			if (Config.INSTANCE.isWindows()) {
				nameWithExt += ".vbs";
			} // NOTE no extension on unix, will be executable
			
			File shortcut = new File(shortcutDir, nameWithExt);
			
			// check the shortcut is valid
			if (shortcut.exists()) {
				
				if (shortcut.isFile()) {
					
					Logger.info(
						"NOTE: Shortcut file %s already exists!", 
						shortcut.getPath()
					);
					
				} else {
					
					throw new InstallerException(
						"Shortcut file %s already exists and is not a normal file!",
						shortcut.getPath()
					);
					
				}
				
			}
			
			// set shortcut file as it is valid
			instance.shortcutFile = shortcut;
			
			return instance;
			
		}
		
	}
	
	/** An exception thrown within a {@code Installer}. */
	@SuppressWarnings("serial")
	public static class InstallerException extends Exception {
		
		public InstallerException(String fmt, Object... args) {
			super(String.format(fmt, args));
			
		}
		
	}
	
}
