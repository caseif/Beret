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

import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a structure containing an attribute (a name and raw byte data).
 *
 * @author Max Roncacé
 */
public class AttributeStructure {

	private ClassInfo parent;
	private String name;
	private byte[] content;

	/**
	 * Loads an {@link AttributeStructure}.
	 *
	 * @param parent The parent {@link ClassInfo} instance
	 * @param name The name of this {@link AttributeStructure}
	 * @param info The raw content of this {@link AttributeStructure}
	 */
	public AttributeStructure(ClassInfo parent, String name, byte[] content) {
		this.parent = parent;
		this.name = name;
		this.content = content;
	}

	/**
	 * Returns the parent {@link ClassInfo} instance.
	 * @return The parent {@link ClassInfo} instance
	 */
	public ClassInfo getParent() {
		return this.parent;
	}

	/**
	 * Gets the name of this {@link AttributeStructure}.
	 *
	 * @return The name of this {@link AttributeStructure}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the raw data of this {@link AttributeStructure}.
	 *
	 * @return The raw data of this {@link AttributeStructure}
	 */
	public byte[] getContent() {
		return this.content;
	}

}
