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

package net.zeddev.zedlog;

import java.awt.EventQueue;
import net.zeddev.zedlog.logger.impl.KeyLogger;
import net.zeddev.zedlog.logger.impl.CompositeDataLogger;
import javax.swing.UnsupportedLookAndFeelException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import net.zeddev.zedlog.gui.ZedLogFrame;
import net.zeddev.zedlog.gui.ZedLogFrameController;
import net.zeddev.zedlog.logger.impl.MouseClickLogger;
import net.zeddev.litelogger.Logger;
import net.zeddev.litelogger.handlers.MsgBoxLogHandler;

/**
 * ZedLog application class.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public class ZedLog {

	private final Logger logger = Logger.getLogger(this);

	private void die() {
		logger.info("Dying!");
		System.exit(1);
	}

	private void addShutdownHook() {

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				shutdown();
			}

		});

	}

	// shutdown the program
	private void shutdown() {

		// remove the native event hook
		if (GlobalScreen.isNativeHookRegistered())
			GlobalScreen.unregisterNativeHook();

	}

	// initialise the program before starting
	private void init() {

		addShutdownHook();

		// add the gui logger handler
		Logger.addHandler(new MsgBoxLogHandler());

		// initalise the GUI look and feel
		try {

			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}

		} catch (ClassNotFoundException |
				 InstantiationException |
				 IllegalAccessException |
				 UnsupportedLookAndFeelException ex) {

			logger.error("Unable to set GUI look and feel.", ex);

		}

	}

	// initialises the JNativeHook lib
	private void initNativeHook() {

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			logger.fatal("Unable to secure native hook!", ex);
			die();
		}

	}

	// a simple test for loggers
	private void testLoggers() {

		CompositeDataLogger loggers = new CompositeDataLogger();
		loggers.addLogger(new KeyLogger());
		loggers.addLogger(new MouseClickLogger());

		System.out.println(loggers.type());
		System.out.println();

		System.out.println("Do some stuff so I can log it.");

		// wait 10 seconds so events can be recorded
		try {
			Thread.sleep(10000);
		} catch (InterruptedException ex) { }

		System.out.print(loggers.toString());

	}

	// starts the gui
	private void startGui() {

		final CompositeDataLogger loggers = new CompositeDataLogger();

		final ZedLogFrame zedlogFrame = new ZedLogFrame();

		final ZedLogFrameController zedLogFrameController =
				new ZedLogFrameController(zedlogFrame, loggers);

		// start gui on event queue
		EventQueue.invokeLater(new Runnable() {

			public void run() {

				initNativeHook();

				zedlogFrame.setVisible(true);
				loggers.shutdown();

			}

		});

	}

    public static void main(String[] args) {

		ZedLog zedlog = new ZedLog();
		zedlog.init();

		//zedlog.testLoggers();
		zedlog.startGui();

		zedlog.shutdown();

    }

}
