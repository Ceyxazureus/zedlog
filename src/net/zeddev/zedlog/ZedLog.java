package net.zeddev.zedlog;
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

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.gui.ZedLogFrame;
import net.zeddev.zedlog.logger.impl.CompositeDataLogger;
import net.zeddev.zedlog.logger.impl.DataLoggers;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 * ZedLog application class.
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class ZedLog implements UncaughtExceptionHandler {

	private final Logger logger = Logger.getLogger(this);

	// the main GUI frame
	private ZedLogFrame zedlogFrame = null;
	
	// the data loggers to be used
	private final CompositeDataLogger loggers = new CompositeDataLogger();
		// NOTE Should NOT be modified (i.e. no adding/removing loggers) until
		//      in the correct thread.
	
	// the logger types add on the command line
	private final List<String> loggerTypes = new ArrayList<String>();
	
	// whether to run the gui or not
	private boolean runGui = true;
	
	private void die() {
		logger.info("Dying!");
		System.exit(1);
	}

	private void addShutdownHook() {

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				logger.info("Shutdown hook called.");
				shutdown();
			}

		});

	}

	// shutdown the program
	private void shutdown() {

		logger.info(String.format(
			"Shutting down in thread - %s (#%d).",
			Thread.currentThread().getName(),
			Thread.currentThread().getId()
		));

		// shutdown the GUI
		if (zedlogFrame != null)
			zedlogFrame.shutdown();

		// remove the native event hook
		if (GlobalScreen.isNativeHookRegistered())
			GlobalScreen.unregisterNativeHook();

	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		String msg = String.format(
			"Uncaught exception %s in thread - %s (%d)",
			ex.getClass().getName(),
			thread.getName(), thread.getId()
		);

		logger.warning(msg, ex);

	}

	// initialise the program before starting
	private void init(String[] args) {

		handleArgs(args);
		
		addShutdownHook();

		// add global uncaught exception handler
		Thread.setDefaultUncaughtExceptionHandler(this);

		// enable anti-aliases fonts
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
		
		// initialise the GUI look and feel
		try {

			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
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
	
	// adds the data loggers to the composite logger
	private void initLoggers() {
		
		logger.info(
			"Updating data loggers in %s thread.", null, 
			Thread.currentThread().getName()
		);
		
		// add the data loggers given on the command line
		for (String type : loggerTypes) {
			
			try {
				
				loggers.addLogger(
					DataLoggers.newDataLogger(type)
				);
				
			} catch (IOException ex) {
				logger.error("Failed to add data logger %s.", ex, type);
			}
			
		}
		
	}
	
	// prints the program usage info
	private void usage() {
		
		System.out.print(
		" \n" +
		"Options: \n" +
		"-help, -h \n" +
		"    Displays this help/usage information. \n" + 
		"-version \n" + 
		"    Prints the version and other information. \n" + 
		"-add, -a <type> \n"  + 
		"    Adds a logger of the given type.  Use the -types argument for a" + 
		"    list of available logger types. \n" + 
		"-quiet, -q \n" + 
		"    Runs in quiet mode (i.e. daemonised). \n" + 
		"-file, -f <filename> \n" + 
		"    Sets the file to store logged data. \n" +
		"-log-file <filename> \n" + 
		"    Sets the program/message log file. \n" +
		" \n"
		);
		
	}
	
	// prints version information
	private void version() {
		
		System.out.println(
		"\n" +
		Config.INSTANCE.FULL_NAME + "\n" +
		Config.INSTANCE.DESCRIPTION + "\n" +
		"\n"
		);
		
	}
	
	// prints the available data logger types
	private void printDataLoggerTypes() {
		
		System.out.println("The available data logger types are: ");
		
		for (String type : DataLoggers.typeList())
			System.out.println(" * " + type);
		
		System.out.println();
		
	}
	
	// adds a new data logger
	private void addLogger(String type) throws IOException {
		
		List<String> typeList = DataLoggers.typeList();
		
		if (typeList.contains(type)) {
			
			loggerTypes.add(type);
			
			logger.info("Adding logger %s.", null, type);
			
		} else {
			logger.warning("Unknown logger type %s", null, type);
		}
		
	}
	
	// sets the data logger output log file
	private void setLogFile(String filename) {
		
		logger.info("Setting data logger file to %s.", null, filename);
		
		try {
			loggers.setLogFile(new File(filename));
		} catch (IOException ex) {
			logger.error("Failed to set log file %s.", ex, filename);
		}
		
	}
	
	// handles command line arguments
	private void handleArgs(String[] args) {
		
		// handle each argument
		for (int i = 0; i < args.length;) {
			String arg = args[i++];
			
			switch (arg) {
			
			// display help/usage
			case "-help": case "-h":
				usage();
				System.exit(0);
			break;
			
			// display version
			case "-version":
				version();
				System.exit(0);
			break;
			
			// add logger
			case "-add": case "-a": {
				
				if ((i + 1) < args.length)
					logger.error("%s requires the logger type.", null, arg);
				
				String type = args[i++];
				
				// add the logger
				try {
					addLogger(type);
				} catch (IOException ex) {
					logger.error("Failed to add logger %s.", ex, type);
				}
				
			} break;
			
			// print the available data logger types
			case "-types":
				printDataLoggerTypes();
				System.exit(0);
			break;
			
			// set quiet mode
			case "-quiet": case "-q":
				
				runGui = false;

				logger.info("Set quiet mode.");
				
			break; 
			
			// TODO add -hidden argument
			//      hide gui, rather than disable it
			
			// set log file
			case "-file": case "-f": {
				
				String filename = args[i++];
				
				setLogFile(filename);
				
			} break;
				
			default:
				logger.warning("Unknown argument %s", null, arg);
			break;
			
			}
			
		}
		
	}

	// starts the gui
	private void startGui() {

		// start gui on event queue
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				initNativeHook();
				
				initLoggers();

				zedlogFrame = new ZedLogFrame(loggers);
				zedlogFrame.setVisible(true);
				
			}
		});

	}
	
	// waits in the background for 
	private void runDaemon() {
		
		logger.info("Running as daemon.");
		
		initNativeHook();
		
		initLoggers();
		
		while (true) {
			// NOTE will run indefinitely ... until killed
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				logger.warning("Interrupted!", ex);
			}
			
		}
		
	}
	
	// starts the program
	private void start() {
		
		if (runGui) {
			startGui();
		} else {
			runDaemon();
		}
		
	}

	public static void main(String[] args) {

		ZedLog zedlog = new ZedLog();
		
		zedlog.init(args);
		zedlog.start();
		zedlog.shutdown();
		
	}

}
