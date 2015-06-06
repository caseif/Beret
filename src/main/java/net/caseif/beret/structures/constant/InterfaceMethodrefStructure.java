package net.caseif.beret.structures.constant;

import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_InterfaceMethodref</code> structure.
 */
public class InterfaceMethodrefStructure extends ConstantStructure {

    private ClassStructure clazz;
    private NameAndTypeStructure name;

    public InterfaceMethodrefStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.INTERFACE_METHOD_REF.getTag(), content);
        this.clazz = (ClassStructure)parent.getFromPool(content[0], content[1]);
        this.name = (NameAndTypeStructure)parent.getFromPool(content[2], content[3]);
    }

    public ClassStructure getClassStructure() {
        return clazz;
    }

    public NameAndTypeStructure getNameAndType() {
        return name;
    }
}
