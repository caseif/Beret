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
import net.caseif.beret.Util;
import net.caseif.beret.structures.AttributeStructure;
import net.caseif.beret.structures.ConstantStructure;

/**
 * Contains information regarding a specific field.
 *
 * @author Max Roncacé
 */
public class FieldInfo {

	private ClassInfo parent;
	private AccessFlag access;
	private String name;
	private String descriptor;
	private AttributeStructure[] attributes;

	/**
	 * Loads information about a field from the given {@link ConstantStructure}.
	 * @param parent The parent {@link ClassInfo} instance
	 * @param info The byte array non-exclusively containing this field's info,
	 *             with index 0 containing the first byte
	 * @throws IllegalArgumentException If <code>structure</code> contains
	 *                                  invalid data
	 */
	public FieldInfo(ClassInfo parent, byte[] info) throws IllegalArgumentException {
		this.parent = parent;

		// get the access flag
		access = new AccessFlag(AccessFlag.AccessTarget.FIELD, info[0], info[1]);

		// get the name from the provided pointer
		int namePointer = Util.bytesToUshort(info[2], info[3]);
		ConstantStructure nameStruct = parent.getConstantPool()[namePointer - 1];
		if (nameStruct.getType() != ConstantStructure.StructureType.UTF_8) {
			throw new IllegalArgumentException("Name index does not point to a UTF-8 structure");
		}
		name = Util.asUtf8(nameStruct.getInfo());

		// get the descriptor from the provided pointer
		int descPointer = Util.bytesToUshort(info[4], info[5]);
		ConstantStructure descStruct = parent.getConstantPool()[descPointer - 1];
		if (descStruct.getType() != ConstantStructure.StructureType.UTF_8) {
			throw new IllegalArgumentException("Descriptor index does not point to a UTF-8 structure");
		}
		descriptor = Util.asUtf8(descStruct.getInfo());

		loadAttributes(parent, info);
	}

	private void loadAttributes(ClassInfo parent, byte[] info) {
		int attrSize = Util.bytesToUshort(info[6], info[7]);
		attributes = new AttributeStructure[attrSize];
		int offset = 8;
		for (int i = 0; i < attrSize; i++) {
			int namePointer = Util.bytesToUshort(info[offset], info[offset + 1]);
			ConstantStructure nameStruct = parent.getConstantPool()[namePointer - 1];
			if (nameStruct.getType() != ConstantStructure.StructureType.UTF_8) {
				throw new IllegalArgumentException("Attribute name index does not point to a UTF-8 structure");
			}
			String name = Util.asUtf8(nameStruct.getInfo());
			offset += 2;
			int infoLength = Util.bytesToInt(info[offset], info[offset + 1],
					info[offset + 2], info[offset + 3]);
			offset += 4;
			byte[] finalInfo = new byte[infoLength];
			System.arraycopy(info, offset, finalInfo, 0, infoLength);
			offset += infoLength;
			attributes[i] = new AttributeStructure(getParent(), name, finalInfo);
		}
	}

	/**
	 * Gets the parent {@link ClassInfo} instance.
	 *
	 * @return The parent {@link ClassInfo} instance
	 */
	public ClassInfo getParent() {
		return this.parent;
	}

	/**
	 * Gets the access modifiers of this field.
	 *
	 * @return The access modifiers of this field
	 */
	public AccessFlag getAccess() {
		return this.access;
	}

	/**
	 * Gets the name associated with this {@link FieldInfo} instance.
	 *
	 * @return The name associated with this {@link FieldInfo} instance
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the descriptor associated with this {@link FieldInfo} instance.
	 *
	 * @return The descriptor associated with this {@link FieldInfo} instance
	 */
	public String getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Gets the {@link AttributeStructure}s associated with this
	 * {@link FieldInfo} instance.
	 *
	 * @return The {@link AttributeStructure}s associated with this
	 * {@link FieldInfo} instance.
	 */
	public AttributeStructure[] getAttributes() {
		return this.attributes;
	}

}
