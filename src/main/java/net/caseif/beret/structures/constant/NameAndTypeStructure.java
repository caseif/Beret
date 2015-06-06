package net.caseif.beret.structures.constant;

import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_NameAndType_info</code> structure.
 */
public class NameAndTypeStructure extends ConstantStructure {

    private Utf8Structure name;
    private Utf8Structure descriptor;

    public NameAndTypeStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.NAME_AND_TYPE.getTag(), content);
        this.name = (Utf8Structure)parent.getFromPool(content[0], content[1]);
        this.descriptor = (Utf8Structure)parent.getFromPool(content[2], content[3]);
    }

    public Utf8Structure getName() {
        return name;
    }

    public Utf8Structure getDescriptor() {
        return descriptor;
    }
}
