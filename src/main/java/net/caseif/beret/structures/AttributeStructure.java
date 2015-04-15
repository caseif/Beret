package net.caseif.beret.structures;

import net.caseif.beret.ClassInfo;
import net.caseif.beret.MethodInfo;

public class AttributeStructure {

	private ClassInfo parent;
	private String name;
	private byte[] info;

	/**
	 * Loads an {@link AttributeStructure}.
	 *
	 * @param parent The parent {@link ClassInfo} instance
	 */
	public AttributeStructure(ClassInfo parent, String name, byte[] info) {
		this.parent = parent;
		this.name = name;
		this.info = info;
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
	public byte[] getInfo() {
		return this.info;
	}

}
