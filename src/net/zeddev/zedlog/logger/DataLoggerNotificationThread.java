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

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import net.zeddev.litelogger.Logger;

/**
 * The thread which notifies a {@code DataLogger}'s observers of an event.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class DataLoggerNotificationThread extends Thread {

	/** The size (max) of the event queue for the notification thread. */
	public static final int QUEUE_SIZE = 25;
		// NOTE: The queue size also determines the delay times in event dispatch.
		//	   See WAIT_CYCLE below for more info.

	/** The delay in-between processing events on the queue (in milliseconds). */
	public static final int WAIT_CYCLE = 1000 / (QUEUE_SIZE * 4);
		// NOTE: Calculated using th above formulae to avoid losing events on the
		//	   queue if posible.  It is still resonable XXX a  for response time.
		// NOTE: The timing of these event dispatch is not critical as the exact
		//	   time the event was received is logged in the LogEntry class.  So
		//	   this is just an optimisation to provide tolerable response
		//	   times.

	private final Logger logger = Logger.getLogger(this);

	// the associated data logger and its observers
	private DataLogger dataLogger;
	private final List<DataLoggerObserver> observers;

	// the event spool
	private final Queue<LogEntry> logSpool = new ArrayBlockingQueue<>(QUEUE_SIZE);
		// NOT queue size sets threshold for number of events

	// whether the thread is running or not
	private boolean running = false;

	/**
	 * Creates a new {@code DataLoggerNotificationThread} which handles
	 * asynchronous event dispatch for {@code DataLogger}s.
	 *
	 * @param dataLogger The parent data logger (must not be {@code null}).
	 * @param observers The list of observers for the given data logger (must
	 * not be {@code null}).
	 */
	public DataLoggerNotificationThread(DataLogger dataLogger, List<DataLoggerObserver> observers) {

		super();

		assert(dataLogger != null);
		assert(observers != null);

		// initialise the thread
		setName(String.format("%s notification thread", dataLogger.type()));
		setPriority(Thread.MIN_PRIORITY);
		setDaemon(true);

		this.observers = observers;
		this.dataLogger = dataLogger;

	}

	/**
	 * Notifies observers of the given {@code LogEntry} asynchronously.
	 *
	 * @param logEntry The logged event.
	 */
	public void notifyEvent(LogEntry logEntry) {

		synchronized (logSpool) {
			logSpool.offer(logEntry);
		}

	}

	/** Shuts the notification thread down. */
	public void shutdown() {
		running = false;
	}

	// notifies the loggers observers of the given log entry
	private void notifyObservers(LogEntry logEntry) {

		synchronized (observers) {

			for (DataLoggerObserver observer : observers)
				observer.notifyLog(dataLogger, logEntry);

		}

	}

	@Override
	public void run() {

		running = true;

		while (running) {

			synchronized(logSpool) {

				// notify observers of event if available
				if (!logSpool.isEmpty())
					notifyObservers(logSpool.remove());

			}

			try {
				Thread.sleep(WAIT_CYCLE);
			} catch (InterruptedException ex) {	}

		}

	}

}
