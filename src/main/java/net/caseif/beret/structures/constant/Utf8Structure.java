package net.caseif.beret.structures.constant;

import net.caseif.beret.Util;
import net.caseif.beret.wrapper.ClassInfo;

/**
 * Represents a <code>CONSTANT_Utf8_info</code> structure.
 */
public class Utf8Structure extends ConstantStructure {

    private String value;

    public Utf8Structure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.UTF_8.getTag(), content);
        this.value = Util.asUtf8(content);
    }

    public int getLength() {
        return getContent().length;
    }

    @Override
    public String toString() {
        return value;
    }
}
