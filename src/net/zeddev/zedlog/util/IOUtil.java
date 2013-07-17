package net.zeddev.zedlog.util;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/** 
 * Input/output utilities methods.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class IOUtil {
	
	/** Reads the given {@code Reader} to the given {@code Writer}. */
	public static void readTo(Reader in, Writer out) throws IOException {
	
		assert(in != null);
		assert(out != null);
		
		// read from in, write to out
		int ch;
		while ((ch = in.read()) >= 0)
			out.write(ch);
		
	}
	
	/** Reads the given {@code InputStream} to the given {@code OutputStream}. */
	public static void readTo(InputStream in, OutputStream out) throws IOException {
		
		assert(in != null);
		assert(out != null);
		
		// read from in, write to out
		int b;
		while ((b = in.read()) >= 0)
			out.write(b);
		
	}
	
}
