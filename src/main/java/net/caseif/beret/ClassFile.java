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

import net.caseif.beret.structures.field.ConstantStructure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ClassFile {

	private static final int CONSTANT_POOL_START = 10; // this will never change

	private final byte[] bytes;

	private int majorVersion;
	private int minorVersion;

	private ConstantStructure[] constantPool;
	private int constantPoolLength;

	private AccessFlag accessFlag;

	private String className;
	private String superName;

	/**
	 * Loads a class file from the given {@link InputStream}.
	 * @param stream The stream to load the class file from
	 * @throws IllegalStateException If the stream does not begin with the
	 *                               standard magic number
	 * @throws IOException If an exception occurs while reading the stream
	 */
	public ClassFile(InputStream stream) throws IllegalStateException, IOException {
		int b;
		List<Byte> byteList = new ArrayList<Byte>();
		while ((b = stream.read()) != -1) {
			byteList.add((byte)b);
		}
		bytes = new byte[byteList.size()];
		int i = 0;
		for (Byte wrapped : byteList) {
			bytes[i] = wrapped;
			++i;
		}
		if (!checkMagicNumber()) {
			throw new IllegalStateException("Bytecode does not begin with standard magic number!");
		}
		parseVersion();
		loadConstantPool();
		loadAccessFlag();
		loadClassInfo();
	}

	/**
	 * Checks the magic number at the top of the loaded bytecode.
	 *
	 * @return <code>true</code> if the top four bytes equal 0xCAFEBABE,
	 *         otherwise, <code>false</code>.
	 */
	private boolean checkMagicNumber() {
		// grab and check the first four bytes to ensure compliance with the format standard
		return bytes[0] == (byte)0xCA && bytes[1] == (byte)0xFE && bytes[2] == (byte)0xBA && bytes[3] == (byte)0xBE;
	}

	/**
	 * Parses the major/minor version from the loaded bytecode.
	 */
	private void parseVersion() {
		this.minorVersion = Util.bytesToUshort(bytes[4], bytes[5]);
		this.majorVersion = Util.bytesToUshort(bytes[6], bytes[7]);
	}

	/**
	 * Loads the constant pool from the loaded bytecode.
	 */
	private void loadConstantPool() {
		int poolSize = Util.bytesToUshort(bytes[8], bytes[9]);
		--poolSize; // indices start at 1 for whatever reason
		constantPool = new ConstantStructure[poolSize];
		int offset = 10;
		for (int i = 0; i < poolSize; i++) {
			byte tag = bytes[offset]; // get the tag of the current structure
			++offset; // move the offset to the content start
			ConstantStructure struct = null;
			if (ConstantStructure.StructureType.fromTag(tag) == ConstantStructure.StructureType.UTF_8) {
				int length = Util.bytesToUshort(bytes[offset], bytes[offset + 1]); // get defined length
				offset += 2; // move offset past length indicator
				try {
					struct = new ConstantStructure(tag, length);
				} catch (IllegalArgumentException ex) {
					System.err.println("Exception caught at offset " + offset + ":");
					ex.printStackTrace();
				}
			} else {
				try {
					struct = new ConstantStructure(tag);
				} catch (IllegalArgumentException ex) {
					System.err.println("Exception caught at offset " + offset + ":");
					ex.printStackTrace();
				}
			}
			if (struct == null) {
				throw new UnsupportedOperationException("Failed to load constant pool");
			}
			int length = struct.getLength(); // get expected length
			byte[] content = new byte[length]; // create empty array of expected size
			System.arraycopy(bytes, offset, content, 0, length);
			struct.setInfo(content); // set the info of the current structure
			offset += length; // move the offset to the next structure
			constantPool[i] = struct; // globally store the loaded structure
		}
		constantPoolLength = offset - 10;
	}

	/**
	 * Loads the class access flag.
	 */
	private void loadAccessFlag() {
		accessFlag = new AccessFlag(bytes[CONSTANT_POOL_START + constantPoolLength],
				bytes[CONSTANT_POOL_START + constantPoolLength + 1]);
	}

	/**
	 * Loads the class and superclass info.
	 *
	 * @throws IOException
	 */
	public void loadClassInfo() throws IOException {
		int offset = CONSTANT_POOL_START + constantPoolLength + 2;
		System.out.println(offset);
		int classInfoPointer = Util.bytesToUshort(bytes[offset], bytes[offset + 1]);
		System.out.println(bytes[offset] + " " + bytes[offset + 1]);
		ConstantStructure classInfo = constantPool[classInfoPointer - 1];
		if (classInfo.getType() != ConstantStructure.StructureType.CLASS) {
			throw new IllegalStateException("Class info pointer does not point to a class info structure");
		}
		int classNamePointer = Util.bytesToUshort(classInfo.getInfo()[0], classInfo.getInfo()[1]);
		System.out.println(classNamePointer);
		ConstantStructure classNameStruct = constantPool[classNamePointer - 1];
		if (classNameStruct.getType() != ConstantStructure.StructureType.UTF_8) {
			throw new IllegalStateException("Class name pointer does not point to a UTF-8 structure");
		}
		className = Util.asUtf8(classNameStruct.getInfo());

		int superInfoPointer = Util.bytesToUshort(bytes[offset + 2], bytes[offset + 3]);
		if (superInfoPointer > 0) {
			ConstantStructure superInfo = constantPool[superInfoPointer - 1];
			if (superInfo.getType() != ConstantStructure.StructureType.CLASS) {
				throw new IllegalStateException("Superclass info pointer does not point to a class info structure");
			}
			int superNamePointer = Util.bytesToUshort(superInfo.getInfo()[0], superInfo.getInfo()[1]);
			ConstantStructure superNameStruct = constantPool[superNamePointer - 1];
			if (superNameStruct.getType() != ConstantStructure.StructureType.UTF_8) {
				throw new IllegalStateException("Superclass name pointer does not point to a UTF-8 structure");
			}
			superName = Util.asUtf8(superNameStruct.getInfo());
		}
		else {
			superName = "java/lang/Object";
		}
	}

	/**
	 * Writes a textual representation of this {@link ClassFile} to the given
	 * {@link OutputStream}.
	 *
	 * @param stream The {@link OutputStream} to write to
	 * @throws IOException If an exception occurs while writing to the
	 *                     {@link OutputStream}
	 */
	public void writeOut(OutputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Generated by Beret").append("\n").append("\n");
		sb.append("Class name: ").append(className).append("\n");
		sb.append("Superclass name: ").append(superName).append("\n");
		sb.append("Class major/minor version: ").append(majorVersion).append(".").append(minorVersion).append("\n");
		sb.append("\n");
		sb.append("Constant pool dump:").append("\n");
		int i = 0;
		for (ConstantStructure cs : constantPool) {
			sb.append("    ");
			sb.append(i + 1).append(": ");
			sb.append(cs.getType().toString()).append(" - ");
			if (cs.getType() == ConstantStructure.StructureType.UTF_8) {
				sb.append(Util.asUtf8(cs.getInfo()).replaceAll("\\n", "\\\\n"));
			} else {
				for (byte b : cs.getInfo()) {
					sb.append(String.format("%02X", b));
				}
				//sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("\n");
			++i;
		}
		sb.append("\n");
		sb.append("Access flags:").append("\n");
		for (AccessFlag.FlagType ft : accessFlag.getFlags()) {
			sb.append("    ").append(ft.toString()).append("\n");
		}

		stream.write(sb.toString().getBytes(Charset.forName("UTF-8")));
		stream.flush();
	}

}

