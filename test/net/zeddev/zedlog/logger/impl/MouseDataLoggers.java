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
import java.awt.event.InputEvent;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for mouse {@link net.zeddev.zedlog.logger.DataLogger} s(i.e.
 * {@link net.zeddev.zedlog.logger.impl.MouseClickLogger},
 * {@link net.zeddev.zedlog.logger.impl.MousePressedLogger},
 * {@link net.zeddev.zedlog.logger.impl.MouseReleasedLogger},
 * {@link net.zeddev.zedlog.logger.impl.MouseMovementLogger},
 * {@link net.zeddev.zedlog.logger.impl.MouseDraggedLogger} and
 * {@link net.zeddev.zedlog.logger.impl.MouseWheelLogger}).
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseDataLoggers {
	
	// the AWT robot used to simulate mouse events
	private final Robot robot;
	
	public MouseDataLoggers() throws Exception {
		robot = new Robot();
	}
	
	// simulates a mouse button click
	private void mouseClick(int button) {
		
		mousePress(button);
		mouseRelease(button);
		
	}
	
	// simulates a mouse button press
	private void mousePress(int button) {
		
		robot.mousePress(button);
		robot.delay(10);
		
	}
	
	// simulates a mouse button press
	private void mouseRelease(int button) {
		
		robot.mouseRelease(button);
		robot.delay(10);
		
	}
	
	// simulates a mouse button press
	private void mouseMove(int x, int y) {
		
		robot.mouseMove(x, y);
		robot.delay(10);
		
	}
	
	// simulates a mouse wheel movement
	private void mouseWheel(int rot) {
		
		robot.mouseWheel(rot);
		robot.delay(10);
		
	}
	
	// simulates a mouse drag event
	private void mouseDrag(int button, int x, int y) {
	
		robot.mousePress(button);
		robot.mouseMove(x, y); 
		robot.mouseRelease(button);
		robot.delay(10);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.MouseClickLogger}. */
	@Test
	public void testMouseClick() throws Throwable {
		
		final DataLogger thelogger = new MouseClickLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger) {
			public void notifyLog(DataLogger logger, LogEntry logEntry) {
				super.notifyLog(logger, logEntry);
				
				assertEquals(logEntry.getEvent().type(), "MouseClicked");
				
			}
		};
		
		thelogger.addObserver(observer);
		
		mouseClick(InputEvent.BUTTON1_MASK);
		
		Thread.sleep(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.MousePressedLogger}. */
	@Test
	public void testMousePress() throws Throwable {
		
		final DataLogger thelogger = new MousePressedLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger) {
			public void notifyLog(DataLogger logger, LogEntry logEntry) {
				super.notifyLog(logger, logEntry);
				
				assertEquals(logEntry.getEvent().type(), "MousePressed");
				
			}
		};
		
		thelogger.addObserver(observer);
		
		mousePress(InputEvent.BUTTON1_MASK);
		
		Thread.sleep(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
		// release button after check for press
		mouseRelease(InputEvent.BUTTON1_MASK);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.MouseClickLogger}. */
	@Test
	public void testMouseRelease() throws Throwable {
		
		final DataLogger thelogger = new MouseReleasedLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger) {
			public void notifyLog(DataLogger logger, LogEntry logEntry) {
				super.notifyLog(logger, logEntry);
				
				assertEquals(logEntry.getEvent().type(), "MouseReleased");
				
			}
		};
		
		thelogger.addObserver(observer);
		
		Robot robot = new Robot();
		
		mouseClick(InputEvent.BUTTON1_MASK);
		
		Thread.sleep(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.MouseMovementLogger}. */
	@Test
	public void testMouseMovement() throws Throwable {
		
		final DataLogger thelogger = new MouseMovementLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger) {
			public void notifyLog(DataLogger logger, LogEntry logEntry) {
				super.notifyLog(logger, logEntry);
				
				assertEquals(logEntry.getEvent().type(), "MouseMoved");
				
			}
		};
		
		thelogger.addObserver(observer);
		
		mouseMove(200, 200); // NOTE assumes mouse is not already at pos :-/
		
		Thread.sleep(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.MouseDraggedLogger}. */
	@Test
	public void testMouseDragged() throws Throwable {
		
		final DataLogger thelogger = new MouseDraggedLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger) {
			public void notifyLog(DataLogger logger, LogEntry logEntry) {
				super.notifyLog(logger, logEntry);
				
				assertEquals(logEntry.getEvent().type(), "MouseDragged");
				
			}
		};
		
		thelogger.addObserver(observer);
		
		mouseDrag(InputEvent.BUTTON1_MASK, 200, 200); // NOTE assumes mouse is not already at pos :-/
		
		Thread.sleep(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
	}
	
	/** Tests the {@link net.zeddev.zedlog.logger.impl.MouseWheelLogger}. */
	@Test
	public void testMouseWheelMove() throws Throwable {
		
		final DataLogger thelogger = new MouseWheelLogger();

		// the observer which receives events
		TestObserver observer = new TestObserver(thelogger) {
			public void notifyLog(DataLogger logger, LogEntry logEntry) {
				super.notifyLog(logger, logEntry);
				
				assertEquals(logEntry.getEvent().type(), "MouseWheelMoved");
				
			}
		};
		
		thelogger.addObserver(observer);
		
		mouseWheel(1);
		
		Thread.sleep(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
	}
	
	// abstract base logger observer for test cases
	private abstract class TestObserver implements DataLoggerObserver {
		
		public int count = 0;
		
		private DataLogger thelogger;
		
		public TestObserver(DataLogger thelogger) {
			
			assertFalse(thelogger == null);
			
			this.thelogger = thelogger;
			
		}
		
		@Override
		public void notifyLog(DataLogger logger, LogEntry logEntry) {
			
			assertTrue(logger == thelogger);
			assertEquals(logEntry.getParent(), logger);
			
			count++;
			
		}
		
	}
	
}
