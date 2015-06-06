package net.caseif.beret.structures.constant;

import net.caseif.beret.Util;
import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_Float_info</code> structure.
 */
public class DoubleStructure extends ConstantStructure {

    private double value;

    public DoubleStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.DOUBLE.getTag(), content);
        value = Double.longBitsToDouble(Util.bytesToLong(content));
    }

    public double doubleValue() {
        return value;
    }
}
