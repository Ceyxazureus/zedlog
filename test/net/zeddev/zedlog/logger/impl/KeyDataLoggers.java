package net.zeddev.zedlog.logger.impl;
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

import java.awt.Robot;
import java.awt.event.KeyEvent;

import net.zeddev.zedlog.logger.DataLogger;
import net.zeddev.zedlog.logger.DataLoggerObserver;
import net.zeddev.zedlog.logger.LogEntry;

import org.junit.Test;
import static org.junit.Assert.*;

import org.w3c.dom.*;

/**
 * Unit test for key {@link net.zeddev.zedlog.logger.DataLogger} (i.e.
 * {@link net.zeddev.zedlog.logger.impl.CharTypedLogger},
 * {@link net.zeddev.zedlog.logger.impl.KeyPressedLogger} and 
 * {@link net.zeddev.zedlog.logger.impl.KeyReleasedLogger}).
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class KeyDataLoggers {
	
	// the AWT robot used to simulate events
	private Robot robot;
	
	public KeyDataLoggers() throws Exception {
	
		 robot = new Robot();
		
	}
	
	// simulates a typing a key
	private void typeChar(int keycode) {
		
		keyPress(keycode);
		keyRelease(keycode);
		
	}
	
	// simulates a key press
	private void keyPress(int keycode) {
		
		robot.keyPress(KeyEvent.VK_A);
		robot.delay(10);
		
	}
	
	// simulates a key release
	private void keyRelease(int keycode) {
		
		robot.keyRelease(KeyEvent.VK_A);
		robot.delay(10);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.CharTypedLogger}. */
	@Test
	public void testCharTyped() throws Throwable {
		
		final DataLogger thelogger = new CharTypedLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger);
		
		thelogger.addObserver(observer);
		
		typeChar(KeyEvent.VK_A);
		
		Thread.sleep(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.KeyPressedLogger}. */
	@Test
	public void testKeyPressed() throws Throwable {
		
		final DataLogger thelogger = new KeyPressedLogger();
		
		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger);
		
		thelogger.addObserver(observer);
		
		keyPress(KeyEvent.VK_A);
		
		Thread.sleep(100); 
			// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
		keyRelease(KeyEvent.VK_A);
			// release once checked.
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.KeyPressedLogger}. */
	@Test
	public void testKeyReleased() throws Throwable {
		
		final DataLogger thelogger = new KeyReleasedLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger);
		
		thelogger.addObserver(observer);
		
		// simulate character press
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		
		Thread.sleep(100); 
			// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
	}
	
	// abstract base logger observer for test cases
	private class TestObserver implements DataLoggerObserver {
		
		public int count = 0;
		
		private DataLogger thelogger;
		
		public TestObserver(DataLogger thelogger) {
			
			assertFalse(thelogger == null);
			
			this.thelogger = thelogger;
			
		}
		
		@Override
		public void notifyLog(DataLogger logger, LogEntry logEntry) {
			
			assertTrue(logger == thelogger);
			
			String msg = logEntry.getMessage().toLowerCase();
			
			// check logEntry 
			assertTrue(msg.matches("\\s*a\\s*"));
			assertEquals(logEntry.getEvent().type(), "KeyEvent");
			assertEquals(logEntry.getParent(), logger);
			
			count++;
			
		}
		
	}
	
}
