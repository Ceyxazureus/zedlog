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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.zeddev.zedlog.logger.impl.DataLoggers;
import net.zeddev.zedlog.logger.impl.LogEvents;
import net.zeddev.zedlog.logger.impl.event.KeyEvent;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test for {@link net.zeddev.zedlog.logger.LogEntry}.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class LogEntryTest {

	/** Tests the {@code LogEntry} creation/instantiation. */
	@Test
	public void testLogEntry() throws Throwable {
		
		DataLogger dl = DataLoggers.newDataLogger("CharTyped");
		String msg = "Test LogEntry Message.";
		LogEvent event = LogEvents.newLogEvent("KeyEvent");
		
		long startTime = System.currentTimeMillis();
		LogEntry inst = new LogEntry(dl, msg, event);
		
		assertEquals(inst.getParent(), dl);
		assertEquals(inst.getMessage(), msg);
		assertEquals(inst.getEvent(), event);
		assertTrue(inst.getTimestamp() >= startTime); // should be AFTER instantiation
		
	}
	
	/** Tests the {@code LogEntry}s {@code toXML()} and {@code fromXML()} methods. */
	@Test
	public void testLogEntryXML() throws Throwable {
		
		DataLogger dl = DataLoggers.newDataLogger("CharTyped");
		String msg = "Test LogEntry Message.";
		LogEvent event = new KeyEvent(KeyEvent.Type.TYPED, 1, 'a');
		
		long startTime = System.currentTimeMillis();
		LogEntry inst = new LogEntry(dl, msg, event);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document xmlLog = docBuilder.newDocument();

		// NOTE dont need rest of document, just an <entries>
		Element entries = xmlLog.createElement("entries");

		inst.toXML(entries);
		
		// parse xml
		LogEntry readEntry = new LogEntry();
		readEntry.fromXML(
			(Element) entries.getElementsByTagName("entry").item(0)
		);
		
		// check read correctly
		assertEquals(readEntry, inst);
		
	}

}