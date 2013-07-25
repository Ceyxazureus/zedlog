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

import net.zeddev.litelogger.Logger;
import net.zeddev.zedlog.logger.KeyDataLoggers;
import net.zeddev.zedlog.util.AssertionsTest;
import net.zeddev.zedlog.util.IOUtilTest;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * Unit test suite for ZedLog.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
@RunWith(TestSuite.SuiteRunner.class)
public final class TestSuite {
	
	private static final Logger logger = Logger.getLogger(TestSuite.class);
	
	// the test cases
	public static final Class[] TEST_CLASSES = {
		AssertionsTest.class,
		IOUtilTest.class,
		KeyDataLoggers.class
	};
	
	// the test state
	private int runCount = 0; // the actual number of tests run
	private long runTime = 0; // the time taken to execute tests
	private int failCount = 0; // the number of failures
	
	// prints a single formatted line to stdout
	private static void printf(String fmt, Object... args) {
		
		assert(fmt != null);
		
		String msg = String.format(fmt, args);
		System.out.print(msg);
		
	}
	
	// runs the given test class
	private void runClass(Class clazz) {
		
		Result result = JUnitCore.runClasses(clazz);
		
		printf(">>>>> Running %s <<<<< \n", clazz.getSimpleName());
		
		// display any failures
		if (!result.wasSuccessful()) {
			
			printf("\nTest Failed! \n");
			printf(">>>>> \n");
			
			for (Failure failure : result.getFailures())
				printf("%s \n", failure.toString());
			
			printf("<<<<< \n\n");
			
		}
		
		runCount += result.getRunCount();
		runTime += result.getRunTime();
		failCount += result.getFailureCount();
		
	}
	
	// runs all of the given test classes
	private void runClasses(Class... classes) {
		
		printf("Running %d test classes. \n\n", classes.length);
		
		printf(">>>>>  Running Test Suite <<<<< \n");
		
		// run the unit tests
		for (Class clazz : TEST_CLASSES) 
			runClass(clazz);		
		
		printf(">>>>>  Test Suite Finished <<<<< \n\n");
		
		printf("%d tests run. \n", runCount);
		printf("Run time %.2f seconds \n", runTime / 1000.0);
		printf("\n");
		
		// pass report
		printf(
			"%d/%d Tests Passed. \n\n", 
			runCount - failCount,
			runCount
		);
		
	}
	
	// initialises the JNativeHook lib
	private static void initNativeHook() {

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			logger.fatal("Unable to secure native hook!", ex);
			System.exit(1);
		}

	}
	
	// shuts down the test suite app
	private static void shutdown() {
		
		logger.info(String.format(
			"Shutting down in thread - %s (#%d).",
			Thread.currentThread().getName(),
			Thread.currentThread().getId()
		));

		// remove the native event hook
		if (GlobalScreen.isNativeHookRegistered())
			GlobalScreen.unregisterNativeHook();

	}
	
	public static void main(String[] args) {
		
		initNativeHook();
		
		TestSuite testSuite = new TestSuite();
		testSuite.runClasses(TEST_CLASSES);
		
		shutdown();
		
	}
	
	/** 
	 * Test suite runner class used by JUnit to execute.
	 * Makes life easier when using an IDE.
	 */
	public static class SuiteRunner extends Suite {
		
		public SuiteRunner(Class<?> setupClass) throws InitializationError {
	       super(setupClass, TEST_CLASSES);
	    }
		
	}
	
}
