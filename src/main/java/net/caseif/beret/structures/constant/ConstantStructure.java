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
package net.caseif.beret.structures.constant;

import net.caseif.beret.wrapper.ClassInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a structure containing a constant value.
 *
 * @author Max Roncacé
 */
public class ConstantStructure {

	private final ClassInfo parent;
	private final StructureType type;
	private final byte[] content;

	/**
	 * Creates a new {@link ConstantStructure} with a type and length inferred
	 * from the given byte tag.
	 *
	 * @param tag The byte tag denoting this structure's type
	 * @throws IllegalArgumentException If <code>tag</code> denotes a UTF-8
	 *                                  literal (in this case a specific length
	 *                                  must be supplied)
	 */
	public ConstantStructure(ClassInfo parent, byte tag, byte[] content) throws IllegalArgumentException {
		this.parent = parent;
		this.type = StructureType.fromTag(tag);
		if (this.type == null) {
			throw new IllegalArgumentException("Bad tag: " + tag);
		}
		this.content = content;
	}

	/**
	 * Gets the parent {@link ClassInfo} of this {@link ConstantStructure}.
	 *
	 * @return The parent {@link ClassInfo} of this {@link ConstantStructure}
	 */
	public ClassInfo getParent() {
		return this.parent;
	}

	/**
	 * Gets the content of this {@link ConstantStructure}.
	 *
	 * @return The content of this {@link ConstantStructure}.
	 */
	public byte[] getContent() {
		return this.content;
	}

	/**
	 * Gets the type of this {@link ConstantStructure}.
	 *
	 * @return The type of this {@link ConstantStructure}
	 */
	public StructureType getType() {
		return this.type;
	}

	/**
	 * Gets the length in bytes of this {@link ConstantStructure}'s content.
	 *
	 * @return The length in bytes of this {@link ConstantStructure}'s content
	 */
	public int getLength() {
		return type.getLength() > -1 ? type.getLength() : content.length;
	}

	public static ConstantStructure createConstantStructure(ClassInfo parent, byte tag, byte[] content) {
		StructureType type = StructureType.fromTag(tag);
		if (type == null) {
			throw new IllegalArgumentException("Invalid structure type tag");
		}
		switch (type) {
			case UTF_8:
				return new Utf8Structure(parent, content);
			case INTEGER:
				return new IntegerStructure(parent, content);
			case FLOAT:
				return new FloatStructure(parent, content);
			case LONG:
				return new LongStructure(parent, content);
			case DOUBLE:
				return new DoubleStructure(parent, content);
			case CLASS:
				return new ClassStructure(parent, content);
			case STRING:
				return new StringStructure(parent, content);
			case FIELD_REF:
				return new FieldrefStructure(parent, content);
			case METHOD_REF:
				return new MethodrefStructure(parent, content);
			case INTERFACE_METHOD_REF:
				return new InterfaceMethodrefStructure(parent, content);
			case NAME_AND_TYPE:
				return new NameAndTypeStructure(parent, content);
			case METHOD_HANDLE:
				return new MethodHandleStructure(parent, content);
			case METHOD_TYPE:
				return new MethodTypeStructure(parent, content);
			case INVOKE_DYNAMIC:
				return new InvokeDynamicStructure(parent, content);
			default:
				return new ConstantStructure(parent, tag, content);
		}
	}

	/**
	 * Represents a particular type of {@link ConstantStructure}.
	 */
	public enum StructureType {

		UTF_8(0x01, -1),
		INTEGER(0x03, 4),
		FLOAT(0x04, 4),
		LONG(0x05, 8),
		DOUBLE(0x06, 8),
		CLASS(0x07, 2),
		STRING(0x08, 2),
		FIELD_REF(0x09, 4),
		METHOD_REF(0x0A, 4),
		INTERFACE_METHOD_REF(0x0B, 4),
		NAME_AND_TYPE(0x0C, 4),
		METHOD_HANDLE(0x0F, 3),
		METHOD_TYPE(0x10, 2),
		INVOKE_DYNAMIC(0x12, 4);

		private static Map<Byte, StructureType> types;

		private byte tag;
		private int length;

		StructureType(int tag, int length) {
			this.tag = (byte)tag;
			this.length = length;
			register((byte)tag);
		}

		/**
		 * Adds this {@link StructureType} to the registry with the given tag.
		 *
		 * @param tag The tag to associated with this {@link StructureType}
		 */
		private void register(byte tag) {
			if (types == null) {
				types = new HashMap<>();
			}
			types.put(tag, this);
		}

		/**
		 * Gets the tag internally associated with this structure type.
		 *
		 * @return The tag internally associated with this structure type
		 */
		public byte getTag() {
			return this.tag;
		}

		/**
		 * Gets the expected length of structures of this type.
		 *
		 * @return The expected length of structures of this type
		 */
		public int getLength() {
			return this.length;
		}

		/**
		 * Gets the {@link StructureType} associated with the given byte tag.
		 *
		 * @param tag The tag to get a {@link StructureType} for
		 * @return The {@link StructureType} associated with the given byte tag
		 */
		public static StructureType fromTag(byte tag) {
			return types.get(tag);
		}

	}

}
