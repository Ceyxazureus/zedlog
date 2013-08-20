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

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.zeddev.zedlog.logger.LogEvent;
import net.zeddev.zedlog.logger.impl.event.KeyEvent;
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Unit test for {@link net.zeddev.zedlog.logger.impl.CompositeDataLogger}.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class CompositeDataLoggerTest {
	
	// the AWT robot used to simulate events
	private Robot robot;

	public CompositeDataLoggerTest() throws Throwable {
		this.robot = new Robot();
	}
	
	// simulates a typing a key
	private void typeChar(int keycode) {
		
		keyPress(keycode);
		keyRelease(keycode);
		
	}
	
	// simulates a key press
	private void keyPress(int keycode) {
		
		robot.keyPress(keycode);
		robot.delay(10);
		
	}
	
	// simulates a key release
	private void keyRelease(int keycode) {
		
		robot.keyRelease(keycode);
		robot.delay(10);
		
	}
	
	/** Tests the {@code CompositeDataLogger}s {@code toXML()} and {@code fromXML()} methods. */
	@Test
	public void testCompositeLoggerXML() throws Throwable {
		
		CompositeDataLogger inst = new CompositeDataLogger();
		inst.addLogger(DataLoggers.newDataLogger("CharTyped"));
		inst.addLogger(DataLoggers.newDataLogger("MouseClick"));
		
		File tmp = new File(System.getProperty("java.io.tmpdir"), 
			"CompositeDataLoggerTest.xml");
		
		inst.setLogFile(tmp);
		
		// generate some events to store
		typeChar(java.awt.event.KeyEvent.VK_A);
		
		inst.shutdown(); // shutdown & release log file
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document xmlLog = docBuilder.newDocument();
		
		CompositeDataLogger readLogger = new CompositeDataLogger();
		readLogger.openLogFile(tmp);
		
		// test reading of logger instances
		
		assertTrue(readLogger.containsLogger(
			DataLoggers.newDataLogger("CharTyped")
		));
		
		assertTrue(readLogger.containsLogger(
			DataLoggers.newDataLogger("MouseClick")
		));
		
	}
	
}