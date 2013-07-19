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

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Unit test suite for ZedLog.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class TestSuite {
	
	// the test cases
	public static final Class[] TEST_CLASSES = {
		// TODO add unit tests
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
	
	public static void main(String[] args) {
		
		TestSuite testSuite = new TestSuite();
		testSuite.runClasses(TEST_CLASSES);
		
	}
	
}
