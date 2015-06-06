package net.caseif.beret.structures.constant;

import net.caseif.beret.wrapper.ClassInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a <code>CONSTANT_MethodHandle_info</code> structure.
 */
public class MethodHandleStructure extends ConstantStructure {

    private ReferenceKind kind;
    private ConstantStructure ref;

    public MethodHandleStructure(ClassInfo parent, byte[] content) {
        super(parent, StructureType.METHOD_HANDLE.getTag(), content);
        this.kind = ReferenceKind.fromMagicNumber(content[0]);
        this.ref = parent.getFromPool(content[1], content[2]);
    }

    public ReferenceKind getReferenceKind() {
        return kind;
    }

    public ConstantStructure getReference() {
        return ref;
    }

    public enum ReferenceKind {

        GET_FIELD(1, StructureType.FIELD_REF),
        GET_STATIC(2, StructureType.FIELD_REF),
        PUT_FIELD(3, StructureType.FIELD_REF),
        PUT_STATIC(4, StructureType.FIELD_REF),

        INVOKE_VIRTUAL(5, StructureType.METHOD_REF),
        NEW_INVOKE_SPECIAL(8, StructureType.METHOD_REF),

        // Java 8 allows the type to be METHOD_REF or INTERFACE_METHOD_REF; 7 and below only allows the former
        INVOKE_STATIC(6, StructureType.METHOD_REF),
        INVOKE_SPECIAL(7, StructureType.METHOD_REF);

        private static Map<Byte, ReferenceKind> kinds = new HashMap<Byte, ReferenceKind>();

        private byte magic;
        private StructureType type;

        ReferenceKind(int magic, StructureType requiredType) {
            this.magic = (byte)magic;
            this.type = requiredType;

            putKind();
        }

        void putKind() {
            kinds.put(magic, this);
        }

        public byte getMagicNumber() {
            return magic;
        }

        public StructureType getRequiredType() {
            return this.type;
        }

        public static ReferenceKind fromMagicNumber(byte magicNumber) {
            return kinds.get(magicNumber);
        }

    }

}
