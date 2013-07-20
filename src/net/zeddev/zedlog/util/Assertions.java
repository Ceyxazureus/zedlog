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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A simple facility to support design-by-contract principles.
 * Significantly smaller and simpler than other more heavy-weight frameworks 
 * (such as contract2j, OVal, etc) but provides better functionality over the
 * intrinsic Java {@code assert}.
 * </p>
 * 
 * <p>
 * The methods work with with the existing Java assertion facility, making it 
 * simple and portable to use (for instance with IDE's & build automation 
 * tools).  No special handling to enable/disable (just enable assertions in the 
 * VM, using the {@code -ea} switch).
 * </p>
 * 
 * <p>
 * All invariant assertions provided as static methods in the {@code Assertions}
 * class.  There are three types/categories of assertion which are;
 * <ul>
 *   <li>Preconditions - invariant condition before executing a method 
 *   ({@code require} prefix).</li>
 *   <li>General - a general invariant condition in main body of a method
 *   ({@code check} prefix).</li>
 *   <li>Postconditions - invariant condition after executing body of a method 
 *   ({@code ensure} prefix).</li>
 * </ul>
 * </p>
 *
 * <p>
 * There are also three types of condition;
 * <ul>
 *   <li>general/predicate (no suffix)</li>
 *   <li>null check ({@code NotNull} suffix)</li>
 *   <li>equality ({@code Equals} suffix)</li>
 * </ul>
 * </p>
 * 
 * <p>
 * There are also three types of condition;
 * <ul>
 *   <li>general/predicate (no suffix)</li>
 *   <li>null check ({@code NotNull} suffix)</li>
 *   <li>equality ({@code Equals } or {@code NotEquals} suffix)</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The assertion methods are named using the following format; 
 * {@code type condition-type} using camel case.  So for instance;
 * <br />
 * <pre>
 * requireNotNull(...); // precondition null check
 * checkNotNull(...); // general null check
 * ensureEquals(...); // postcondition equality check
 * // etc...
 * </pre>
 * </p>
 * 
 * <p>
 * The builtin {@code assert} can be used in conjunction with the 
 * {@code Assertions} methods to reproduce traditional assertion behavior,
 * where the statement is not executed when assertions are not enabled.
 * This is done like so;
 * </p>
 * 
 * <p>
 * <pre>
 * assert require(...);
 * assert checkNotNull(...);
 * etc.
 * </pre>
 * </p>
 * 
 * <p>
 * <b>NOTE:</b> This file is released as a stand-alone utility.  The original 
 * file and the associated unit test can be found on GitHub Gist - 
 * <a href="https://gist.github.com/zscott92/5795983">here</a>.
 * </p>
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class Assertions {
    
	/**
	 * Checks a precondition.
	 * 
	 * @param cond The condition that should be met.
	 * @param desc A description of the error, when {@code cond}
	 * is {@code false} (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean require(boolean cond, String desc, Object... args) {
		
		assert requireNotNull(desc);
		
		if (assertionsEnabled() && !cond) {
			
			throw fromCaller(new PreconditionException(
				String.format(desc, args)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks a precondition.
	 * 
	 * @param cond The condition that should be met.
	 */
	public static boolean require(boolean cond) {
		return require(cond, "");
	}
	
	/**
	 * Checks that an object reference is not {@code null} (as a precondition).
	 * 
	 * @param object The object reference to be checked.
	 * @param name The name of the object.
	 */
	public static boolean requireNotNull(Object object, String name) {
		
		if (assertionsEnabled() && object == null) {
			
			throw fromCaller(new PreconditionException(
				nullExceptionMessage(name)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks that an object reference is not {@code null} (as a precondition).
	 * 
	 * @param object The object reference to be checked.
	 */
	public static boolean requireNotNull(Object object) {
		return requireNotNull(object, "");
	}
	
	/**
	 * Checks that an object has an expected value (as a precondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param expected The expected value of the {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean requireEquals(
			Object object, 
			Object expected, 
			String desc,
			Object... args) {
		
		assert requireNotNull(object);
		assert requireNotNull(expected);
		assert requireNotNull(desc);
		
		return require(object.equals(expected), desc, args);
		
	}
	
	/**
	 * Checks that an object has an expected value (as a precondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param expected The expected value of the {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value.
	 */
	public static boolean requireEquals(Object object, Object expected) {
		
		assert requireNotNull(object);
		assert requireNotNull(expected);
		
		if (assertionsEnabled() && !object.equals(expected)) {
			
			throw fromCaller(new PreconditionException(
				equalityExceptionMessage(object, expected)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks that an object does not have the given value (as a precondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param value The value to compare with {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean requireNotEquals(
			Object object, 
			Object value, 
			String desc,
			Object... args) {
		
		assert requireNotNull(object);
		assert requireNotNull(value);
		assert requireNotNull(desc);
		
		return require(!object.equals(value), desc, args);
		
	}
	
	/**
	 * Checks that an object does not have the given value (as a precondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param value The value to compare with {@code object} (must not be 
	 * {@code null}).
	 */
	public static boolean requireNotEquals(Object object, Object value) {
		
		assert requireNotNull(object);
		assert requireNotNull(value);
		
		if (assertionsEnabled() && object.equals(value)) {
			
			throw fromCaller(new PreconditionException(
				equalityExceptionMessage(object, value)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks a general invariant.
	 * 
	 * @param cond The condition that should be met.
	 * @param desc A description of the error, when {@code cond}
	 * is {@code false} (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean check(boolean cond, String desc, Object... args) {
		
		assert requireNotNull(desc);
		
		if (assertionsEnabled() && !cond) {
			
			throw fromCaller(new InvariantException(
				String.format(desc, args)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks a general invariant.
	 * 
	 * @param cond The condition that should be met.
	 */
	public static boolean check(boolean cond) {
		return check(cond, "");
	}
	
	/**
	 * Checks that an object reference is not {@code null}.
	 * 
	 * @param object The object reference to be checked.
	 * @param name The name of the object.
	 */
	public static boolean checkNotNull(Object object, String name) {
		
		if (assertionsEnabled() && object == null) {
			
			throw fromCaller(new InvariantException(
				nullExceptionMessage(name)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks that an object reference is not {@code null}.
	 * 
	 * @param object The object reference to be checked.
	 */
	public static boolean checkNotNull(Object object) {
		return checkNotNull(object, "");
	}
	
	/**
	 * Checks that an object has an expected value.
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param expected The expected value of the {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean checkEquals(
			Object object, 
			Object expected, 
			String desc,
			Object... args) {
		
		assert requireNotNull(object);
		assert requireNotNull(expected);
		assert requireNotNull(desc);
		
		return check(object.equals(expected), desc, args);
		
	}
	
	/**
	 * Checks that an object has an expected value.
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param expected The expected value of the {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value.
	 */
	public static boolean checkEquals(Object object, Object expected) {
		
		assert requireNotNull(object);
		assert requireNotNull(expected);
		
		if (assertionsEnabled() && !object.equals(expected)) {
			
			throw fromCaller(new InvariantException(
				equalityExceptionMessage(object, expected)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks that an object does not have the given value.
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param expected The value to compare with {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean checkNotEquals(
			Object object, 
			Object value, 
			String desc,
			Object... args) {
		
		assert requireNotNull(object);
		assert requireNotNull(value);
		assert requireNotNull(desc);
		
		return check(!object.equals(value), desc, args);
		
	}
	
	/**
	 * Checks that an object does not have the given value.
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param value The value to compare with {@code object} (must not be 
	 * {@code null}).
	 */
	public static boolean checkNotEquals(Object object, Object value) {
		
		assert requireNotNull(object);
		assert requireNotNull(value);
		
		if (assertionsEnabled() && object.equals(value)) {
			
			throw fromCaller(new InvariantException(
				equalityExceptionMessage(object, value)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks a postcondition.
	 * 
	 * @param cond The condition that should be met.
	 * @param desc A description of the error, when {@code cond}
	 * is {@code false} (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean ensure(boolean cond, String desc, Object... args) {
		
		assert requireNotNull(desc);
		
		if (assertionsEnabled() && !cond) {
			
			throw fromCaller(new PostconditionException(
				String.format(desc, args)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks a postcondition.
	 * 
	 * @param cond The condition that should be met.
	 */
	public static boolean ensure(boolean cond) {
		return ensure(cond, "");
	}
	
	/**
	 * Checks that an object reference is not {@code null} (as a postcondition).
	 * 
	 * @param object The object reference to be checked.
	 * @param name The name of the object.
	 */
	public static boolean ensureNotNull(Object object, String name) {
		
		if (assertionsEnabled() && object == null) {
			
			throw fromCaller(new PostconditionException(
				nullExceptionMessage(name)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks that an object reference is not {@code null} (as a postcondition).
	 * 
	 * @param object The object reference to be checked.
	 */
	public static boolean ensureNotNull(Object object) {
		return ensureNotNull(object, "");
	}
	
	/**
	 * Checks that an object has an expected value (as a postcondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param expected The expected value of the {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean ensureEquals(
			Object object, 
			Object expected, 
			String desc,
			Object... args) {
		
		assert requireNotNull(object);
		assert requireNotNull(expected);
		assert requireNotNull(desc);
		
		return ensure(object.equals(expected), desc, args);
		
	}
	
	/**
	 * Checks that an object has an expected value (as a postcondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param expected The expected value of the {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value.
	 */
	public static boolean ensureEquals(Object object, Object expected) {
		
		assert requireNotNull(object);
		assert requireNotNull(expected);
		
		if (assertionsEnabled() && !object.equals(expected)) {
			
			throw fromCaller(new PostconditionException(
				equalityExceptionMessage(object, expected)
			));
			
		}
		
		return true;
		
	}
	
	/**
	 * Checks that an object does not have the given value (as a precondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param value The value to compare with {@code object} (must not be 
	 * {@code null}).
	 * @param desc A description of the error when the object does not have the 
	 * expected value (using the {@code String.format()} format).
	 * Cannot be {@code null}.
	 * @param args The arguments to go with the formatted description.
	 */
	public static boolean ensureNotEquals(
			Object object, 
			Object value, 
			String desc,
			Object... args) {
		
		assert requireNotNull(object);
		assert requireNotNull(value);
		assert requireNotNull(desc);
		
		return ensure(!object.equals(value), desc, args);
		
	}
	
	/**
	 * Checks that an object does not have the given value (as a precondition).
	 * 
	 * @param object The object to be checked (must not be {@code null}).
	 * @param value The value to compare with {@code object} (must not be 
	 * {@code null}).
	 */
	public static boolean ensureNotEquals(Object object, Object value) {
		
		assert requireNotNull(object);
		assert requireNotNull(value);
		
		if (assertionsEnabled() && object.equals(value)) {
			
			throw fromCaller(new PostconditionException(
				equalityExceptionMessage(object, value)
			));
			
		}
		
		return true;
		
	}
	
	// builds a string describing a failed null pointer condition
	private static String nullExceptionMessage(String name) {
		
		if (name == null) {
			return "Null reference";
		} else {
			return String.format("%s must not be null", name);
		}
		
	}
	
	// builds a string describing an equality exception
	private static String equalityExceptionMessage(
			Object object, 
			Object expected) {
		
		StringBuilder msg = new StringBuilder();
		
		msg.append("Expected \"");
		msg.append(expected.toString());
		msg.append("\" but was \"");
		msg.append(object.toString());
		msg.append("\"");
		
		return msg.toString();
		
	}
	
	// checks whether assertions are enabled
	private static boolean assertionsEnabled() {
		
		boolean enabled = false;
		assert(enabled = true);
		
		return enabled;
		
	}
	
	// sets the exception stack trace from the callers scope 
	// (i.e removing internal calls from within this class)
	private static <E extends Throwable> E fromCaller(E ex) {
		
		List<StackTraceElement> stackTrace = new ArrayList<StackTraceElement>();
		
		// add all calls, not from this class
		for (StackTraceElement element : ex.getStackTrace()) {
			
			if (!element.getClassName().equals(Assertions.class.getName()))
				stackTrace.add(element);
			
		}
		
		// change to modified stack trace
		ex.setStackTrace(
			stackTrace.toArray(
				new StackTraceElement[stackTrace.size()]
			)
		);
		
		return ex;
		
	}
	
	/** Thrown when a precondition is not met (asserted with a call 
	 * to a {@code require*} method). */
	public static class PreconditionException extends AssertionError {
		
		/** Creates a new {@code PreconditionException}, with description. */
		public PreconditionException(String msg) {
			super(String.format(
				"Precondition not met; %s", msg
			));
			
		}
		
	}
	
	/** Thrown when a invariant condition is not met (asserted with a call 
	 * to a {@code check*} method). */
	public static class InvariantException extends AssertionError {
		
		/** Creates a new {@code InvariantException}, with description. */
		public InvariantException(String msg) {
			super(msg);
		}
		
	}
	
	/** Thrown when a postcondition is not met (asserted with a call 
	 * to an {@code ensure*} method). */
	public static class PostconditionException extends AssertionError {
		
		/** Creates a new {@code PostconditionException}, with description. */
		public PostconditionException(String msg) {
			super(String.format(
				"Postcondition not met; %s", msg
			));
			
		}
		
	}
	
}
