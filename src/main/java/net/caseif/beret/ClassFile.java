package net.caseif.beret;

import net.caseif.beret.structures.field.ConstantStructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassFile {

	final private byte[] bytes;

	private short majorVersion;
	private short minorVersion;

	private ConstantStructure[] constantPool;

	/**
	 * Loads a class file from the given {@link InputStream}.
	 * @param stream The stream to load the class file from
	 * @throws IllegalStateException If the stream does not begin with the
	 *                               standard magic number
	 * @throws IOException If an exception occurs while reading the stream
	 */
	public ClassFile(InputStream stream) throws IllegalStateException, IOException {
		int b;
		List<Byte> byteList = new ArrayList<Byte>();
		while ((b = stream.read()) != -1) {
			byteList.add((byte)b);
		}
		bytes = new byte[byteList.size()];
		int i = 0;
		for (Byte wrapped : byteList) {
			bytes[i] = wrapped;
			++i;
		}
		if (!checkMagicNumber()) {
			throw new IllegalStateException("Bytecode does not begin with standard magic number!");
		}
		parseVersion();
		loadConstantPool();
	}

	/**
	 * Checks the magic number at the top of the loaded bytecode.
	 *
	 * @return <code>true</code> if the top four bytes equal 0xCAFEBABE,
	 *         otherwise, <code>false</code>.
	 */
	private boolean checkMagicNumber() {
		// grab and check the first four bytes to ensure compliance with the format standard
		return bytes[0] == (byte)0xCA && bytes[1] == (byte)0xFE && bytes[2] == (byte)0xBA && bytes[3] == (byte)0xBE;
	}

	/**
	 * Parses the major/minor version from the loaded bytecode.
	 */
	private void parseVersion() {
		this.minorVersion = Util.bytesToShort(bytes[4], bytes[5]); // parse two bytes into a short
		this.majorVersion = Util.bytesToShort(bytes[6], bytes[7]);
	}

	/**
	 * Loads the constant pool from the loaded bytecode.
	 */
	private void loadConstantPool() {
		short poolSize = Util.bytesToShort(bytes[8], bytes[9]); // parse two bytes into a short
		poolSize -= 1; // the pool size is defined as one larger than it actually is
		constantPool = new ConstantStructure[poolSize];
		int offset = 10;
		for (int i = 0; i < poolSize; i++) {
			byte tag = bytes[offset]; // get the tag of the current structure
			++offset; // move the offset to the content start
			ConstantStructure struct;
			if (ConstantStructure.StructureType.fromTag(tag) == ConstantStructure.StructureType.UTF_8) {
				short length = Util.bytesToShort(bytes[offset], bytes[offset + 1]); // get defined length
				offset += 2; // move offset past length indicator
				struct = new ConstantStructure(tag, length);
			}
			else {
				struct = new ConstantStructure(tag);
			}
			int length = struct.getLength(); // get expected length
			byte[] content = new byte[length]; // create empty array of expected size
			for (int j = 0; j < length; j++) {
				content[j] = bytes[offset + j]; // populate the array with a subset of the class content
			}
			struct.setInfo(content); // set the info of the current structure
			offset += length; // move the offset to the next structure
			constantPool[i] = struct; // globally store the loaded structure
		}
	}

}
