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

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	private String shortcutLink = null;
	
	// NOTE instantiated using builder below
	private Installer() {
	}
	
	/** Installs the given file. */
	public void install(File file) {
		// TODO
	}
	
	/** Installs all files and the shortcut. */
	public void installAll() {
		
		for (File file : installFiles())
			install(file);
		
		installShortcut();
				
	}
	
	/** Installs the binary shortcut. */
	public void installShortcut() {
		// TODO
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
		Installer instance = new Installer();
	
		// TODO set default values for directories below
		
		private File installDir = null;
		
		// shortcut details
		private String shortcutName = null;
		private File shortcutDir = null;
		
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
			instance.shortcutLink = src;
			
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
		
		/** Returns the built instance. 
		 * @throws InstallerException
		 */
		public Installer instance() throws InstallerException {
			
			// first check that instance has been built correctly
			
			if (installDir == null) {
				
				throw new InstallerException("Install directory not set!!");
				
			} else if (instance.files.size() > 0) {
				
				throw new InstallerException("Install files not set!");
				
			} else if (shortcutName == null ||
			           shortcutDir == null ||
			           instance.shortcutLink == null) {
				
				if (shortcutName != null) {
					
					throw new InstallerException(
						"Shortcut details not set for %s.", shortcutName
					);
					
				} else {
					throw new InstallerException("Shortcut details not set.");
				}
				
			}
			
			// create install directory, if does not exist
			if (!installDir.exists())
				installDir.mkdirs();
			
			// create shortcut directory, if does not exist
			if (!shortcutDir.exists())
				shortcutDir.mkdirs();
		
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
