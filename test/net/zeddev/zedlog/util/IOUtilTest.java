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

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Unit test for {@link net.zeddev.zedlog.util.IOUtil}.
 * 
 * @author Zachary Scott <zscott.dev@gmail.com>
 */
public final class IOUtilTest {

	@Test
	public void testReadToReaderWriter() throws Throwable {
		
		String instr = "test string";
		
		StringReader in = new StringReader(instr);
		StringWriter out = new StringWriter();
		
		IOUtil.readTo(in, out);
		
		assertEquals(instr, out.toString());
		
	}
	
	@Test
	public void testReadToStreams() throws Throwable {
		
		byte[] inbytes = {1, 2, 3, 4};
		
		ByteArrayInputStream in = new ByteArrayInputStream(inbytes);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		IOUtil.readTo(in, out);
		
		byte[] outbytes = out.toByteArray();
		
		assertEquals(inbytes.length, outbytes.length);
		assertEquals(inbytes[0], outbytes[0]);
		assertEquals(inbytes[1], outbytes[1]);
		assertEquals(inbytes[2], outbytes[2]);
		assertEquals(inbytes[3], outbytes[3]);
		
	}
	
}
