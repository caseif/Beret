package net.caseif.beret.structures.constant;

import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_Class_info</code> structure.
 */
public class ClassStructure extends ConstantStructure {

    private Utf8Structure name;

    public ClassStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.CLASS.getTag(), content);
        this.name = (Utf8Structure)parent.getFromPool(content);
    }

    public Utf8Structure getName() {
        return name;
    }
}
