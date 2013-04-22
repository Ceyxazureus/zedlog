
package net.zeddev.zedlog.logger;

/**
 * A single log record by a <code>DataLogger</code>.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class LogEntry {

	private String message;
	private final long timestamp = System.currentTimeMillis();

	/**
	 * Creates a new <code>LogEntry</code> with the given detail message.
	 *
	 * @param message The logged message.
	 */
	public LogEntry(final String message) {
		setMessage(message);
	}

	public final String getMessage() {
		return message;
	}

	public final void setMessage(String message) {
		this.message = message;
	}

	public final long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return getMessage();
	}

}
