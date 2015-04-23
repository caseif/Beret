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
package net.caseif.beret.wrapper;

import net.caseif.beret.wrapper.synthetic.AccessFlag;
import net.caseif.beret.wrapper.synthetic.Instruction;
import net.caseif.beret.Util;
import net.caseif.beret.structures.AttributeStructure;
import net.caseif.beret.structures.CodeStructure;
import net.caseif.beret.structures.ConstantStructure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents information about a class file.
 *
 * @author Max Roncacé
 */
public class ClassInfo {

	private static final int CONSTANT_POOL_START = 10; // this will never change
	private static int INTERFACE_POOL_START;
	private static int FIELD_POOL_START;
	private static int METHOD_POOL_START;
	private static int ATTRIBUTE_POOL_START;

	private final byte[] bytes;

	private int majorVersion;
	private int minorVersion;

	private ConstantStructure[] constantPool;
	private int constantPoolLength;

	private AccessFlag accessFlag;

	private String className;
	private String superName;

	private String[] interfacePool;
	private FieldInfo[] fields;
	private MethodInfo[] methods;
	private AttributeStructure[] attributes;

	/**
	 * Loads a class file from the given {@link InputStream}.
	 * @param stream The stream to load the class file from
	 * @throws IllegalStateException If the stream does not begin with the
	 *                               standard magic number
	 * @throws IOException If an exception occurs while reading the stream
	 */
	public ClassInfo(InputStream stream) throws IllegalStateException, IOException {
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
		loadInterfaces();
		loadFields();
		loadMethods();
		loadAttributes();
	}

	/**
	 * Gets the qualified name of this class.
	 *
	 * @return The qualified name of this class
	 */
	public String getName() {
		return className;
	}

	/**
	 * Gets the qualified name of this class's superclass.
	 *
	 * @return The qualified name of this class's superclass
	 */
	public String getSuperName() {
		return superName;
	}

	/**
	 * Gets this class's access modifiers.
	 *
	 * @return This class's access modifiers
	 */
	public AccessFlag getAccessModifiers() {
		return accessFlag;
	}

	/**
	 * Gets this class's major version.
	 *
	 * @return This class's major version
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	/**
	 * Gets this class's major version.
	 *
	 * @return This class's major version
	 */
	public int getMinorVersion() {
		return minorVersion;
	}

	/**
	 * Gets this class's field pool.
	 *
	 * @return This class's field pool
	 */
	public FieldInfo[] getFields() {
		return fields;
	}

	/**
	 * Gets this class's field pool.
	 *
	 * @return This class's field pool
	 */
	public MethodInfo[] getMethods() {
		return methods;
	}

	/**
	 * Gets the bytes comprising this class.
	 *
	 * @return The bytes comprising this class
	 */
	public byte[] getBytes() {
		return this.bytes;
	}

	/**
	 * Returns this class's constant pool.
	 *
	 * @return This class's constant pool
	 */
	public ConstantStructure[] getConstantPool() {
		return this.constantPool;
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
		accessFlag = new AccessFlag(AccessFlag.AccessTarget.CLASS,
				bytes[CONSTANT_POOL_START + constantPoolLength],
				bytes[CONSTANT_POOL_START + constantPoolLength + 1]);
	}

