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

import java.io.Writer;
import java.util.Scanner;

import org.jnativehook.mouse.NativeMouseEvent;

import org.w3c.dom.*;
import static net.zeddev.zedlog.util.Assertions.*;

/**
 * A mouse event describing a button click.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseDraggedEvent extends MouseEvent {

	private int buttonCode = -1;

	public MouseDraggedEvent() {
	}

	public MouseDraggedEvent(final NativeMouseEvent event) {
		super(event);
		setButtonCode(event.getButton());
	}

	public final int getButtonCode() {
		return buttonCode;
	}

	public final void setButtonCode(int buttonCode) {
		this.buttonCode = buttonCode;
	}

	@Override
	public String type() {
		return "MouseDragged";
	}

	@Override
	public void toXML(Element parent) throws Exception {
		super.toXML(parent);
		
		requireNotNull(parent);
		requireEquals(parent.getTagName(), "event");
		
		parent.setAttribute("bcode", Integer.toString(getButtonCode()));

	}

	@Override
	public void fromXML(Element parent) throws Exception {
		super.fromXML(parent);
		
		requireNotNull(parent);
		requireEquals(parent.getTagName(), "entry");
		
		setButtonCode(Integer.parseInt(
			parent.getAttribute("bcode")
		));

	}
	
	@Override
	public void write(Writer output) throws Exception {

		assert(output != null);

		output.write(Integer.toString(getButtonCode()));
		output.write("|");

		super.write(output);

	}

	@Override
	public void read(Scanner scanner) throws Exception {

		setButtonCode(scanner.nextInt());

		super.read(scanner);

	}

	@Override
	public String toString() {

		StringBuilder msg = new StringBuilder();

		msg.append("Mouse dragged - at ");
		msg.append(posString(getX(), getY()));
		msg.append(".");

		return msg.toString();

	}

}