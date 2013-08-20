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

import java.lang.reflect.Field;

/**
 * <p>
 * A utility to assist in creating <code>hashCode()</code> implementations.
 * Can simply be used like so;
 * </p>
 * 
 * <p>
 *<pre>
 *public class SomeClass {
 *
 *    // class fields
 *    int anInt;
 *    Object someObject;
 *    ... // handles all types (via boxing)
 *
 *    public int hashCode() {
 *        return HashUtil.hashFor(this);
 *    }
 *
 *}
 *</pre>
 * </p>
 * 
 * <p>
 * <b>NOTE:</b> This file is released as a stand-alone utility.  The original 
 * file can be found on GitHub Gist - 
 * <a href="https://gist.github.com/zscott92/5465510">here</a>.
 * </p>
 *
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class HashUtil {

  // used to mix 32 bit integers into the hash value
	private static final int MIXING_PRIME = 2147483647;
		// Chosen as largest prime which will not overflow 63 bits when multiplied with a 32 bit integer.

	// the primes used to mix smaller ints into 32 bits
	private static final int PRIME16 = 8191;
	private static final int PRIME8 = 524287;
		// NOTE Chosen as mersenne primes which will not to overflow 32 bits
		//      when multiplied with their respective integer sizes.

	private int hash;

	/**
	 * Creates a new <code>HashUtil</code> with the default initial state (1).
	 *
	 */
	public HashUtil() {
		hash = 1;
	}

	/**
	 * Creates a new <code>HashUtil</code> with the given initial hash value.
	 *
	 * @param initial The initial hash state.
	 */
	public HashUtil(int initial) {
		hash = initial;
	}

	public int hash(byte b) {
		return hash((int) b * PRIME8);
	}

	public int hash(Byte i) {
		return hash(i.byteValue());
	}

	public int hash(char c) {
		return hash((short) c);
	}

	public int hash(Character i) {
		return hash(i.charValue());
	}

	public int hash(short s) {
		return hash((int) s * PRIME16);
	}

	public int hash(Short i) {
		return hash(i.shortValue());
	}

	public int hash(int i) {
		// NOTE This is the core of the hashing algorithm and any changes
		//      here will be reflected in all other hash() calls.

		long tmp = (long) hash * MIXING_PRIME; // mix into a 64 bit number space
		hash = (int) (tmp >>> 32 ^ tmp); // now mix back into 32 bit
		
		return hash ^= i;

	}

	public int hash(Integer i) {
		return hash(i.intValue());
	}

	public int hash(long l) {
		return hash((int) (l ^ (l >>> 32)));
	}

	public int hash(Long l) {
		return hash(l.longValue());
	}

	public int hash(float f) {
		return hash(Float.floatToIntBits(f));
	}

	public int hash(Float f) {
		return hash(f.floatValue());
	}

	public int hash(double d) {
		return hash(Double.doubleToLongBits(d));
	}

	public int hash(Double d) {
		return hash(d.doubleValue());
	}

	public int hash(final Object obj) {
		return hash(obj.hashCode());
	}

	/** Hashes all of the given objects. */
	public int hash(Object... objs) {

		for (Object obj : objs)
			hash(obj);

		return hash();

	}

	/**
	 * Hashes all of the given objects.
	 * Can of course be used with primitive types too, via the use of
	 * boxing (or autoboxing).
	 *
	 */
	public static int hashAll(Object... objs) {
		HashUtil hashUtil = new HashUtil();
		return hashUtil.hash(objs);
	}

	/**
	 * Hashes the given object.
	 * Hashes each of the given objects fields via the use of reflection.  This
	 * allows for a much simpler <code>hashCode</code>.
	 * <br />
	 *<pre>
	 *public int hashCode() {
	 *    return HashUtil.hashFor(this);
	 *}
	 *</pre>
	 *
	 * @param object The object to be hashed.
	 * @return The hash code of the hashed object.
	 * @throws IllegalAccessException If access to <code>object</code>s fields
	 *         are not accessible.
	 * @throws SecurityException If a security manager prevents access to the
	 *         objects fields.
	 */
	public static int hashFor(Object object)
			throws IllegalAccessException {

		HashUtil hashUtil = new HashUtil();

		// hash each field in the object
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			hashUtil.hash(field.get(object));
		}

		return hashUtil.hash();

	}

	/** Returns the current hash code value. */
	public int hash() {
		return hash;
	}

	@Override
	public int hashCode() {
		return hash();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || obj.getClass() != getClass()) {
			return false;
		} else {
			HashUtil other = (HashUtil) obj;
			return hash == other.hash;
		}

	}

}