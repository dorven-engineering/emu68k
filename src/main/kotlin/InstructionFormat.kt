package xyz.lonjil.risc5

import org.apache.commons.lang3.BitField

enum class InstructionType {
    R,I,S,B,U,J,
}

val baseOpMask = BitField(0x7C)

enum class BaseOp(val value: Int) {
    LOAD        (0x00),
    LOAD_FP     (0x01),
    CUSTOM_0    (0x02),
    MISC_MEM    (0x03),
    OP_IMM      (0x04),
    AUIPC       (0x05),
    OP_IMM_32   (0x06),

    STORE       (0x08),
    STORE_FP    (0x09),
    CUSTOM_1    (0x0A),
    AMO         (0x0B),
    OP          (0x0C),
    LUI         (0x0D),
    OP_32       (0x0E),

    MADD        (0x10),
    MSUB        (0x11),
    NMSUB       (0x12),
    NMADD       (0x13),
    OP_FP       (0x14),
    RESERVED1   (0x15),
    CUSTOM_2    (0x16), // also rv128

    BRANCH      (0x18),
    JALR        (0x19),
    RESERVED2   (0x1A),
    JAL         (0x1B),
    SYSTEM      (0x1C),
    RESERVED3   (0x1D),
    CUSTOM3     (0x1E), // also rv128
    ;

    companion object {
        private val map = BaseOp.values().associateBy(BaseOp::value)
        fun fromInt(type: Int) = map[type] ?: throw IllegalArgumentException()
        fun fromRawInstruction(foo: Int) = fromInt(baseOpMask.getValue(foo))
    }
}
