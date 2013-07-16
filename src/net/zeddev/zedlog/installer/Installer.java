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
import java.util.ArrayList;
import java.util.List;

/**
 * An installer.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class Installer {

	private File installDir = null;
	
	// binary shortcut details
	private File shortcutFile = null;
	private String shortcutLink = null;
	
	private List<String> files = new ArrayList<String>();
	
	// NOTE instantiated using builder below
	private Installer() {
	}
	
	/** Installs the given file. */
	public void install(String url) {
		// TODO
	}
	
	/** Installs all files and the shortcut. */
	public void installAll() {
		
		for (String url : installFiles())
			install(url);
		
		installShortcut();
				
	}
	
	/** Installs the binary shortcut. */
	public void installShortcut() {
		// TODO
	}
	
	/** The files to be installed. */
	public List<String> installFiles() {
		return new ArrayList<String>(files);
	}
	
	/** Builds a installer instance for the current environment. */
	public static class InstallerBuilder {
	
		// the installer being constructed
		Installer instance = new Installer();
	
		// shortcut details
		private String shortcutName = null;
		private File shortcutDir = null;
		
		/** Add install file. */
		public void addFile(String file) {
			
			assert(file != null);
			
			instance.files.add(file);
			
		}
		
		/** Sets the shortcut details. */
		public void shortcut(String name, File dir, String link) {
			
			assert(name != null && !name.equals(""));
			assert(dir != null && dir.exists() && dir.isDirectory());
			assert(link != null && !link.equals(""));
			
			shortcutName = name;
			shortcutDir = dir;
			instance.shortcutLink = link;
			
		}
		
		/** Returns the built instance. 
		 * @throws InstallerException
		 */
		public void instance() throws InstallerException {
			
			// first check that instance has been built correctly
			
			if (instance.installDir == null) {
				
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
			if (!instance.installDir.exists())
				instance.installDir.mkdirs();
			
			// create shortcut directory, if does not exist
			if (!shortcutDir.exists())
				shortcutDir.mkdirs();
		
		}
		
	}
	
	/** An exception thrown within a {code Installer}. */
	@SuppressWarnings("serial")
	public static class InstallerException extends Exception {
		
		public InstallerException(String fmt, Object... args) {
			super(String.format(fmt, args));
			
		}
		
	}
	
}
