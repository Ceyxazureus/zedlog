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
 * A <code>DataLogger</code> for mouse dragging events.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseDraggedLogger extends AbstractDataLogger
		implements NativeMouseMotionListener {

	final List<LogEntry> log = new ArrayList();
	final List<MouseDraggedEvent> mouseDrags = new ArrayList<>();

	/**
	 * Creates a new <code>MouseDraggedLogger</code>.
	 *
	 */
	public MouseDraggedLogger() {
		GlobalScreen.getInstance().addNativeMouseMotionListener(this);
	}

	@Override
	public void shutdown() {
		GlobalScreen.getInstance().removeNativeMouseMotionListener(this);
	}

	@Override
	public String type() {
		return "mouse drag";
	}

	@Override
	public List<LogEntry> logEntries() {
		return new ArrayList(log);
	}

	/**
	 * Returns the current list mouse dragged events.
	 *
	 * @return The current list mouse dragged events.
	 */
	public List<MouseDraggedEvent> getDraggedEvents() {
		return new ArrayList<>(mouseDrags);
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
		// IGNORED
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent event) {

		MouseDraggedEvent draggedEvent =
			new MouseDraggedEvent(event);

		String msg = draggedEvent.toString();

		// log the wheel moved event details
		mouseDrags.add(draggedEvent);

		log(msg);

	}

	/**
	 * A mouse event describing a button click.
	 *
	 * @author Zachary Scott <zscott.dev@gmail.com>
	 */
	public final class MouseDraggedEvent extends MouseEvent {

		protected MouseDraggedEvent(final NativeMouseEvent event) {
			super(event);
		}

		@Override
		public String toString() {

			StringBuilder msg = new StringBuilder();

			msg.append("Mouse dragged - at ");
			msg.append(posString(getX(), getY()));
			msg.append(".\n");

			return msg.toString();

		}

	}

}

