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
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import net.zeddev.zedlog.logger.AbstractDataLogger;
import net.zeddev.zedlog.logger.LogEntry;

/**
 * A <code>DataLogger</code> for key released events.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class KeyReleasedLogger extends AbstractDataLogger implements NativeKeyListener {

	/**
	 * Creates a new <code>KeyPressedLogger</code>.
	 *
	 */
	public KeyReleasedLogger() {
		super();
		GlobalScreen.getInstance().addNativeKeyListener(this);
	}

	@Override
	public String type() {
		return "KeyReleased";
	}

	@Override
	public void shutdown() {
		GlobalScreen.getInstance().removeNativeKeyListener(this);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent event) {
		// IGNORED
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event) {

		KeyEvent keyEvent = new KeyEvent(KeyEvent.Type.RELEASED, event.getKeyCode(), event.getKeyChar());
		String key = NativeKeyEvent.getKeyText(event.getKeyCode());

		LogEntry logEntry = new LogEntry(String.format("%s ", key), keyEvent);
		notifyDataLoggerObservers(this, logEntry);

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {
		// IGNORED
	}

}
