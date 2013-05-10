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

package net.zeddev.zedlog.logger.impl.event;

import org.jnativehook.mouse.NativeMouseEvent;

/**
 * A mouse event describing a mouse movement.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseMovedEvent extends MouseEvent {

	public MouseMovedEvent() {
		super();
	}

	public MouseMovedEvent(final NativeMouseEvent event) {
		super(event);
	}

	@Override
	public String toString() {

		StringBuilder msg = new StringBuilder();

		msg.append("Mouse moved - at ");
		msg.append(posString(getX(), getY()));
		msg.append(".\n");

		return msg.toString();

	}

}
