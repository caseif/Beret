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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Beret Extraordinary Reverse Engineering Toolkit.
 *
 * @author Max Roncacé
 * @version 1.0.0-SNAPSHOT
 */
public class Beret {

	public static void main(String[] args) {
		if (args.length < 2) {
			printUsage();
			System.exit(0);
		}
		String action = args[0];
		List<String> valid = Arrays.asList("dump", "decompile");
		if (!valid.contains(action.toLowerCase())) {
			System.err.println("Invalid command!");
			printUsage();
			System.exit(1);
		}
		File input = new File(args[1]);
		if (!input.exists()) {
			System.err.println("Invalid input file!");
			System.exit(1);
		}
		System.out.println("Reading from " + input.getAbsolutePath() + "...");
		ClassInfo cf = null;
		try {
			cf = new ClassInfo(new FileInputStream(input));
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Invalid input file!");
			System.exit(1);
		}
		OutputStream os;
		try {
			if (args.length > 2) {
				File output = new File(args[1]);
				System.out.println("Writing to " + output.getAbsolutePath() + "...");
				os = new FileOutputStream(output);
			} else {
				os = System.out;
			}
			if (action.equalsIgnoreCase("dump")) {
				cf.dump(os);
			} else if (action.equalsIgnoreCase("decompile")) {
				System.err.println("Not yet implemented!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Failed to write to output stream!");
			System.exit(1);
		}
	}

	public static void printUsage() {
		System.out.println("Usage: Beret.jar <command> <class file> [<output file>]");
		System.out.println("Available commands:");
		System.out.println("    dump - Dumps info about a class in an arbitrary format");
		System.out.println("    decompile - Decompiles a class into its original source code");
	}

}
