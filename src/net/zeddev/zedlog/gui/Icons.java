package net.zeddev.zedlog.gui;
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

import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.Config;
import static net.zeddev.zedlog.util.Assertions.*;

/**
 * Manages GUI icons.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public enum Icons {

	INSTANCE;

	/** The extention which all icons must have ({@code .png}). */
	public static final String EXT = ".png";

	private final Logger logger = Logger.getLogger(this);

	// the cached icons
	private Map<String, Icon> icons = new HashMap<>();

	public static Icons getInstance() {
		return INSTANCE;
	}

	// loads an icon from resource
	private Icon loadIcon(String name) {

		String resource = Config.ICON_DIR + "/" + name + EXT;
		logger.info("Loading icon '%s'.", null, resource);

		return new ImageIcon(getClass().getResource(resource));

	}

	/**
	 * Returns the icon instance for the given name.
	 *
	 * @param name The name of the icon (the name of the icon file without extention).
	 * @return The {@code Icon} instance.
	 */
	public Icon getIcon(final String name) {
		
		requireNotNull(name != null);
		requireNotEquals(name, "");

		if (!icons.containsKey(name))
			icons.put(name, loadIcon(name));

		return icons.get(name);

	}

}
