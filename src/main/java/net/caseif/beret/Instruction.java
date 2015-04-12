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

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration of all Java bytecode instructions.
 *
 * @author Max Roncacé
 */
public enum Instruction {

	/**
	 * Loads an references from an array onto the stack.
	 *
	 * <p>Stack: <code>arrayref, index -> value</code></p>
	 */
	AALOAD(0x32, 0),
	/**
	 * Stores a reference in an array.
	 *
	 * <p>Stack: <code>arrayref, index, value -> ()</code></p>
	 */
	AASTORE(0x53, 0),
	/**
	 * Pushes a <code>null</code> references onto the stack.
	 *
	 * <p>Stack: <code>() -> null</code></p>
	 */
	ACONST_NULL(0x01, 0),
	/**
	 * Loads a reference onto the stack from local variable <code>index</code>.
	 *
	 * <p>Accepts: <code>byte index</code></p>
	 * <p>Stack: <code>() -> objectref</code></p>
	 */
	ALOAD(0x19, 1),
	/**
	 * Loads a reference onto the stack from local variable <code>0</code>.
	 *
	 * <p>Stack: <code>() -> objectref</code></p>
	 */
	ALOAD_0(0x2A, 0),
	/**
	 * Loads a reference onto the stack from local variable <code>1</code>.
	 *
	 * <p>Stack: <code>() -> objectref</code></p>
	 */
	ALOAD_1(0x2B, 0),
	/**
	 * Loads a reference onto the stack from local variable <code>2</code>.
	 *
	 * <p>Stack: <code>() -> objectref</code></p>
	 */
	ALOAD_2(0x2C, 0),
	/**
	 * Loads a reference onto the stack from local variable <code>3</code>.
	 *
	 * <p>Stack: <code>() -> objectref</code></p>
	 */
	ALOAD_3(0x2D, 0),
	/**
	 * Creates a new array of length <code>count</code> and type
	 * <code>typeref</code>.
	 *
	 * <p>Accepts: <code>short typeref</code></p>
	 * <p>Stack: <code>count -> arrayref</code></p>
	 */
	ANEWARRAY(0xBD, 2),
	/**
	 * Returns a reference from a method and clears the stack.
	 *
	 * <p>Stack: objectref -> [empty]</p>
	 */
	ARETURN(0xB0, 0),
	/**
	 * Replaces <code>arrayref</code> on the top of the stack with its length.
	 *
	 * <p>Stack: <code>arrayref -> length</code></p>
	 */
	ARRAYLENGTH(0xBE, 0),
	/**
	 * Stores a reference into local variable <code>index</code>.
	 *
	 * <p>Accepts: <code>byte index</code></p>
	 * <p>Stack: <code>objectref -> ()</code></p>
	 */
	ASTORE(0x3A, 1),
	/**
	 * Stores a references into local variable <code>0</code>.
	 *
	 * <p>Stack: <code>objectref -> ()</code></p>
	 */
	ASTORE_0(0x4B, 0),
	/**
	 * Stores a references into local variable <code>1</code>.
	 *
	 * <p>Stack: <code>objectref -> ()</code></p>
	 */
	ASTORE_1(0x4C, 0),
	/**
	 * Stores a references into local variable <code>2</code>.
	 *
	 * <p>Stack: <code>objectref -> ()</code></p>
	 */
	ASTORE_2(0x4D, 0),
	/**
	 * Stores a references into local variable <code>3</code>.
	 *
	 * <p>Stack: <code>objectref -> ()</code></p>
	 */
	ASTORE_3(0x4E, 0),
	/**
	 * Throws an error or exception and clears the stack save the reference to
	 * the Throwable at the top.
	 *
	 * <p>Stack: <code>objectref -> [empty], objectref</code></p>
	 */
	ATHROW(0xBF, 0),
	/**
	 * Loads a byte or boolean value from an array.
	 *
	 * <p>Stack: <code>arrayref, index -> value</code></p>
	 */
	BALOAD(0x33, 0),
	/**
	 * Stores a byte or boolean value into an array.
	 *
	 * <p>Stack: <code>arrayref, index, value</code></p>
	 */
	BASTORE(0x54, 0),
	/**
	 * Pushes a byte onto the stack as an integer value.
	 *
	 * <p>Accepts: <code>byte value</code></p>
	 * <p>Stack: <code>() -> value</code></p>
	 */
	BIPUSH(0x10, 1),
	/**
	 * Reserved for breakpoints in Java debuggers; should not appear in a class
	 * file.
	 */
	BREAKPOINT(0xCA, 0),
	/**
	 * Loads a char from an array.
	 *
	 * <p>Stack: <code>arrayref, index -> value</code></p>
	 */
	CALOAD(0x34, 0),
	/**
	 * Stores a char into an array.
	 *
	 * <p>Stack: <code>arrayref, index, value -> ()</code></p>
	 */
	CASTORE(0x55, 0),
	/**
	 * Checks whether <code>objectref</code> matches of the type of the class at
	 * <code>index</code> of the constant pool.
	 *
	 * <p>Accepts: <code>short index</code></p>
	 * <p>Stack: <code>objectref -> objectref</code></p>
	 */
	CHECKCAST(0xC, 2),
	/**
	 * Converts a double to a float.
	 *
	 * <p>Stack: <code>value -> result</code></p>
	 */
	D2F(0x90, 0),
	/**
	 * Converts a double to an integer.
	 *
	 * <p>Stack: <code>value -> result</code></p>
	 */
	D2I(0x8E, 0),
	/**
	 * Converts a double to an long.
	 *
	 * <p>Stack: <code>value -> result</code></p>
	 */
	D2L(0x8F, 0),
	/**
	 * Adds two doubles.
	 *
	 * <p>Stack: <code>value1, value2 -> result</code></p>
	 */
	DADD(0x63, 0),
	/**
	 * Loads a double from an array.
	 *
	 * <p>Stack: <code>arrayref, index -> value</code></p>
	 */
	DALOAD(0x31, 0),
	/**
	 * Stores a double into an array.
	 *
	 * <p>Stack: <code>arrayref, index, value -> ()</code></p>
	 */
	DASTORE(0x52, 0),
	/**
	 * Checks whether double <code>value1</code> is greater than double
	 * <code>value2</code>.
	 *
	 * <p>Stack: <code>value1, value2 -> result</code></p>
	 */
	DCMPG(0x98, 0),
	/**
	 * Checks whether double <code>value1</code> is less than double
	 * <code>value2</code>
	 *
	 * <p>Stack: <code>value1, value2 -> result</code></p>
	 */
	DCMPL(0x97, 0),
	/**
	 * Pushes the double constant <code>0.0</code> onto the stack.
	 *
	 * <p>Stack: <code>() -> 0.0</code></p>
	 */
	DCONST_0(0x0E, 0),
	/**
	 * Pushes the double constant <code>1.0</code> onto the stack.
	 *
	 * <p>Stack: <code>() -> 1.0</code></p>
	 */
	DCONST_1(0x0F, 0),
	/**
	 * Divides two doubles.
	 *
	 * <p>Stack: <code>value1, value2 -> result</code></p>
	 */
	DDIV(0x6F, 0),
	/**
	 * Loads a double from local variable <code>index</code>.
	 *
	 * <p>Accepts: <code>byte index</code></p>
	 * <p>Stack: <code>() -> value</code></p>
	 */
	DLOAD(0x18, 1),
	/**
	 * Loads a double from local variable <code>0</code>.
	 *
	 * <p>Stack: <code>() -> value</code></p>
	 */
	DLOAD_0(0x26, 0),
	/**
	 * Loads a double from local variable <code>1</code>.
	 *
	 * <p>Stack: <code>() -> value</code></p>
	 */
	DLOAD_1(0x27, 0),
	/**
	 * Loads a double from local variable <code>2</code>.
	 *
	 * <p>Stack: <code>() -> value</code></p>
	 */
	DLOAD_2(0x28, 0),
	/**
	 * Loads a double from local variable <code>3</code>.
	 *
	 * <p>Stack: <code>() -> value</code></p>
	 */
	DLOAD_3(0x29, 0),
	/**
	 * Multiplies two doubles.
	 *
	 * <p>Stack: <code>value1, value2 -> result</code></p>
	 */
	DMUL(0x6B, 0),
	/**
	 * Negates a double.
	 *
	 * <p>Stack: <code>value -> result</code></p>
	 */
	DNEG(0x77, 0),
	/**
	 * Gets the remainder from division between two doubles (modulus).
	 *
	 * <p>Stack: <code>value1, value2 -> result</code></p>
	 */
	DREM(0x73, 0),
	/**
	 * Returns a double from a method and clears the stack.
	 *
	 * <p>Stack: <code>value -> [empty]</code></p>
	 */
	DRETURN(0xAF, 0),
	/**
	 * Stores double <code>value</code> into local variable <code>index</code>.
	 *
	 * <p>Stack: <code>value -> ()</code></p>
	 */
	DSTORE(0x39, 0),
	/**
	 * Stores double <code>value</code> into local variable <code>0</code>.
	 *
	 * <p>Stack: <code>value -> ()</code></p>
	 */
	DSTORE_0(0x47, 0),
	/**
	 * Stores double <code>value</code> into local variable <code>1</code>.
	 *
	 * <p>Stack: <code>value -> ()</code></p>
	 */
	DSTORE_1(0x48, 0),
	/**
	 * Stores double <code>value</code> into local variable <code>2</code>.
	 *
	 * <p>Stack: <code>value -> ()</code></p>
	 */
	DSTORE_2(0x49, 0),
	/**
	 * Stores double <code>value</code> into local variable <code>3</code>.
	 *
	 * <p>Stack: <code>value -> ()</code></p>
	 */
	DSTORE_3(0x4A, 0),
	/**
	 * Subtracts a double from another.
	 *
	 * <p>Stack: <code>value1, value2 -> result</code></p>
	 */
	DSUB(0x67, 0),
	/**
	 * Duplicates the value on top of the stack.
	 *
	 * <p>Stack: <code>value -> value, value</code></p>
	 */
	DUP(0x59, 0),
	/**
	 * Inserts a copy of the top value of the stack two values from the top.
	 *
	 * <p>Precondition: The top two values of the stack must not be of type
	 * double or long</p>
	 * <p>Stack: <code>value1, value2 -> value2, value1, value2</code></p>
	 */
	DUP_X1(0x5A, 0),
	/**
	 * Inserts a copy of the top value of the stack two (if <code>value2</code>
	 * is double or long, in which case it occupies the entry of
	 * <code>value3</code>) or three (if <code>value2</code> is neither a double
	 * or long) values from the top.
	 *
	 * <p>Stack: <code>value1, value2, value3 -> value3, value1, value2, value3
	 * </code></p>
	 */
	DUP_X2(0x5B, 0),
	/**
	 * Duplicates the top two stack words (a word being two entries or 8 bytes).
	 *
	 * <p>Stack: <code>value1, value2 -> value1, value2, value1, value2</code>
	 * </p>
	 */
	DUP2(0x5C, 0),
	/**
	 * Duplicates the top two stack words and inserts them beneath the third
	 * word.
	 *
	 * <p>Stack: <code>value1, value2, value3 -> value2, value3, value1, value2,
	 * value3</code></p>
	 */
	DUP2_X1(0x5D, 0),
	/**
	 * Duplicates the top two stack words and inserts them beneath the fourth
	 * word.
	 *
	 * <p>Stack: <code>value1, value2, value3, value4 -> value3, value4, value1,
	 * value2, value3, value4</code></p>
	 */
	DUP2_X2(0x5E, 0),
	F2D(0x8D, 0),
	F2I(0x8B, 0),
	F2L(0x8C, 0),
	FADD(0x62, 0),
	FALOAD(0x30, 0),
	FASTORE(0x51, 0),
	FCMPG(0x96, 0),
	FCMPL(0x95, 0),
	FCONST_0(0x0B, 0),
	FCONST_1(0x0C, 0),
	FCONST_2(0x0D, 0),
	FDIV(0x6E, 0),
	FLOAD(0x17, 1),
	FLOAD_0(0x22, 0),
	FLOAD_1(0x23, 0),
	FLOAD_2(0x24, 0),
	FLOAD_3(0x25, 0),
	FMUL(0x6A, 0),
	FNEG(0x76, 0),
	FREM(0x72, 0),
	FRETURN(0xAE, 0),
	FSTORE(0x38, 1),
	FSTORE_0(0x43, 0),
	FSTORE_1(0x44, 0),
	FSTORE_2(0x45, 0),
	FSUB(0x66, 0),
	GETFIELD(0xB4, 2),
	GETSTATIC(0xB2, 2),
	GOTO(0xA7, 2),
	GOTO_W(0xC8, 0),
	I2B(0x91, 0),
	I2C(0x92, 0),
	I2D(0x87, 0),
	I2F(0x86, 0),
	I2L(0x85, 0),
	I2S(0x93, 0),
	IADD(0x60, 0),
	IALOAD(0x2E, 0),
	IAND(0x7E, 0),
	IASTORE(0x41, 0),
	ICONST_M1(0x02, 0),
	ICONST_0(0x03, 0),
	ICONST_1(0x04, 0),
	ICONST_2(0x05, 0),
	ICONST_3(0x06, 0),
	ICONST_4(0x07, 0),
	ICONST_5(0x08, 0),
	IDIV(0x6C, 0),
	IF_ACMPEQ(0xA5, 2),
	IF_ACMPNE(0xA6, 2),
	IF_ICMPEQ(0x9F, 2),
	IF_ICMPGE(0xA2, 2),
	IF_ICMPGT(0xA3, 2),
	IF_ICMPLE(0xA4, 2),
	IF_ICMPLT(0xA1, 2),
	IF_ICMPNE(0xA0, 2),
	IFEQ(0x99, 2),
	IFGE(0x9C, 2),
	IFGT(0x9D, 2),
	IFLE(0x9E, 2),
	IFLT(0x9B, 2),
	IFNE(0x9A, 2),
	IFNONNULL(0xC7, 2),
	IFNULL(0xC6, 2),
	IINC(0x84, 2),
	ILOAD(0x15, 1),
	ILOAD_0(0x1A, 0),
	ILOAD_1(0x1B, 0),
	ILOAD_2(0x1C, 0),
	ILOAD_3(0x1D, 0),
	IMPDEP1(0xFE, 0),
	IMPDEP2(0xFF, 0),
	IMUL(0x68, 0),
	INEG(0x74, 0),
	INSTANCEOF(0xC1, 2),
	INVOKEDYNAMIC(0xBA, 4),
	INVOKEINTERFACE(0xB9, 4),
	INVOKESPECIAL(0xB7, 2),
	INVOKESTATIC(0xB8, 2),
	INVOKEVIRTUAL(0xB6, 2),
	IOR(0x80, 0),
	IREM(0x70, 0),
	IRETURN(0xAC, 0),
	ISHL(0x78, 0),
	ISTORE(0x36, 1),
	ISTORE_0(0x3B, 0),
	ISTORE_1(0x3C, 0),
	ISTORE_2(0x3D, 0),
	ISTORE_3(0x3E, 0),
	ISUB(0x64, 0),
	IUSHR(0x7C, 0),
	IXOR(0x82, 0),
	JSR(0xA8, 2),
	JSR_W(0xC9, 4),
	L2D(0x8A, 0),
	L2F(0x89, 0),
	L2I(0x88, 0),
	LADD(0x61, 0),
	LALOAD(0x2F, 0),
	LAND(0x7F, 0),
	LASTORE(0x50, 0),
	LCMP(0x94, 0),
	LCONST_0(0x09, 0),
	LCONST_1(0x0A, 0),
	LDC(0x12, 1),
	LDC_W(0x13, 2),
	LDC2_W(0x14, 2),
	LDIV(0x6D, 0),
	LLOAD(0x16, 1),
	LLOAD_0(0x1E, 0),
	LLOAD_1(0x1F, 0),
	LLOAD_2(0x20, 0),
	LLOAD_3(0x21, 0),
	LMUL(0x69, 0),
	LNEG(0x75, 0),
	LOOKUPSWITCH(0xAB, -1),
	LOR(0x81, 0),
	LREM(0x71, 0),
	LRETURN(0xAD, 0),
	LSHL(0x79, 0),
	LSHR(0x7B, 0),
	LSTORE(0x37, 1),
	LSTORE_0(0x3F, 0),
	LSTORE_1(0x40, 0),
	LSTORE_2(0x41, 0),
	LSTORE_3(0x42, 0),
	LSUB(0x65, 0),
	LUSHR(0x7D, 0),
	LXOR(0x83, 0),
	MONITORENTER(0xC2, 0),
	MONITOREXIT(0xC3, 0),
	MULTIANEWARRAY(0xC5, 3),
	NEW(0xBB, 2),
	NEWARRAY(0xBC, 1),
	NOP(0x00, 0),
	POP(0x57, 0),
	POP2(0x58, 0),
	PUTFIELD(0xB5, 2),
	PUTSTATIC(0xB3, 2),
	RET(0xA9, 1),
	RETURN(0xB1, 0),
	SALOAD(0x35, 0),
	SASTORE(0x56, 0),
	SIPUSH(0x11, 2),
	SWAP(0x5F, 0),
	TABLESWITCH(0xAA, -1),
	WIDE(0xC4, -1);

