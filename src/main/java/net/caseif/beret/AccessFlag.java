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

import java.util.HashSet;
import java.util.Set;

public final class AccessFlag {

	private final Set<FlagType> flags;

	/**
	 * Constructs a new access flag from the given bitmask.
	 *
	 * @param first The first byte of the bitmask
	 * @param second The second byte of the bitmask
	 */
	public AccessFlag(byte first, byte second) {
		flags = new HashSet<>();
		for (FlagType ft : FlagType.values()) {
			if (ft.isFirst && (first & ft.mask) == ft.mask
					|| !ft.isFirst
					&& (second & ft.mask) == ft.mask) {
				flags.add(ft);
			}
		}
		//TODO: verify the flag combination is valid
	}

	/**
	 * Gets all access flags set by this {@link AccessFlag}.
	 *
	 * @return All access flags set by this {@link AccessFlag}
	 */
	public Set<FlagType> getFlags() {
		return this.flags;
	}

	/**
	 * Represents a specific access flag type.
	 */
	public enum FlagType {

		//TODO: document
		ACC_PUBLIC((byte)0x01, false),
		ACC_FINAL((byte)0x10, false),
		ACC_SUPER((byte)0x20, false),
		ACC_INTERFACE((byte)0x02, true),
		ACC_ABSTRACT((byte)0x04, true),
		ACC_SYNTHETIC((byte)0x10, true),
		ACC_ANNOTATION((byte)0x20, true),
		ACC_ENUM((byte)0x40, true);

		private byte mask;
		private boolean isFirst;

		FlagType(byte mask, boolean isFirst) {
			this.mask = mask;
			this.isFirst = isFirst;
		}

	}

}
