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

import net.zeddev.zedlog.installer.Installer.InstallerException;

/**
 * A common interface for an installer front end.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public interface InstallerUi {

	/** Displays the initial 'splash' screen. */
	public void showSplash();
	
	/** Ensures that the users agrees to the licensing terms and conditions. */
	public boolean acceptLicense() throws InstallerException;
	
	/** Prompts user for installation data. */
	public Installer getInstaller() throws InstallerException;
	
	/** Runs the given installer with a graphical view. */
	public void install(Installer installer) throws InstallerException;
	
	/** Shows end screen. */
	public void showFinished();
	
}
