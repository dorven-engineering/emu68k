package xyz.lonjil.m68k


// Generated on the fly.
interface Argument {
    fun computeValue(): Int
    fun writeValue(data: Int)
    fun computeAccessTime(): Int {
        return 0
    }
}

// Generated on the fly.
interface Instruction {
    fun execute(opcode: Int, source: Argument, destination: Argument, cpu: M68k)
    fun computeExecuteTime(opcode: Int, source: Argument, destination: Argument): Int {
        return 1 + source.computeAccessTime() + destination.computeAccessTime()
    }
    fun identity(): String
}

interface InstructionRegistrar {
    fun registerInstructions(isa: InstructionSet)
}