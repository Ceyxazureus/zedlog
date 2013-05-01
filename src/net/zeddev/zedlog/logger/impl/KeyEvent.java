
package net.zeddev.zedlog.logger.impl;

import net.zeddev.zedlog.logger.LogEvent;

/**
 * A key pressed, released or typed event.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class KeyEvent extends LogEvent {

	/** The type of <code>KeyEvent</code>. */
	public static enum Type {
		PRESSED, RELEASED, TYPED
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

}
