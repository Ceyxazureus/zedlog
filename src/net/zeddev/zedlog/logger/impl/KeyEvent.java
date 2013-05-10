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

import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;
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

	private Type eventType = null;
	private int keyCode = -1;
	private char ch = (char) -1;

	public KeyEvent() {
	}

	public KeyEvent(Type eventType, int keyCode, char ch) {
		this.eventType = eventType;
		this.keyCode = keyCode;
		this.ch = ch;
	}

	public Type getEventType() {
		return eventType;
	}

	public void setEventType(Type eventType) {
		this.eventType = eventType;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public char getChar() {
		return ch;
	}

	public void setChar(char ch) {
		this.ch = ch;
	}

	@Override
	public void write(Writer output) throws Exception {

		assert(output != null);

		output.write(getEventType().toString());
		output.write("|");
		output.write(Integer.toString(getKeyCode()));
		output.write("|");
		output.write(Short.toString((short) getChar()));

	}

	@Override
	public void read(Scanner scanner) throws Exception {

		readEventType(scanner);
		setKeyCode(scanner.nextInt());
		setChar((char) scanner.nextShort());

	}

	private void readEventType(Scanner scanner) {

		String type = scanner.next();

		if (type.equalsIgnoreCase(Type.PRESSED.toString())) {
			setEventType(Type.PRESSED);
		} else if (type.equalsIgnoreCase(Type.RELEASED.toString())) {
			setEventType(Type.RELEASED);
		} else if (type.equalsIgnoreCase(Type.TYPED.toString())) {
			setEventType(Type.TYPED);
		} // XXX IGNORED if not known

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
