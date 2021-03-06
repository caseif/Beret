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
package net.caseif.beret.wrapper.synthetic;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a flag containing one or more access modifiers.
 *
 * @author Max Roncacé
 */
public final class AccessFlag {

    private final AccessTarget target;
    private final Set<Enum> flags;

    /**
     * Constructs a new access flag from the given bitmask.
     *
     * @param target The item type this flag applies to
     * @param first  The first byte of the bitmask
     * @param second The second byte of the bitmask
     */
    public AccessFlag(AccessTarget target, byte first, byte second) {
        this.target = target;
        flags = new HashSet<>();
        if (target == AccessTarget.CLASS) {
            for (ClassFlag ft : ClassFlag.values()) {
                if (ft.isFirst && (first & ft.mask) == ft.mask
                        || !ft.isFirst && (second & ft.mask) == ft.mask) {
                    flags.add(ft);
                }
            }
        } else if (target == AccessTarget.FIELD) {
            for (FieldFlag ft : FieldFlag.values()) {
                if (ft.isFirst && (first & ft.mask) == ft.mask
                        || !ft.isFirst && (second & ft.mask) == ft.mask) {
                    flags.add(ft);
                }
            }
        } else if (target == AccessTarget.METHOD) {
            for (MethodFlag ft : MethodFlag.values()) {
                if (ft.isFirst && (first & ft.mask) == ft.mask
                        || !ft.isFirst && (second & ft.mask) == ft.mask) {
                    flags.add(ft);
                }
            }
        }
        //TODO: verify the flag combination is valid
    }

    /**
     * Gets the structure type which these flags apply to.
     *
     * @return The structure type which these flags apply to
     */
    public AccessTarget getTargetType() {
        return this.target;
    }

    /**
     * Gets all access flags set by this {@link AccessFlag}.
     *
     * @return All access flags set by this {@link AccessFlag}
     */
    public Set<? extends Enum> getFlags() {
        return this.flags;
    }

    /**
     * Represents a construct which may have access flags applied to it.
     */
    public enum AccessTarget {
        CLASS,
        FIELD,
        METHOD
    }

    /**
     * Represents a flag applying to a class.
     */
    public enum ClassFlag {

        //TODO: document
        ACC_PUBLIC((byte)0x01, false, true),
        ACC_ABSTRACT((byte)0x04, true, true),
        ACC_FINAL((byte)0x10, false, true),
        ACC_INTERFACE((byte)0x02, true, true),
        ACC_ENUM((byte)0x40, true, true),
        ACC_SUPER((byte)0x20, false, false),
        ACC_SYNTHETIC((byte)0x10, true, false),
        ACC_ANNOTATION((byte)0x20, true, false);

        private byte mask;
        private boolean isFirst;
        private boolean inSource;

        ClassFlag(byte mask, boolean isFirst, boolean inSource) {
            this.mask = mask;
            this.isFirst = isFirst;
            this.inSource = inSource;
        }

        public boolean isPresentInSource() {
            return inSource;
        }

        @Override
        public String toString() {
            return this.name().substring(4).toLowerCase();
        }

    }

    /**
     * Represents a flag applying to a field.
     */
    public enum FieldFlag {

        //TODO: document
        ACC_PUBLIC((byte)0x01, false, true),
        ACC_PRIVATE((byte)0x02, false, true),
        ACC_PROTECTED((byte)0x04, false, true),
        ACC_STATIC((byte)0x08, false, true),
        ACC_VOLATILE((byte)0x40, false, true),
        ACC_TRANSIENT((byte)0x80, false, true),
        ACC_FINAL((byte)0x10, false, true),
        ACC_ENUM((byte)0x40, true, false),
        ACC_SYNTHETIC((byte)0x10, true, false);

        private byte mask;
        private boolean isFirst;
        private boolean inSource;

        FieldFlag(byte mask, boolean isFirst, boolean inSource) {
            this.mask = mask;
            this.isFirst = isFirst;
            this.inSource = inSource;
        }

        public boolean isPresentInSource() {
            return inSource;
        }

        @Override
        public String toString() {
            return this.name().substring(4).toLowerCase();
        }

    }

    /**
     * Represents a flag applying to a method.
     */
    public enum MethodFlag {

        //TODO: document
        ACC_PUBLIC((byte)0x01, false, true),
        ACC_PRIVATE((byte)0x02, false, true),
        ACC_PROTECTED((byte)0x04, false, true),
        ACC_STATIC((byte)0x08, false, true),
        ACC_ABSTRACT((byte)0x04, true, true),
        ACC_SYNCHRONIZED((byte)0x20, false, true),
        ACC_FINAL((byte)0x10, false, true),
        ACC_STRICT((byte)0x08, true, true),
        ACC_NATIVE((byte)0x01, true, true),
        ACC_BRIDGE((byte)0x40, false, false),
        ACC_VARARGS((byte)0x80, false, false),
        ACC_SYNTHETIC((byte)0x10, true, false);

        private byte mask;
        private boolean isFirst;
        private boolean inSource;

        MethodFlag(byte mask, boolean isFirst, boolean inSource) {
            this.mask = mask;
            this.isFirst = isFirst;
            this.inSource = inSource;
        }

        public boolean isPresentInSource() {
            return inSource;
        }

        @Override
        public String toString() {
            return this.name().substring(4).toLowerCase();
        }

    }

}
