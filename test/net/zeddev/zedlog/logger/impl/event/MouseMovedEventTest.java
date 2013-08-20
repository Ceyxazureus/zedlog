package net.zeddev.zedlog.logger.impl.event;
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
import net.zeddev.zedlog.logger.LogEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test for {@link net.zeddev.zedlog.logger.impl.event.MouseMovedEvent}.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class MouseMovedEventTest {
	
	/** Tests the {@code MouseMovedEvent} creation/instantiation. */
	@Test
	public void testMouseMovedEvent() throws Throwable {
		
		MouseMovedEvent inst = new MouseMovedEvent(
			new NativeMouseEvent(1, 2, 3, 4, 5, 6)
		);
		
		assertEquals(inst.getX(), 4);
		assertEquals(inst.getY(), 5);
		
	}
	
	/** Tests the {@code MouseMovedEvent}s {@code toXML()} and {@code fromXML()} methods. */
	@Test
	public void testMouseMovedEventXML() throws Throwable {
		
		LogEvent inst = new MouseMovedEvent(
			new NativeMouseEvent(1, 2, 3, 4, 5, 6)
		);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document xmlLog = docBuilder.newDocument();

		// NOTE dont need rest of document, just an <event>
		Element event = xmlLog.createElement("event");

		inst.toXML(event);
		
		// parse xml
		LogEvent readEvent = new MouseMovedEvent();
		readEvent.fromXML(event);
		
		// check read correctly
		assertEquals(readEvent, inst);
		
	}
	
}