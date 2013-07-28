package net.zeddev.zedlog.logger;
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

import net.zeddev.zedlog.logger.impl.MouseClickLogger;
import net.zeddev.zedlog.logger.impl.MousePressedLogger;
import net.zeddev.zedlog.logger.impl.MouseReleasedLogger;
import net.zeddev.zedlog.logger.impl.MouseMovementLogger;
import net.zeddev.zedlog.logger.impl.MouseDraggedLogger;
import net.zeddev.zedlog.logger.impl.MouseWheelLogger;

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
		
		Robot robot = new Robot();
		
		// simulate character press
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		robot.delay(100); 
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
		
		Robot robot = new Robot();
		
		// simulate character press
		robot.mousePress(InputEvent.BUTTON1_MASK);
		
		robot.delay(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
		// release button after check for press
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
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
		
		// move the mouse about
		Robot robot = new Robot();
		robot.mouseMove(200, 200); // NOTE assumes mouse is not already at pos :-/
		
		robot.delay(100); 
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
		
		// simulate mouse drag
		Robot robot = new Robot();
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseMove(200, 200); // NOTE assumes mouse is not already at pos :-/
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		robot.delay(100); 
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
		
		// simulate mouse wheel movement
		Robot robot = new Robot();
		robot.mouseWheel(1);
		
		robot.delay(100); 
		// NOTE Must delay here as events are dispatched on a different thread
		
		assertEquals(observer.count, 1);
		
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
		
		// simulate character press
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		robot.delay(100); 
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
