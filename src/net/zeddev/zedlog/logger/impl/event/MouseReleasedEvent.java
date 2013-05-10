
package net.zeddev.zedlog.logger.impl.event;

import java.io.Writer;
import java.util.Scanner;
import org.jnativehook.mouse.NativeMouseEvent;

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
		msg.append(".\n");

		return msg.toString();

	}

}
