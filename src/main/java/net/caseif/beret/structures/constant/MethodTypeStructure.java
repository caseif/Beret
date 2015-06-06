package net.caseif.beret.structures.constant;

import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_MethodType_info</code> structure.
 */
public class MethodTypeStructure extends ConstantStructure {

    private Utf8Structure descriptor;

    public MethodTypeStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.METHOD_TYPE.getTag(), content);
        this.descriptor = (Utf8Structure)parent.getFromPool(content);
    }

    public Utf8Structure getDescriptor() {
        return descriptor;
    }
}
