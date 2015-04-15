package net.caseif.beret.structures;

import net.caseif.beret.ClassInfo;
import net.caseif.beret.ExceptionHandler;
import net.caseif.beret.Instruction;
import net.caseif.beret.MethodInfo;
import net.caseif.beret.Opcode;
import net.caseif.beret.Util;

/**
 * Represents a Code attribute in a method.
 *
 * @author Max Roncacé
 */
public class CodeStructure extends AttributeStructure {

	/**
	 * The number of bytes preceding the instruction array in a code attribute.
	 */
	private static final int HEADER_LENGTH = 8;

	private MethodInfo method;
	private int stackSize;
	private int localSize;
	private Instruction[] code;
	private ExceptionHandler[] exceptionHandlers;
	private AttributeStructure[] attributes;

	/**
	 * Creates a new {@link CodeStructure} with the given name and content.
	 *
	 * @param parent The parent {@link ClassInfo} instance
	 * @param name The name of the new {@link CodeStructure}
	 * @param info The content of the new {@link CodeStructure}
	 */
	public CodeStructure(MethodInfo parent, String name, byte[] info) {
		super(parent.getParent(), name, info);
		this.method = parent;
		this.stackSize = Util.bytesToUshort(info[0], info[1]);
		this.localSize = Util.bytesToUshort(info[2], info[3]);
		long codeSize = Util.bytesToUint(info[4], info[5], info[6], info[7]);
		if (codeSize > Integer.MAX_VALUE) {
			throw new UnsupportedOperationException("Code attribute is too long");
		}
		code = new Instruction[(int)codeSize];
		for (int i = 0; i < codeSize; i++) {
			Opcode opcode = Opcode.fromByte(info[i + HEADER_LENGTH]);
			int extra = opcode.getAdditionalBytes();
			if (extra == -1) {
				throw new UnsupportedOperationException("Unsupported opcode: " + opcode.toString()); //TODO
			}
			byte[] extraBytes = new byte[extra];
			System.arraycopy(info, i + HEADER_LENGTH, extraBytes, 0, extra);
			code[i] = new Instruction(opcode, extraBytes);
		}
		int offset = HEADER_LENGTH + code.length;
		int exceptionTableLength = Util.bytesToUshort(info[offset], info[offset + 1]);
		offset += 2;
		exceptionHandlers = new ExceptionHandler[exceptionTableLength];
		for (int i = 0; i < exceptionTableLength; i++) {
			int startIndex = Util.bytesToUshort(info[offset], info[offset + 1]);
			int endIndex = Util.bytesToUshort(info[offset], info[offset + 1]);
			int handlerStartIndex = Util.bytesToUshort(info[offset], info[offset + 1]);
			int catchType = Util.bytesToUshort(info[offset], info[offset + 1]);
			byte[] classRef = getParent().getConstantPool()[catchType].getInfo();
			int catchTypeRef = Util.bytesToUshort(classRef[0], classRef[1]);
			String catchTypeName = Util.asUtf8(getParent().getConstantPool()[catchTypeRef].getInfo());
			exceptionHandlers[i] = new ExceptionHandler(
					method, startIndex, endIndex, handlerStartIndex, catchTypeName
			);
			offset += 8;
		}
		int attributeCount = Util.bytesToUshort(info[offset], info[offset + 1]);
		offset += 2;
		attributes = new AttributeStructure[attributeCount];
		for (int i = 0; i < attributeCount; i++) {
			int namePointer = Util.bytesToUshort(info[offset], info[offset + 1]);
			ConstantStructure nameStruct = getParent().getConstantPool()[namePointer - 1];
			if (nameStruct.getType() != ConstantStructure.StructureType.UTF_8) {
				throw new IllegalArgumentException("Attribute name index does not point to a UTF-8 structure");
			}
			String attrName = Util.asUtf8(nameStruct.getInfo());
			offset += 2;
			//TODO: add support for long arrays
			long infoLength = Util.bytesToUint(info[offset], info[offset + 1],
					info[offset + 2], info[offset + 3]);
			if (infoLength > Integer.MAX_VALUE) {
				throw new UnsupportedOperationException("Attribute is too long");
			}
			offset += 4;
			byte[] finalInfo = new byte[(int)infoLength];
			System.arraycopy(info, offset, finalInfo, 0, (int)infoLength);
			offset += infoLength;
			attributes[i] = new AttributeStructure(this.getParent(), attrName, finalInfo);
		}
	}

	/**
	 * Returns the parent method for this {@link CodeStructure}.
	 *
	 * @return The parent method for this {@link CodeStructure}
	 */
	public MethodInfo getMethod() {
		return this.method;
	}

	/**
	 * Returns the maximum depth of the stack during this
	 * {@link CodeStructure}'s execution.
	 *
	 * @return The maximum depth of the stack during this
	 * {@link CodeStructure}'s execution
	 */
	public int getMaxStackSize() {
		return this.stackSize;
	}

	/**
	 * Returns the maximum number of local variables during this
	 * {@link CodeStructure}'s execution.
	 *
	 * @return The maximum depth of local variables during this
	 * {@link CodeStructure}'s execution
	 */
	public int getMaxLocalSize() {
		return this.localSize;
	}

	/**
	 * Returns an array containing {@link Instruction}s defined by this
	 * {@link CodeStructure}.
	 *
	 * @return An array containing {@link Instruction}s defined by this
	 * {@link CodeStructure}.
	 */
	public Instruction[] getCode() {
		return this.code;
	}

	/**
	 * Returns an array containing this {@link CodeStructure}'s
	 * {@link ExceptionHandler}s.
	 *
	 * @return An array containing this {@link CodeStructure}'s
	 * {@link ExceptionHandler}s
	 */
	public ExceptionHandler[] getExceptionHandlers() {
		return this.exceptionHandlers;
	}

	/**
	 * Returns an array containing this {@link CodeStructure}'s
	 * defined attributes.
	 *
	 * @return An array containing this {@link CodeStructure}'s
	 * defined attributes
	 */
	public AttributeStructure[] getAttributes() {
		return this.attributes;
	}

}
