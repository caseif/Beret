package net.caseif.beret;

/**
 * Represents an instruction consisting of an opcode and any extra bytes.
 *
 * @author Max Roncacé
 */
public class Instruction {

	private Opcode opcode;
	private byte[] extra;

	public Instruction(Opcode opcode, byte... extraBytes) {
		assert opcode.getAdditionalBytes() == extraBytes.length;
		this.opcode = opcode;
		this.extra = extraBytes;
	}

	public Opcode getOpcode() {
		return this.opcode;
	}

	public byte[] getExtraBytes() {
		return this.extra;
	}

}
