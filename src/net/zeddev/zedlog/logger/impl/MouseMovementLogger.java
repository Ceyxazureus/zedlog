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
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;
import net.zeddev.zedlog.logger.AbstractDataLogger;
import net.zeddev.zedlog.logger.LogEntry;

/**
 * A <code>DataLogger</code> for mouse movements events.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseMovementLogger extends AbstractDataLogger
		implements NativeMouseMotionListener {

	final List<LogEntry> log = new ArrayList();
	final List<MouseMovedEvent> mouseMoves = new ArrayList<>();

	/**
	 * Creates a new <code>MouseMovementLogger</code>.
	 *
	 */
	public MouseMovementLogger() {
		GlobalScreen.getInstance().addNativeMouseMotionListener(this);
	}

	@Override
	public void shutdown() {
		GlobalScreen.getInstance().removeNativeMouseMotionListener(this);
	}

	@Override
	public String type() {
		return "mouse movement";
	}


	/**
	 * Returns the current list mouse movement events.
	 *
	 * @return The current list mouse movement events.
	 */
	public List<MouseMovedEvent> getMovedEvents() {
		return new ArrayList<>(mouseMoves);
	}

	private void log(String msg) {

		LogEntry logEntry = new LogEntry(msg.toString());

		// log the event
		log.add(logEntry);

		// notiy observers of event
		notifyDataLoggerObservers(this, logEntry);

	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent event) {

		MouseMovedEvent moveEvent = new MouseMovedEvent(event);
		String msg = moveEvent.toString();

		// log the mouse moved event details
		mouseMoves.add(moveEvent);

		log(msg);

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent nme) {
		// IGNORED
	}

	/**
	 * A mouse event describing a mouse movement.
	 *
	 * @author Zachary Scott <zscott.dev@gmail.com>
	 */
	public final class MouseMovedEvent extends MouseEvent {

		protected MouseMovedEvent(final NativeMouseEvent event) {
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

}