	private static Map<Byte, Instruction> codes;

	private byte byteCode;
	private int extra;

	/**
	 * Constructs a new {@link Instruction} with the given byte.
	 *
	 * @param byteCode The byte associated with this {@link Instruction}
	 * @param additionalBytes The number of additional bytes following this
	 *                        instruction (pass <code>-1</code> to indicate a
	 *                        variable number, implying this instruction must be
	 *                        handled with a special case)
	 */
	Instruction(byte byteCode, int additionalBytes) {
		this.byteCode = byteCode;
		this.extra = additionalBytes;
		register(byteCode);
	}

	/**
	 * Constructs a new {@link Instruction} with the given byte.
	 *
	 * @param byteCode The byte associated with this {@link Instruction}
	 * @param additionalBytes The number of additional bytes following this
	 *                        instruction (pass <code>-1</code> to indicate a
	 *                        variable number, implying this instruction must be
	 *                        handled with a special case)
	 */
	private Instruction(int byteCode, int additionalBytes) {
		this((byte)byteCode, additionalBytes);
	}

	/**
	 * Registers this {@link Instruction} with the given byte tag.
	 *
	 * @param code The byte tag to associate with this {@link Instruction}.
	 */
	private void register(byte code) {
		if (codes == null) {
			codes = new HashMap<>();
		}
		codes.put(code, this);
	}

	/**
	 * Returns the byte associated with this {@link Instruction}.
	 *
	 * @return The <code>byte</code> representing this {@link Instruction}.
	 */
	public byte getByteCode() {
		return this.byteCode;
	}

	/**
	 * Returns the number of additional bytes which follow this
	 * {@link Instruction}.
	 * @return The number of additional bytes which follow this
	 *         {@link Instruction}
	 */
	public int getAdditionalBytes() {
		return this.extra;
	}

	public static Instruction fromByte(byte byteCode) {
		return codes.get(byteCode);
	}

}
