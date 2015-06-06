package net.caseif.beret.structures.constant;

import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_Class_info</code> structure.
 */
public class StringStructure extends ConstantStructure {

    private Utf8Structure body;

    public StringStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.STRING.getTag(), content);
        this.body = (Utf8Structure)parent.getFromPool(content);
    }

    public Utf8Structure getBody() {
        return body;
    }
}
