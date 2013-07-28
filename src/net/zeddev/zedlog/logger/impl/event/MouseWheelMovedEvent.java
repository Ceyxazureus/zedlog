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

import org.jnativehook.mouse.NativeMouseWheelEvent;

import org.w3c.dom.*;
import static net.zeddev.zedlog.util.Assertions.*;

/**
 * A mouse event describing a mouse wheel movement.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class MouseWheelMovedEvent extends MouseEvent {

	private int rotation = -1;

	public MouseWheelMovedEvent() {
	}

	public MouseWheelMovedEvent(final NativeMouseWheelEvent event) {
		super(event);
		setRotation(event.getWheelRotation());
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	@Override
	public String type() {
		return "MouseWheelMoved";
	}
	
	@Override
	public void toXML(Element parent) throws Exception {
		
		requireNotNull(parent);
		
		// TODO implement me

	}

	@Override
	public void fromXML(Element parent) throws Exception {
		
		requireNotNull(parent);
		
		// TODO implement me

	}

	@Override
	public void write(Writer output) throws Exception {

		assert(output != null);

		output.write(Integer.toString(getRotation()));
		output.write("|");

		super.write(output);

	}

	@Override
	public void read(Scanner scanner) throws Exception {

		setRotation(scanner.nextInt());

		super.read(scanner);

	}

	@Override
	public String toString() {

		StringBuilder msg = new StringBuilder();

		msg.append("Mouse wheel moved - at ");
		msg.append(posString(getX(), getY()));

		msg.append(" ");
		msg.append(Math.abs(getRotation()));
		msg.append(" units");

		if (getRotation() > 0) {
			msg.append(" down");
		} else {
			msg.append(" up");
		}

		msg.append(".\n");

		return msg.toString();

	}

}
