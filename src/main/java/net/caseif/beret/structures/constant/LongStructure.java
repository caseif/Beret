package net.caseif.beret.structures.constant;

import net.caseif.beret.Util;
import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_Long_info</code> structure.
 */
public class LongStructure extends ConstantStructure {

    private long value;

    public LongStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.LONG.getTag(), content);
        this.value = Util.bytesToLong(content);
    }

    public long longValue() {
        return value;
    }
}
