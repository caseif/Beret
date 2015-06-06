package net.caseif.beret.structures.constant;

import net.caseif.beret.Util;
import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_InvokeDynamic_info</code> structure.
 */
public class InvokeDynamicStructure extends ConstantStructure {

    private int bootstrapMethodIndex;
    private NameAndTypeStructure name;

    public InvokeDynamicStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.INVOKE_DYNAMIC.getTag(), content);
        this.bootstrapMethodIndex = Util.bytesToUshort(content[0], content[1]);
        this.name = (NameAndTypeStructure)parent.getFromPool(content[2], content[3]);
    }

    public int getBootstrapMethodIndex() {
        return bootstrapMethodIndex;
    }

    public NameAndTypeStructure getNameAndType() {
        return name;
    }
}
