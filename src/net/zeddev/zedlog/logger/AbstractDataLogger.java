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

package net.zeddev.zedlog.logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a skeletal implementation of <code>DataLogger</code>.
 * Lightens the burden of implementing a <code>DataLogger</code>.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public abstract class AbstractDataLogger implements DataLogger {

	private final List<DataLoggerObserver> observers = new ArrayList<>();

	// the thread responsible for notificating observers of log events
	private final DataLoggerNotificationThread notifyThread;

	// whether or not to record the log entries
	private boolean recording = true;

	protected AbstractDataLogger() {

		// start the notification thread
		notifyThread = new DataLoggerNotificationThread(this, observers);
		notifyThread.start();

	}

	// just a precaution in case shutdown() is not called
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		shutdown();
	}

	@Override
	public void shutdown() {

		// stop the notify thread
		notifyThread.shutdown();

	}

	@Override
	public void addObserver(DataLoggerObserver observer) {

		synchronized (observers) {
			observers.add(observer);
		}

	}

	@Override
	public void removeObserver(DataLoggerObserver observer) {

		synchronized (observers) {
			observers.remove(observer);
		}

	}

	/**
	 * Notifies all observers of a log event.
	 *
	 * @param logger The logger which made the notification.
	 * @param logEntry The log entry.
	 */
	protected void notifyDataLoggerObservers(final DataLogger logger, final LogEntry logEntry) {

		if (isRecording()) {
			// XXX notify observers only if recording

			notifyThread.notifyEvent(logEntry);

		}

	}

	@Override
	public boolean isRecording() {
		return recording;
	}

	@Override
	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	@Override
	public String toString() {
		return type();
	}

	@Override
	public abstract String type();

}
