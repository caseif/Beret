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

import static net.caseif.beret.AccessFlag.ClassFlag.ACC_ABSTRACT;
import static net.caseif.beret.AccessFlag.ClassFlag.ACC_ANNOTATION;
import static net.caseif.beret.AccessFlag.ClassFlag.ACC_ENUM;
import static net.caseif.beret.AccessFlag.ClassFlag.ACC_FINAL;
import static net.caseif.beret.AccessFlag.ClassFlag.ACC_INTERFACE;
import static net.caseif.beret.AccessFlag.ClassFlag.ACC_PUBLIC;
import static net.caseif.beret.AccessFlag.ClassFlag.ACC_SUPER;
import static net.caseif.beret.AccessFlag.ClassFlag.ACC_SYNTHETIC;

import org.junit.Test;

import java.util.Arrays;

public class AccessFlagTest {

	@Test
	public void testFlags() {
		testFlag((byte)0x02, (byte)0x10, ACC_INTERFACE, ACC_FINAL);
		testFlag((byte)0x12, (byte)0x10, ACC_SYNTHETIC, ACC_INTERFACE, ACC_FINAL);
		testFlag((byte)0x12, (byte)0x11, ACC_SYNTHETIC, ACC_INTERFACE, ACC_FINAL, ACC_PUBLIC);
		testFlag((byte)0x12, (byte)0x21, ACC_SYNTHETIC, ACC_INTERFACE, ACC_SUPER, ACC_PUBLIC);
		testFlag((byte)0x24, (byte)0x10, ACC_ANNOTATION, ACC_ABSTRACT, ACC_FINAL);
		testFlag((byte)0x44, (byte)0x10, ACC_ENUM, ACC_ABSTRACT, ACC_FINAL);
	}

	private void testFlag(byte first, byte second, AccessFlag.ClassFlag... expected) {
		AccessFlag flag = new AccessFlag(AccessFlag.AccessTarget.CLASS, first, second);
		assert flag.getFlags().containsAll(Arrays.asList(expected)); // check it has all the flags we expect
		assert Arrays.asList(expected).containsAll(flag.getFlags()); // check it doesn't have any unexpected flags
	}

}