	/**
	 * Loads the class and superclass info.
	 */
	public void loadClassInfo() {
		int offset = CONSTANT_POOL_START + constantPoolLength + 2;
		int classInfoPointer = Util.bytesToUshort(bytes[offset], bytes[offset + 1]);
		ConstantStructure classInfo = constantPool[classInfoPointer - 1];
		if (classInfo.getType() != ConstantStructure.StructureType.CLASS) {
			throw new IllegalStateException("Class info pointer does not point to a class info structure");
		}
		int classNamePointer = Util.bytesToUshort(classInfo.getInfo()[0], classInfo.getInfo()[1]);
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
		} else { // super pointer is 0x00, so it defaults to Object
			superName = "java/lang/Object";
		}
		INTERFACE_POOL_START = CONSTANT_POOL_START + constantPoolLength + 6;
	}

	private void loadInterfaces() {
		int offset = INTERFACE_POOL_START; // byte offset in the binary file
		int poolLength = Util.bytesToUshort(bytes[offset], bytes[offset + 1]); // indexing starts at 1
		interfacePool = new String[poolLength];
		offset += 2;
		for (int i = 0; i < poolLength; i++) {
			int pointer = Util.bytesToUshort(bytes[offset], bytes[offset + 1]);
			if (pointer > 0) {
				ConstantStructure classStruct = constantPool[pointer - 1];
				if (classStruct.getType() != ConstantStructure.StructureType.CLASS) {
					throw new IllegalStateException("Interface pointer does not point to a class structure");
				}
				int utf8Pointer = Util.bytesToUshort(classStruct.getInfo()[0], classStruct.getInfo()[1]);
				ConstantStructure interfaceName = constantPool[utf8Pointer - 1];
				if (interfaceName.getType() != ConstantStructure.StructureType.UTF_8) {
					throw new IllegalStateException("Class structure does not point to a UTF-8 structure");
				}
				interfacePool[i] = Util.asUtf8(interfaceName.getInfo());
			} else {
				interfacePool[i] = "";
			}
			offset += 2; // move to the next pointer
		}
		FIELD_POOL_START = INTERFACE_POOL_START + 2 + interfacePool.length * 2;
	}

	public void loadFields() {
		int offset = FIELD_POOL_START;
		int fieldCount = Util.bytesToUshort(bytes[offset], bytes[offset + 1]);
		fields = new FieldInfo[fieldCount];
		offset += 2;
		for (int i = 0; i < fieldCount; i++) {
			byte[] info = new byte[bytes.length - offset];
			System.arraycopy(bytes, offset, info, 0, info.length);
			fields[i] = new FieldInfo(this, info);
			offset += 8; // field access, name, descriptor, and attribute count
			for (AttributeStructure attr : fields[i].getAttributes()) {
				offset += 6; // attribute name and length
				offset += attr.getInfo().length;
			}
		}
		METHOD_POOL_START = offset;
	}

	public void loadMethods() {
		int offset = METHOD_POOL_START;
		int methodCount = Util.bytesToUshort(bytes[offset], bytes[offset + 1]);
		methods = new MethodInfo[methodCount];
		offset += 2;
		for (int i = 0; i < methodCount; i++) {
			byte[] info = new byte[bytes.length - offset];
			System.arraycopy(bytes, offset, info, 0, info.length);
			methods[i] = new MethodInfo(this, info);
			offset += 8; // field access, name, descriptor, and attribute count
			for (AttributeStructure attr : methods[i].getAttributes()) {
				offset += 6; // attribute name and length
				offset += attr.getInfo().length;
			}
		}
		ATTRIBUTE_POOL_START = offset;
	}

	private void loadAttributes() {
		int offset = ATTRIBUTE_POOL_START;
		int attrSize = Util.bytesToUshort(getBytes()[offset], getBytes()[offset + 1]);
		offset += 2;
		attributes = new AttributeStructure[attrSize];
		for (int i = 0; i < attrSize; i++) {
			int namePointer = Util.bytesToUshort(getBytes()[offset], getBytes()[offset + 1]);
			ConstantStructure nameStruct = getConstantPool()[namePointer - 1];
			if (nameStruct.getType() != ConstantStructure.StructureType.UTF_8) {
				throw new IllegalArgumentException("Attribute name index does not point to a UTF-8 structure");
			}
			String name = Util.asUtf8(nameStruct.getInfo());
			offset += 2;
			//TODO: add support for long arrays
			long infoLength = Util.bytesToUint(getBytes()[offset], getBytes()[offset + 1],
					getBytes()[offset + 2], getBytes()[offset + 3]);
			if (infoLength > Integer.MAX_VALUE) {
				throw new UnsupportedOperationException("Attribute is too long");
			}
			offset += 4;
			byte[] finalInfo = new byte[(int)infoLength];
			System.arraycopy(getBytes(), offset, finalInfo, 0, (int)infoLength);
			offset += infoLength;
			attributes[i] = new AttributeStructure(this, name, finalInfo);
		}
	}

	/**
	 * Dumps a textual representation of this {@link ClassInfo} to the given
	 * {@link OutputStream}.
	 *
	 * @param stream The {@link OutputStream} to write to
	 * @throws IOException If an exception occurs while writing to the
	 *                     {@link OutputStream}
	 */
	@SuppressWarnings("unchecked")
	public void dump(OutputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Generated by Beret").append("\n\n");
		sb.append("Class name: ").append(className).append("\n");
		sb.append("Superclass name: ").append(superName).append("\n");
		sb.append("Class major/minor version: ").append(majorVersion).append(".").append(minorVersion).append("\n");
		sb.append("\n");
		sb.append("Constant pool dump:").append("\n");
		int i = 0;
		for (ConstantStructure cs : constantPool) {
			sb.append("  ");
			sb.append(i + 1).append(": ");
			sb.append(cs.getType().toString()).append(" - ");
			if (cs.getType() == ConstantStructure.StructureType.UTF_8) {
				sb.append(Util.asUtf8(cs.getInfo()).replaceAll("\\n", "\\\\n"));
			} else {
				sb.append(Util.bytesToHex(cs.getInfo()));
			}
			sb.append("\n");
			++i;
		}

		sb.append("\n");
		sb.append("Access flags: ");
		assert accessFlag.getTargetType() == AccessFlag.AccessTarget.CLASS;
		for (AccessFlag.ClassFlag ft : (Set<AccessFlag.ClassFlag>)accessFlag.getFlags()) {
			sb.append(ft.toString()).append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);

		sb.append("\n\n");
		sb.append("Interfaces:").append("\n");
		for (String s : interfacePool) {
			sb.append("  ").append(s).append("\n");
		}

		sb.append("\n");
		sb.append("Fields:").append("\n");
		for (FieldInfo f : fields) {
			sb.append("  ").append(f.getName()).append(":").append("\n");
			sb.append("    ").append("Access flags: ");
			assert f.getAccess().getTargetType() == AccessFlag.AccessTarget.FIELD;
			for (AccessFlag.FieldFlag flag : (Set<AccessFlag.FieldFlag>)f.getAccess().getFlags()) {
				sb.append(flag.toString()).append(" ");
			}
			sb.deleteCharAt(sb.length() - 1).append("\n");
			sb.append("    ").append("Descriptor: ").append(f.getDescriptor()).append("\n");
			sb.append("    ").append("Attributes:").append("\n");
			for (AttributeStructure attr : f.getAttributes()) {
				sb.append("      ").append(attr.getName()).append(": ")
						.append(Util.bytesToHex(attr.getInfo())).append("\n");
			}
		}

		sb.append("\n");
		sb.append("Methods:").append("\n");
		for (MethodInfo f : methods) {
			sb.append("  ").append(f.getName()).append(":").append("\n");
			sb.append("    ").append("Access flags: ");
			assert f.getAccess().getTargetType() == AccessFlag.AccessTarget.METHOD;
			for (AccessFlag.MethodFlag flag : (Set<AccessFlag.MethodFlag>)f.getAccess().getFlags()) {
				sb.append(flag.toString()).append(" ");
			}
			sb.deleteCharAt(sb.length() - 1).append("\n");
			sb.append("    ").append("Descriptor: ").append(f.getDescriptor()).append("\n");
			sb.append("    ").append("Attributes:").append("\n");
			for (AttributeStructure attr : f.getAttributes()) {
				sb.append("      ").append(attr.getName()).append(":");
				if (attr instanceof CodeStructure) {
					CodeStructure cs = (CodeStructure)attr;
					sb.append("\n").append("        ")
							.append("Max stack size: ").append(cs.getMaxStackSize()).append("\n");
					sb.append("        ").append("Max local variables: ").append(cs.getMaxLocalSize()).append("\n");
					sb.append("        ").append("Exception handlers: ").append(cs.getExceptionHandlers().length)
							.append(" (not dumped)").append("\n");
					sb.append("        ").append("Attributes: ").append(cs.getAttributes().length)
							.append(" (not dumped)").append("\n");
					sb.append("        ").append("Body:").append("\n");
					for (Instruction instr : cs.getCode()) {
						sb.append("          ").append(instr.getOpcode().toString().toLowerCase())
								.append(instr.getExtraBytes().length > 0 ? " " : "")
								.append(Util.bytesToHex(instr.getExtraBytes())).append("\n");
					}
				} else {
					sb.append(" ").append(Util.bytesToHex(attr.getInfo())).append("\n");
				}
			}
		}

		sb.append("Attributes:").append("\n");
		for (AttributeStructure attr : attributes) {
			sb.append("  ").append(attr.getName()).append(": ").append(Util.bytesToHex(attr.getInfo()));
		}

		sb.append("\n"); // for good measure

		stream.write(sb.toString().getBytes(Charset.forName("UTF-8")));
		stream.flush();
	}

}
