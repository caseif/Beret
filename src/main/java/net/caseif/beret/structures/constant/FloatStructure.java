package net.caseif.beret.structures.constant;

import net.caseif.beret.Util;
import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_Float_info</code> structure.
 */
public class FloatStructure extends ConstantStructure {

    private float value;

    public FloatStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.FLOAT.getTag(), content);
        value = Float.intBitsToFloat(Util.bytesToInt(content));
    }

    public float floatValue() {
        return value;
    }
}
