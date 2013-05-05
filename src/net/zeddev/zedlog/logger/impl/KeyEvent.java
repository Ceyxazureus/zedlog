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

import net.zeddev.zedlog.logger.LogEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * A key pressed, released or typed event.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class KeyEvent extends LogEvent {

	/** The type of <code>KeyEvent</code>. */
	public static enum Type {

		PRESSED("pressed"), RELEASED("released"), TYPED("typed");

		private String type;

		private Type(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}

	}

	private final Type eventType;
	private final int keyCode;
	private final char ch;

	public KeyEvent(Type eventType, int keyCode, char ch) {
		this.eventType = eventType;
		this.keyCode = keyCode;
		this.ch = ch;
	}

	public final Type getEventType() {
		return eventType;
	}

	public final int getKeyCode() {
		return keyCode;
	}

	public final char getChar() {
		return ch;
	}

	@Override
	public String toString() {

		if (keyCode < 0) {
			return String.format("%s %s", Character.toString(ch), eventType.toString());
		} else {
			return String.format("%s %s", NativeKeyEvent.getKeyText(keyCode), eventType.toString());
		}

	}

}
