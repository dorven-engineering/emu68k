package xyz.lonjil.risc5

import org.apache.commons.lang3.BitField

enum class InstructionType {
    R,I,S,B,U,J,
}

val opcodeMask = BitField(0x7F)
val baseOpMask = BitField(0x7C)
val opInstLen1 = BitField(0x3)
val opInstLen2 = BitField(0x1C)
val immSignMask = BitField(1.shl(31))
val immIMask = BitField(0xFFF.shl(20))
val immSMask0_4 = BitField(0x1F.shl(6))
val immSMask5_11 = BitField(0x7F.shl(25))
val immBMask1_4 = BitField(0xF.shl(7))
val immBMask5_10 = BitField(0x3F.shl(24))
val immBMask11 = BitField(1.shl(6))
val immBMask12 = immSignMask
val immUMask12_31 = BitField(0xFFFFF.shl(11))
val immJMask1_10 = BitField(0xA.shl(20))
val immJMask11 = BitField(1.shl(19))
val immJMask12_19 = BitField(0x3F.shl(11))
val immJMask20 = immSignMask

fun extend_sign(number: Int, bits: Int): Int {
    val shift = 32 - bits
    return number.shl(shift) shr(shift)
}

fun deshuffle(inst: Int, format: InstructionType): Int? {
    return when (format) {
        InstructionType.R -> null
        InstructionType.I -> extend_sign(immIMask.getValue(inst),12)
        InstructionType.S -> extend_sign(immSMask5_11.getValue(inst)
            .shl(5).or(immSMask0_4.getValue(inst)),12)
        InstructionType.B -> extend_sign(immBMask12.getValue(inst)
            shl(12).or(immBMask11.getValue(inst).shl(11))
            .or(immBMask5_10.getValue(inst).shl(5))
            .or(immBMask1_4.getValue(inst).shl(1)),13)
        InstructionType.U -> immUMask12_31.getValue(inst).shl(12)
        InstructionType.J -> extend_sign(immJMask20.getValue(inst)
            .shl(20).or(immJMask12_19.getValue(inst).shl(12))
            .or(immJMask11.getValue(inst).shl(11))
            .or(immJMask1_10.getValue(inst).shl(1)), 21)
    }
}


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
