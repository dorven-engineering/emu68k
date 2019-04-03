package xyz.lonjil.m68k

object InstructionSet {
    val instructions: Array<Instruction?>
    var knownOps: Int = 0

    init {
        instructions = Array(65536, { _ -> null }) // Null means invalid!
    }

    fun addInstruction(opcode: Int, instr: Instruction) {
        if (instructions[opcode] == null) {
            instructions[opcode] = instr
            knownOps++
        } else {
            // Throwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
            throw IllegalArgumentException("Attempted to overwrite existing instruction ["+ (instructions[opcode] as Instruction).javaClass.name + "] at 0x" + String.format("%04x", opcode) + " with [" + instr.javaClass.name + "]")
        }
    }
}
