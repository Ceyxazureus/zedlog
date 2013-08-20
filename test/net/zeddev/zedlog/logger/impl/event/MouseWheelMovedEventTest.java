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
import org.jnativehook.mouse.NativeMouseWheelEvent;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test for {@link net.zeddev.zedlog.logger.impl.event.MouseWheelMovedEvent}.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class MouseWheelMovedEventTest {
	
	/** Tests the {@code MouseWheelMovedEvent} creation/instantiation. */
	@Test
	public void testMouseWheelMovedEvent() throws Throwable {
		
		MouseWheelMovedEvent inst = new MouseWheelMovedEvent(
			new NativeMouseWheelEvent(1, 2, 3, 4, 5, 6, 7, 8, 9)
		);	
		
		assertEquals(inst.getX(), 4);
		assertEquals(inst.getY(), 5);
		assertEquals(inst.getRotation(), 9);
	}
	
	/** Tests the {@code MousePressedEvent}s {@code toXML()} and {@code fromXML()} methods. */
	@Test
	public void testMouseWheelMovedEventXML() throws Throwable {
		
		LogEvent inst = new MouseWheelMovedEvent(
			new NativeMouseWheelEvent(1, 2, 3, 4, 5, 6, 7, 8, 9)
		);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document xmlLog = docBuilder.newDocument();

		// NOTE dont need rest of document, just an <event>
		Element event = xmlLog.createElement("event");

		inst.toXML(event);
		
		// parse xml
		LogEvent readEvent = new MouseWheelMovedEvent();
		readEvent.fromXML(event);
		
		// check read correctly
		assertEquals(readEvent, inst);
		
	}
	
}