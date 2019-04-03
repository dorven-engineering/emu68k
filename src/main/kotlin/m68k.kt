package xyz.lonjil.m68k

enum class Register(val n: Int) {
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
    PC(14),
    CCR(15),
}

class M68k() {
    var regs = IntArray(16)

    fun ReadRegister(reg: Register): Int {
        return regs[reg.n]
    }
    fun WriteRegister(reg: Register, word: Int) {
        regs[reg.n] = word
    }


}

