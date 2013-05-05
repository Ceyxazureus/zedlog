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
 * A <code>DataLogger</code> for mouse click events.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseClickLogger extends AbstractDataLogger
		implements NativeMouseListener {

	/**
	 * Creates a new <code>MouseLogger</code>.
	 *
	 */
	public MouseClickLogger() {
		GlobalScreen.getInstance().addNativeMouseListener(this);
	}

	@Override
	public void shutdown() {
		GlobalScreen.getInstance().removeNativeMouseListener(this);
	}

	@Override
	public String type() {
		return "MouseClick";
	}

	@Override
	public void nativeMouseClicked(final NativeMouseEvent event) {

		MouseClickedEvent clickEvent =
			new MouseClickedEvent(event);

		LogEntry logEntry = new LogEntry(clickEvent.toString(), clickEvent);
		notifyDataLoggerObservers(this, logEntry);

	}

	@Override
	public void nativeMousePressed(NativeMouseEvent event) {
		// IGNORED
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent event) {
		// IGNORED
	}

	/**
	 * A mouse event describing a button click.
	 *
	 * @author Zachary Scott <zscott.dev@gmail.com>
	 */
	public static final class MouseClickedEvent extends MouseEvent {

		private int buttonCode;
		private String button;
		private int clickCount;

		protected MouseClickedEvent(final NativeMouseEvent event) {
			super(event);
			setButtonCode(event.getButton());
			setButton(buttonName(event.getButton()));
			setClickCount(event.getClickCount());
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

		public final int getClickCount() {
			return clickCount;
		}

		public final void setClickCount(int clickCount) {
			this.clickCount = clickCount;
		}

		@Override
		public String toString() {

			StringBuilder msg = new StringBuilder();

			msg.append("Mouse clicked - ");
			msg.append(getButton());
			msg.append(" at ");
			msg.append(posString(getX(), getY()));

			if (getClickCount() > 1) {
				msg.append(" ");
				msg.append(getClickCount());
				msg.append(" times.\n");
			} else {
				msg.append(".\n");
			}

			return msg.toString();

		}

	}

}

