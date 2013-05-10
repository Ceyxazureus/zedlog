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
		return "MouseWheel";
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent event) {

		MouseWheelMovedEvent wheelEvent =
			new MouseWheelMovedEvent(event);

		LogEntry logEntry = new LogEntry(this, wheelEvent.toString(), wheelEvent);
		notifyDataLoggerObservers(this, logEntry);

	}

}
