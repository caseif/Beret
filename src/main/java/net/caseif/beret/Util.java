/*
 * New BSD License (BSD-new)
 *
 * Copyright (c) 2015 Maxim Roncacé
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the copyright holder nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.caseif.beret;

import net.caseif.beret.structures.AttributeStructure;

import java.nio.charset.Charset;

/**
 * A basic utility class.
 *
 * @author Max Roncacé
 */
public class Util {

	private static String tab = "    "; // default tab size of 4

	public static void setTabSize(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append(' ');
		}
		tab = sb.toString();
	}

	public static String tab(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append(tab);
		}
		return sb.toString();
	}

	public static int bytesToInt(byte b1, byte b2, byte b3, byte b4) {
		return (int)bytesToUint(b1, b2, b3, b4);
	}

	public static int bytesToInt(byte[] bytes) {
		if (bytes.length != 4) {
			throw new IllegalArgumentException("Bad byte array length");
		}
		return bytesToInt(bytes[0], bytes[1], bytes[2], bytes[3]);
	}

	public static long bytesToUint(byte b1, byte b2, byte b3, byte b4) {
		return ((b1 & 0xFF) << 24) + ((b2 & 0xFF) << 16) + ((b3 & 0xFF) << 8) + (b4 & 0xFF);
	}

	public static long bytesToUint(byte[] bytes) {
		if (bytes.length != 4) {
			throw new IllegalArgumentException("Bad byte array length");
		}
		return bytesToUint(bytes[0], bytes[1], bytes[2], bytes[3]);
	}

	public static short bytesToShort(byte b1, byte b2) {
		return (short)bytesToUshort(b1, b2);
	}

	public static short bytesToShort(byte[] bytes) {
		if (bytes.length != 2) {
			throw new IllegalArgumentException("Bad byte array length");
		}
		return bytesToShort(bytes[0], bytes[1]);
	}

	public static int bytesToUshort(byte b1, byte b2) {
		return ((b1 & 0xFF) << 8) + (b2 & 0xFF);
	}

	public static int bytesToUshort(byte[] bytes) {
		if (bytes.length != 2) {
			throw new IllegalArgumentException("Bad byte array length");
		}
		return bytesToUshort(bytes[0], bytes[1]);
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static String asUtf8(byte[] bytes) {
		return new String(bytes, Charset.forName("UTF-8"));
	}

	public static AttributeStructure getAttrFromName(AttributeStructure[] attrs, String name) {
		for (AttributeStructure as : attrs) {
			if (as.getName().equals(name)) {
				return as;
			}
		}
		return null;
	}

}
