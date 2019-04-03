package xyz.lonjil.m68k

enum class Register(n: Int) {
    D0(0),
    D1(1),
    D2(2),
    D3(3),
    D4(4),
    D6(5),
    D7(6),
    A0(7),
    A1(8),
    A2(9),
    A3(10),
    A4(11),
    A5(12),
    A6(13),
    A7(14),
    USP(14),
    PC(15),
    CCR(16),
}

class m68k(ram_size: Int) {
    var ram: ByteArray
    var regs = IntArray(17)

    init {
        ram = ByteArray(ram_size)
    }

    fun ReadRegister(reg: Register): Int {
        return regs[reg as Int]
    }
    fun WriteRegister(reg: Register, word: Int) {
        regs[reg as Int] = word
    }

    fun MemRead8(loc: Int): Byte {
        return ram[loc]
    }
    fun MemRead16(loc: Int): Short {
        return (ram[loc] as Int).shl(8).or(ram[loc+1] as Int) as Short
    }
    fun MemRead32(loc: Int): Int {
        return (ram[loc] as Int).shl(8).or(ram[loc+1] as Int).shl(8).or(ram[loc+2] as Int).shl(8).or(ram[loc+3] as Int)
    }
    fun MemWrite8(loc: Int, byte: Byte) {
        ram[loc] = byte
    }
    fun MemWrite16(loc: Int, short: Short) {
        ram[loc] = (short as Int).shr(8) as Byte
        ram[loc+1] = (short as Int).and(0xFF) as Byte
    }
    fun MemWrite32(loc: Int, int: Int) {
        ram[loc] = int.shr(24) as Byte
        ram[loc+1] = int.shr(16).and(0xFF) as Byte
        ram[loc+2] = int.shr(8).and(0xFF) as Byte
        ram[loc+3] = int.and(0xFF) as Byte
    }

}



enum class AddressingMode {

}

enum class Op {
    Add,
    Sub,
}