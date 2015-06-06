package net.caseif.beret.structures.constant;

import net.caseif.beret.Util;
import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_Integer_info</code> structure.
 */
public class IntegerStructure extends ConstantStructure {

    private int value;

    public IntegerStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.INTEGER.getTag(), content);
        this.value = Util.bytesToInt(content);
    }

    public int intValue() {
        return value;
    }
}
