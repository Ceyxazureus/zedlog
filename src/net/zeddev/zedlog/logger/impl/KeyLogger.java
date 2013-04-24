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
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import net.zeddev.zedlog.logger.AbstractDataLogger;
import net.zeddev.zedlog.logger.LogEntry;

/**
 * A <code>DataLogger</code> for keyboard events.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class KeyLogger extends AbstractDataLogger implements NativeKeyListener {

	final List<LogEntry> log = new ArrayList();

	/**
	 * Creates a new <code>KeyLogger</code>.
	 *
	 */
	public KeyLogger() {
		GlobalScreen.getInstance().addNativeKeyListener(this);
	}

	@Override
	public String type() {
		return "key logger";
	}

	@Override
	public List<LogEntry> logEntries() {
		return new ArrayList(log);
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
		// IGNORED
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {

		StringBuilder logMsg = new StringBuilder();

		char ch = event.getKeyChar();

		// add spacing if not alphanumeric char
		if (ch == '\n' || ch == '\r') {
			logMsg.append("[Return]");
		} else if (ch == '\t') {
			logMsg.append("[Tab]");
		} else if (ch == ' ') {
			logMsg.append("[Space]");
		} else if (Character.isLetterOrDigit(ch) || Character.isWhitespace(ch)) {
			logMsg.append(ch);
		} else {

			String key = NativeKeyEvent.getKeyText(event.getKeyChar());

			logMsg.append("[");
			logMsg.append(key);
			logMsg.append("]");

		}

		LogEntry logEntry = new LogEntry(logMsg.toString());

		// log the key press
		log.add(logEntry);

		// notify observers
		notifyDataLoggerObservers(this, logEntry);

	}

}