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

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import net.zeddev.zedlog.logger.AbstractDataLogger;
import net.zeddev.zedlog.logger.LogEntry;

/**
 * A <code>DataLogger</code> for mouse pressed events.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MousePressedLogger extends AbstractDataLogger
		implements NativeMouseListener {

	/**
	 * Creates a new <code>MouseLogger</code>.
	 *
	 */
	public MousePressedLogger() {
		GlobalScreen.getInstance().addNativeMouseListener(this);
	}

	@Override
	public void shutdown() {
		GlobalScreen.getInstance().removeNativeMouseListener(this);
	}

	@Override
	public String type() {
		return "MousePressed";
	}

	@Override
	public void nativeMouseClicked(final NativeMouseEvent event) {
		// IGNORED
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent event) {

		MousePressedLogger.MousePressedEvent clickEvent =
			new MousePressedLogger.MousePressedEvent(event);

		LogEntry logEntry = new LogEntry(this, clickEvent.toString(), clickEvent);
		notifyDataLoggerObservers(this, logEntry);

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent event) {
		// IGNORED
	}

	/**
	 * A mouse event describing a button press.
	 *
	 * @author Zachary Scott <zscott.dev@gmail.com>
	 */
	public static final class MousePressedEvent extends MouseEvent {

		private int buttonCode;
		private String button;

		protected MousePressedEvent(final NativeMouseEvent event) {
			super(event);
			setButtonCode(event.getButton());
			setButton(buttonName(event.getButton()));
		}

		public final int getButtonCode() {
			return buttonCode;
		}

		public final void setButtonCode(int buttonCode) {
			this.buttonCode = buttonCode;
		}

		public final String getButton() {
			return button;
		}

		public final void setButton(String button) {
			this.button = button;
		}

		@Override
		public String toString() {

			StringBuilder msg = new StringBuilder();

			msg.append("Mouse pressed - ");
			msg.append(getButton());
			msg.append(" at ");
			msg.append(posString(getX(), getY()));
			msg.append(".\n");

			return msg.toString();

		}

	}

}
