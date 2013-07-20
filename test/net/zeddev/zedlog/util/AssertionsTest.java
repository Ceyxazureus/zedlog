package net.zeddev.zedlog.util;
/* Copyright (C) 2013  Zachary Scott <zscott.dev@gmail.com>
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION  OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

import static org.junit.Assert.*;
import static net.zeddev.zedlog.util.Assertions.*;

import org.junit.Test;

/**
 * Unit test for {@code net.zeddev.util.Assertions}.
 * 
 * <p>
 * <b>NOTE:</b> The {@code Assertions} class is released as a stand-alone utility and
 * the original file can be found on GitHub Gist - 
 * <a href="https://gist.github.com/zscott92/5795983">here</a>.
 * </p>
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class AssertionsTest {

	/** Test passing conditions on assertion methods. */
	@Test
	public void testAssertTrue() throws Throwable {
		// NOTE all of the following assertions should pass
		
		Object testObject = "Test object";
		
		// precondition assertions
		require(true, "desc");
		require(true);
		requireNotNull(testObject, "name");
		requireNotNull(testObject);
		requireEquals(testObject, testObject, "desc");
		requireEquals(testObject, testObject);
		requireNotEquals(testObject, "not equals");

		// general invariant assertions
		check(true, "desc");
		check(true);
		checkNotNull(testObject, "name");
		checkNotNull(testObject);
		checkEquals(testObject, testObject, "desc");
		checkEquals(testObject, testObject);
		checkNotEquals(testObject, "not equals");
		
		// postcondition assertions
		ensure(true, "desc");
		ensure(true);
		ensureNotNull(testObject, "name");
		ensureNotNull(testObject);
		ensureEquals(testObject, testObject, "desc");
		ensureEquals(testObject, testObject);
		ensureNotEquals(testObject, "not equals");
		
	}
	
	/** Test fail conditions on assertion methods. */
	@Test
	public void testAssertFail() throws Throwable {
		
		// precondition assertions
		
		try {
			require(false);
			fail("Assertions.require() fail condition not caught!");
		} catch (PreconditionException ex) { }
		
		try {
			requireNotNull(null);
			fail("Assertions.requireNotNull() fail condition not caught!");
		} catch (PreconditionException ex) { }
		
		try {
			requireEquals("test1", "test2");
			fail("Assertions.requireEquals() fail condition not caught!");
		} catch (PreconditionException ex) { }
		
		try {
			requireEquals("test1", "test2", "desc");
			fail("Assertions.requireEquals() fail condition not caught!");
		} catch (PreconditionException ex) { }
		
		try {
			requireNotEquals("test1", "test1", "desc");
			fail("Assertions.requireNotEquals() fail condition not caught!");
		} catch (PreconditionException ex) { }
		
		// general assertions
		
		try {
			check(false);
			fail("Assertions.check() fail condition not caught!");
		} catch (InvariantException ex) { }
		
		try {
			checkNotNull(null);
			fail("Assertions.checkNotNull() fail condition not caught!");
		} catch (InvariantException ex) { }
		
		try {
			checkEquals("test1", "test2");
			fail("Assertions.checkEquals() fail condition not caught!");
		} catch (InvariantException ex) { }
		
		try {
			checkEquals("test1", "test2", "desc");
			fail("Assertions.checkEquals() fail condition not caught!");
		} catch (InvariantException ex) { }
		
		try {
			checkNotEquals("test1", "test1", "desc");
			fail("Assertions.checkNotEquals() fail condition not caught!");
		} catch (InvariantException ex) { }
		
		// precondition assertions
		
		try {
			ensure(false);
			fail("Assertions.ensure() fail condition not caught!");
		} catch (PostconditionException ex) { }
		
		try {
			ensureNotNull(null);
			fail("Assertions.ensureNotNull() fail condition not caught!");
		} catch (PostconditionException ex) { }
		
		try {
			ensureEquals("test1", "test2");
			fail("Assertions.ensureEquals() fail condition not caught!");
		} catch (PostconditionException ex) { }
		
		try {
			ensureEquals("test1", "test2", "desc");
			fail("Assertions.ensureEquals() fail condition not caught!");
		} catch (PostconditionException ex) { }
		
		try {
			ensureNotEquals("test1", "test1", "desc");
			fail("Assertions.ensureNotEquals() fail condition not caught!");
		} catch (PostconditionException ex) { }
		
	}
	
	/** Test calling with assert builtin. */
	@Test
	public void testCallWithAssert() throws Throwable {
		// NOTE all of the following assertions should pass
		
		Object testObject = "Test object";
		
		// precondition assertions
		assert require(true, "desc");
		assert require(true);
		assert requireNotNull(testObject, "name");
		assert requireNotNull(testObject);
		assert requireEquals(testObject, testObject, "desc");
		assert requireEquals(testObject, testObject);
		assert requireNotEquals(testObject, "not equals");

		// general invariant assertions
		assert check(true, "desc");
		assert check(true);
		assert checkNotNull(testObject, "name");
		assert checkNotNull(testObject);
		assert checkEquals(testObject, testObject, "desc");
		assert checkEquals(testObject, testObject);
		assert checkNotEquals(testObject, "not equals");
		
		// postcondition assertions
		assert ensure(true, "desc");
		assert ensure(true);
		assert ensureNotNull(testObject, "name");
		assert ensureNotNull(testObject);
		assert ensureEquals(testObject, testObject, "desc");
		assert ensureEquals(testObject, testObject);
		assert ensureNotEquals(testObject, "not equals");
		
	}
	
	/** Test whether the internal {@code fromCaller} correctly alters exception stack trace. */ 
	@Test
	public void testFromCaller() throws Throwable {
		
		try {
			check(false);
			// NOTE should throw exception here
		} catch (AssertionError e) {
			
			StackTraceElement[] stackTrace = e.getStackTrace(); 
			StackTraceElement top = stackTrace[0]; // top element
			
			// check that the element on top of the stack is this method
			assertEquals(top.getClassName(), getClass().getName());
			assertEquals(top.getMethodName(), "testFromCaller");
			
		}
		
	}

}
