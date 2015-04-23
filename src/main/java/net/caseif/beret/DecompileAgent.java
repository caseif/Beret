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

import net.caseif.beret.wrapper.ClassInfo;
import net.caseif.beret.wrapper.synthetic.AccessFlag;

/**
 * Agent for handling decompilation of raw class data to Java code.
 *
 * @author Max Roncacé
 */
public class DecompileAgent {

    private ClassInfo handle;

    /**
     * Consructs a new {@link DecompileAgent} from the given {@link ClassInfo}.
     *
     * @param classInfo The handle for the new {@link DecompileAgent}
     */
    public DecompileAgent(ClassInfo classInfo) {
        this.handle = classInfo;
    }

    /**
     * Prints a representation of this {@link DecompileAgent}'s class in Java
     * code.
     *
     * @return A String containing the decompiled code
     */
    public String decompile() {
        StringBuilder sb = new StringBuilder();
        sb.append("// generated by Beret <https://github.com/caseif/Beret>").append("\n");
        if (handle.getName().contains("/")) {
            sb.append("package ").append(getPackage()).append(";").append("\n\n");
        }
        sb.append(getClassDeclaration()).append("\n");
        sb.append("    ").append("//TODO: actually decompile the class file").append("\n");
        sb.append("}");
        sb.append("\n");
        return sb.toString();
    }

    public String getPackage() {
        if (handle.getName().contains("/")) {
            return handle.getName().substring(0, handle.getName().lastIndexOf("/")).replace("/", ".");
        }
        return "";
    }

    public String getClassDeclaration() {
        StringBuilder sb = new StringBuilder();
        for (AccessFlag.ClassFlag flag : AccessFlag.ClassFlag.values()) {
            if (flag.isPresentInSource() && handle.getAccessModifiers().getFlags().contains(flag)) {
                sb.append(flag.toString()).append(" ");
            }
        }
        if (!handle.getAccessModifiers().getFlags().contains(AccessFlag.ClassFlag.ACC_ENUM)
                && !handle.getAccessModifiers().getFlags().contains(AccessFlag.ClassFlag.ACC_INTERFACE)) {
            sb.append("class ");
        }
        sb.append(handle.getName().substring(getPackage().length())).append(" {");
        return sb.toString();
    }

}
