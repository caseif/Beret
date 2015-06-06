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
 * Represents a <code>CONSTANT_MethodHandle_info</code> structure.
 */
public class MethodHandleStructure extends ConstantStructure {

    private ReferenceKind kind;
    private ConstantStructure ref;

    public MethodHandleStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.METHOD_HANDLE.getTag(), content);
        this.kind = ReferenceKind.fromMagicNumber(content[0]);
        this.ref = parent.getFromPool(content[1], content[2]);
    }

    public ReferenceKind getReferenceKind() {
        return kind;
    }

    public ConstantStructure getReference() {
        return ref;
    }

    public enum ReferenceKind {

        GET_FIELD(1, StructureType.FIELD_REF),
        GET_STATIC(2, StructureType.FIELD_REF),
        PUT_FIELD(3, StructureType.FIELD_REF),
        PUT_STATIC(4, StructureType.FIELD_REF),

        INVOKE_VIRTUAL(5, StructureType.METHOD_REF),
        NEW_INVOKE_SPECIAL(8, StructureType.METHOD_REF),

        // Java 8 allows the type to be METHOD_REF or INTERFACE_METHOD_REF; 7 and below only allows the former
        INVOKE_STATIC(6, StructureType.METHOD_REF),
        INVOKE_SPECIAL(7, StructureType.METHOD_REF);

        private static Map<Byte, ReferenceKind> kinds = new HashMap<Byte, ReferenceKind>();

        private byte magic;
        private StructureType type;

        ReferenceKind(int magic, StructureType requiredType) {
            this.magic = (byte)magic;
            this.type = requiredType;

            putKind();
        }

        void putKind() {
            kinds.put(magic, this);
        }

        public byte getMagicNumber() {
            return magic;
        }

        public StructureType getRequiredType() {
            return this.type;
        }

        public static ReferenceKind fromMagicNumber(byte magicNumber) {
            return kinds.get(magicNumber);
        }

    }

}
