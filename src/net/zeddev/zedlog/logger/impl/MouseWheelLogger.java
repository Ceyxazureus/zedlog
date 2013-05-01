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

package net.zeddev.zedlog.logger.impl;

import java.util.ArrayList;
import java.util.List;
import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;
import net.zeddev.zedlog.logger.AbstractDataLogger;
import net.zeddev.zedlog.logger.LogEntry;

/**
 * A <code>DataLogger</code> for mouse wheel movement events.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseWheelLogger extends AbstractDataLogger
		implements NativeMouseWheelListener {

	/**
	 * Creates a new <code>MouseWheelLogger</code>.
	 *
	 */
	public MouseWheelLogger() {
		GlobalScreen.getInstance().addNativeMouseWheelListener(this);
	}

	@Override
	public void shutdown() {
		GlobalScreen.getInstance().removeNativeMouseWheelListener(this);
	}

	@Override
	public String type() {
		return "mouse wheel";
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent event) {

		MouseWheelMovedEvent wheelEvent =
			new MouseWheelMovedEvent(event);

		LogEntry logEntry = new LogEntry(wheelEvent.toString(), wheelEvent);
		notifyDataLoggerObservers(this, logEntry);

	}

	/**
	 * A mouse event describing a mouse wheel movement.
	 *
	 * @author Zachary Scott <zscott.dev@gmail.com>
	 */
	public final class MouseWheelMovedEvent extends MouseEvent {

		private int rotation;

		protected MouseWheelMovedEvent(final NativeMouseWheelEvent event) {
			super(event);
			setRotation(event.getWheelRotation());
		}

		public int getRotation() {
			return rotation;
		}

		public void setRotation(int rotation) {
			this.rotation = rotation;
		}

		@Override
		public String toString() {

			StringBuilder msg = new StringBuilder();

			msg.append("Mouse wheel moved - at ");
			msg.append(posString(getX(), getY()));

			msg.append(" ");
			msg.append(Math.abs(getRotation()));
			msg.append(" units");

			if (getRotation() > 0) {
				msg.append(" down");
			} else {
				msg.append(" up");
			}

			msg.append(".\n");

			return msg.toString();

		}

	}

}
