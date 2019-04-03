package xyz.lonjil.m68k

class M68k(bus: MemoryBusHandler) : ClockedDevice {
    // Devices
    lateinit var cycleManager: CycleManager
    var membus: MemoryBusHandler = bus


    // Registers and register related things
    private val dataRegisters: IntArray = IntArray(8)
    private val addrRegisters: IntArray = IntArray(7)

    var userStackpointer: Int = 0
    var interruptStackpointer: Int = 0
    var masterStackpointer: Int = 0

    // Getter/Setter that allows you to just use whatever stackpointer is currently active
    var stackpointer: Int
        get() {
            if (masterOrInterrupt && inSupervisor) {
                return masterStackpointer
            } else if (inSupervisor) {
                return interruptStackpointer
            } else {
                return userStackpointer
            }
        }
        set(value) {
            if (masterOrInterrupt && inSupervisor) {
                masterStackpointer = value
            } else if (inSupervisor) {
                interruptStackpointer = value
            } else {
                userStackpointer = value
            }
        }
    var programCounter: Int = 0

    var inSupervisor = true
    var masterOrInterrupt = true
    var traceMode: Byte = 0
    var interruptPriorityMask: Byte = 7

    var sfc: Byte = 0
    var dfc: Byte = 0
    var vbr: Int = 0

    var carry = false
    var overflow = false
    var zero = false
    var negative = false
    var extend = false

    fun clear_user_flags() {
        carry = false
        overflow = false
        zero = false
        negative = false
        extend = false
    }


    override var usedCycles: Long = 0

    var has_reset_once = false
    override fun reset() {
        if (!has_reset_once) {
            usedCycles += 512
            has_reset_once = true
        } else {
            usedCycles += 4
        }

        traceMode = 0
        interruptPriorityMask = 7
        inSupervisor = true
        masterOrInterrupt = false
        vbr = 0

        clear_user_flags()

        interruptStackpointer = membus.readDoubleWord(vbr + 0, MemoryAccessType.SuperData).expectValue()
        programCounter = membus.readDoubleWord(vbr + 4, MemoryAccessType.SuperData).expectValue()
    }

    override fun catchUp(lateCycles: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerCycleManager(manager: CycleManager) {
        cycleManager = manager
    }

    override fun tick() {
        TODO("not implemented")
    }

    fun executeNextInstr() {
        TODO("not implemented")
    }
}

