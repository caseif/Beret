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
package net.caseif.beret.structures;

import net.caseif.beret.Opcode;
import net.caseif.beret.Util;
import net.caseif.beret.wrapper.ClassInfo;
import net.caseif.beret.wrapper.MethodInfo;
import net.caseif.beret.wrapper.synthetic.ExceptionHandler;
import net.caseif.beret.wrapper.synthetic.Instruction;

import java.util.LinkedList;

/**
 * Represents a Code attribute in a method.
 *
 * @author Max Roncacé
 */
public class CodeStructure extends AttributeStructure {

	/**
	 * The number of bytes preceding the instruction array in a code attribute.
	 */
	private static final int HEADER_LENGTH = 8;

	private MethodInfo method;
	private int stackSize;
	private int localSize;
	private Instruction[] code;
	private ExceptionHandler[] exceptionHandlers;
	private AttributeStructure[] attributes;

	/**
	 * Creates a new {@link CodeStructure} with the given name and content.
	 *
	 * @param parent The parent {@link ClassInfo} instance
	 * @param name The name of the new {@link CodeStructure}
	 * @param info The content of the new {@link CodeStructure}
	 */
	public CodeStructure(MethodInfo parent, String name, byte[] info) {
		super(parent.getParent(), name, info);
		this.method = parent;
		this.stackSize = Util.bytesToUshort(info[0], info[1]);
		this.localSize = Util.bytesToUshort(info[2], info[3]);
		long codeSize = Util.bytesToUint(info[4], info[5], info[6], info[7]);
		if (codeSize > Integer.MAX_VALUE) {
			throw new UnsupportedOperationException("Code attribute is too long");
		}
		LinkedList<Instruction> instrs = new LinkedList<>();
		for (int i = 0; i < codeSize; i++) {
			Opcode opcode = Opcode.fromByte(info[i + HEADER_LENGTH]);
			if (opcode == null) {
				System.err.println("Unrecognized opcode 0x" + Util.bytesToHex(new byte[]{info[i + HEADER_LENGTH]})
						+ " at offset " + i + " for method " + parent.getName() + parent.getDescriptor());
				instrs.add(new Instruction(Opcode.UNKNOWN, i));
				continue;
			}
			int extra = opcode.getAdditionalBytes();
			if (extra == -1) {
				throw new UnsupportedOperationException("Unsupported opcode: " + opcode.toString()); //TODO
			}
			byte[] extraBytes = new byte[extra];
			System.arraycopy(info, i + HEADER_LENGTH + 1, extraBytes, 0, extra);
			i += extra;
			instrs.add(new Instruction(opcode, i - extra, extraBytes));
		}
		code = new Instruction[instrs.size()];
		instrs.toArray(code);
		int offset = (int)(HEADER_LENGTH + codeSize);
		int exceptionTableLength = Util.bytesToUshort(info[offset], info[offset + 1]);
		offset += 2;
		exceptionHandlers = new ExceptionHandler[exceptionTableLength];
		for (int i = 0; i < exceptionTableLength; i++) {
			int startIndex = Util.bytesToUshort(info[offset], info[offset + 1]);
			int endIndex = Util.bytesToUshort(info[offset], info[offset + 1]);
			int handlerStartIndex = Util.bytesToUshort(info[offset], info[offset + 1]);
			int catchType = Util.bytesToUshort(info[offset], info[offset + 1]);
			byte[] classRef = getParent().getFromPool(catchType).getContent();
			String catchTypeName = getParent().getFromPool(classRef).toString();
			exceptionHandlers[i] = new ExceptionHandler(
					method, startIndex, endIndex, handlerStartIndex, catchTypeName
			);
			offset += 8;
		}
		int attributeCount = Util.bytesToUshort(info[offset], info[offset + 1]);
		offset += 2;
		attributes = new AttributeStructure[attributeCount];
		for (int i = 0; i < attributeCount; i++) {
			int namePointer = Util.bytesToUshort(info[offset], info[offset + 1]);
			String attrName = getParent().getFromPool(namePointer).toString();
			offset += 2;
			//TODO: add support for long arrays
			long infoLength = Util.bytesToUint(info[offset], info[offset + 1],
					info[offset + 2], info[offset + 3]);
			if (infoLength > Integer.MAX_VALUE) {
				throw new UnsupportedOperationException("Attribute is too long");
			}
			offset += 4;
			byte[] finalInfo = new byte[(int)infoLength];
			System.arraycopy(info, offset, finalInfo, 0, (int)infoLength);
			offset += infoLength;
			attributes[i] = new AttributeStructure(this.getParent(), attrName, finalInfo);
		}
	}

	/**
	 * Returns the parent method for this {@link CodeStructure}.
	 *
	 * @return The parent method for this {@link CodeStructure}
	 */
	public MethodInfo getMethod() {
		return this.method;
	}

	/**
	 * Returns the maximum depth of the stack during this
	 * {@link CodeStructure}'s execution.
	 *
	 * @return The maximum depth of the stack during this
	 * {@link CodeStructure}'s execution
	 */
	public int getMaxStackSize() {
		return this.stackSize;
	}

	/**
	 * Returns the maximum number of local variables during this
	 * {@link CodeStructure}'s execution.
	 *
	 * @return The maximum depth of local variables during this
	 * {@link CodeStructure}'s execution
	 */
	public int getMaxLocalSize() {
		return this.localSize;
	}

	/**
	 * Returns an array containing {@link Instruction}s defined by this
	 * {@link CodeStructure}.
	 *
	 * @return An array containing {@link Instruction}s defined by this
	 * {@link CodeStructure}.
	 */
	public Instruction[] getCode() {
		return this.code;
	}

	/**
	 * Returns an array containing this {@link CodeStructure}'s
	 * {@link ExceptionHandler}s.
	 *
	 * @return An array containing this {@link CodeStructure}'s
	 * {@link ExceptionHandler}s
	 */
	public ExceptionHandler[] getExceptionHandlers() {
		return this.exceptionHandlers;
	}

	/**
	 * Returns an array containing this {@link CodeStructure}'s
	 * defined attributes.
	 *
	 * @return An array containing this {@link CodeStructure}'s
	 * defined attributes
	 */
	public AttributeStructure[] getAttributes() {
		return this.attributes;
	}

}
