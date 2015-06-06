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

public class TypeDescriptor {

    private static Map<String, String> primTypes = new HashMap<String, String>();

    private String handle;

    private boolean isClass;
    private int arrayDimensions;
    private String canonicalName;
    private String reqImport = null;

    static {
        primTypes.put("B", "byte");
        primTypes.put("C", "char");
        primTypes.put("D", "double");
        primTypes.put("F", "float");
        primTypes.put("I", "int");
        primTypes.put("J", "long");
        primTypes.put("S", "short");
        primTypes.put("V", "void");
        primTypes.put("Z", "boolean");

    }

    /**
     * Constructs a new descriptor object from the given string.
     *
     * @param str The string to construct a descriptor from
     */
    public TypeDescriptor(String str) {
        handle = str;
        arrayDimensions = str.length() - str.replaceAll("\\[", "").length();
        str = str.replaceAll("\\[", "");
        if (primTypes.containsKey(str)) {
            canonicalName = primTypes.get(str);
        } else if (str.startsWith("L") && str.endsWith(";")) {
            String className = str.substring(1, str.length() - 1);
            if (className.length() - className.replace("/", "").length() == 2
                    && className.startsWith("java/lang/")) {
                canonicalName = className.substring("java/lang/".length());
            } else {
                canonicalName = className.substring(className.lastIndexOf("/") + 1);
                reqImport = className.replaceAll("/", ".");
            }
        } else {
            throw new IllegalArgumentException("Invalid descriptor: " + str);
        }
    }

    public String getRequiredImport() {
        return this.reqImport;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(canonicalName);
        for (int i = 0; i < arrayDimensions; i++) {
            sb.append("[]");
        }
        return sb.toString();
    }

}
