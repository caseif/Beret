package net.caseif.beret.structures;

public class AttributeStructure {

	private String name;
	private byte[] info;

	/**
	 * Loads an {@link AttributeStructure}
	 */
	public AttributeStructure(String name, byte[] info) {
		this.name = name;
		this.info = info;
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
