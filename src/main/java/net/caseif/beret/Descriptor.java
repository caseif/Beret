package net.caseif.beret;

import java.util.HashMap;
import java.util.Map;

public class Descriptor {

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
		primTypes.put("Z", "boolean");

	}

	/**
	 * Constructs a new descriptor object from the given string.
	 *
	 * @param str The string to construct a descriptor from
	 */
	public Descriptor(String str) {
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
			sb.append("[");
		}
		return sb.toString();
	}

}
