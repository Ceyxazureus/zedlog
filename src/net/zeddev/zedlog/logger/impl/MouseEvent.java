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

package net.zeddev.zedlog.logger.impl;

import net.zeddev.zedlog.logger.LogEvent;
import org.jnativehook.mouse.NativeMouseEvent;

/**
 * A generic mouse event.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class MouseEvent extends LogEvent {

   private int x, y;
   private long timestamp = System.currentTimeMillis();

   protected MouseEvent(final NativeMouseEvent event) {
	   setX(event.getX());
	   setY(event.getY());
   }

   public final int getX() {
	   return x;
   }

   public final void setX(int x) {
	   this.x = x;
   }

   public final int getY() {
	   return y;
   }

   public final void setY(int y) {
	   this.y = y;
   }

   public final long timeOccured() {
	   return timestamp;
   }

	/**
	 * Returns the name of the given mouse button.
	 *
	 * @param button The button code.
	 */
	protected String buttonName(int button) {

		switch (button) {

			case NativeMouseEvent.NOBUTTON:
				return "no button";

			case NativeMouseEvent.BUTTON1:
				return "left";

			case NativeMouseEvent.BUTTON2:
				return "right";

			case NativeMouseEvent.BUTTON3:
				return "middle";

			case NativeMouseEvent.BUTTON4:
				return "button 4";

			case NativeMouseEvent.BUTTON5:
				return "button 5";

			default:
				return String.valueOf(button);

		}

	}

	/**
	 * Returns a string contained the given position.
	 *
	 * @param x
	 * @param y
	 * @return A string contained the given position.
	 */
	protected String posString(int x, int y) {

		StringBuilder pos = new StringBuilder();

		pos.append("(");
		pos.append(x);
		pos.append(", ");
		pos.append(y);
		pos.append(")");

		return pos.toString();

	}

}
