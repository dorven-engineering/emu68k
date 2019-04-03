package xyz.lonjil.m68k


// Generated on the fly.
interface Argument {
    fun computeValue(): Int
    fun writeValue(data: Int)
    fun parentCPU(): M68k
}

// Generated on the fly.
interface Instruction {
    fun execute(opcode: Int, source: Argument, destination: Argument, cpu: M68k)
    fun identity(): String
}

interface InstructionRegistrar {
    fun registerInstructions(isa: InstructionSet)
}