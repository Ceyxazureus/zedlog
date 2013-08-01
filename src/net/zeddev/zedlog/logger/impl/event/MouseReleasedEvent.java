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
 * A mouse event describing a button release.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseReleasedEvent extends MouseEvent {

	private int buttonCode = -1;
	private String button = null;

	public MouseReleasedEvent() {
	}

	public MouseReleasedEvent(final NativeMouseEvent event) {
		super(event);
		setButtonCode(event.getButton());
		setButton(buttonName(event.getButton()));
	}

	public final int getButtonCode() {
		return buttonCode;
	}

	public final void setButtonCode(int buttonCode) {
		this.buttonCode = buttonCode;
	}

	public final String getButton() {
		return button;
	}

	public final void setButton(String button) {
		this.button = button;
	}

	@Override
	public String type() {
		return "MouseReleased";
	}
	
	@Override
	public void toXML(Element parent) throws Exception {
		
		super.toXML(parent);
		
		requireNotNull(parent);
		requireEquals(parent.getTagName(), "event");
		
		parent.setAttribute("bcode", Integer.toString(getButtonCode()));
		parent.setAttribute("bname", getButton());

	}

	@Override
	public void fromXML(Element parent) throws Exception {
		super.fromXML(parent);
		
		requireNotNull(parent);
		requireEquals(parent.getTagName(), "entry");
		
		setButtonCode(Integer.parseInt(
			parent.getAttribute("bcode")
		));

		setButton(parent.getAttribute("bname"));
		
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
		setButton(buttonName(getButtonCode()));

		super.read(scanner);

	}

	@Override
	public String toString() {

		StringBuilder msg = new StringBuilder();

		msg.append("Mouse released - ");
		msg.append(getButton());
		msg.append(" at ");
		msg.append(posString(getX(), getY()));
		msg.append(".");

		return msg.toString();

	}

}
