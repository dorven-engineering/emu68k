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

enum class MemoryAccessStatus {
    Success,
    Failure,
    BadData,
}

data class ReadAccessResult<T>(val data: T, val status: MemoryAccessStatus, val timeTaken: Int)
data class WriteAccessResult(val status: MemoryAccessStatus, val timeTaken: Int)

interface MemoryDevice {
    val deviceSize: Int

    // All memory accesses should throw an error if you attempt to read past the end of the device, as this is not intended behavior due to
    // the fact there are multiple MemoryDevices on one bus.

    fun readByte(addr: Int): ReadAccessResult<Byte>
    fun writeByte(addr: Int, data: Byte): WriteAccessResult

    fun readWord(addr: Int): ReadAccessResult<Short>
    fun writeWord(addr: Int, data: Short): WriteAccessResult

    fun readDoubleWord(addr: Int): ReadAccessResult<Int>
    fun writeDoubleWord(addr: Int, data: Int): WriteAccessResult

    // DANGER DANGER avoid in actual emulation code. Intended for usage by, say, a debugger.
    fun rawMemoryDevice(): ByteArray
}