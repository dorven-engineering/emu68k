package xyz.lonjil.m68k

import java.nio.ByteBuffer


class RAMDevice(size: Int, ramLatency: Int = 1, failLatency: Int = 1): MemoryDevice, ClockedDevice {
    private val backingArray: ByteBuffer = ByteBuffer.allocate(size)
    private val ramLatency: Int = ramLatency // Preserve provided latency
    private val failLatency: Int = failLatency
    private lateinit var manager: CycleManager

    override val deviceSize: Int = size
    override var usedCycles: Long = 0

    override fun registerCycleManager(man: CycleManager) {
        manager = man
    }

    override fun tick() {
        // literally 0 effort
    }

    override fun catchUp(lateCycles: Long) {
        this.usedCycles = 0 // literally 0 effort
    }

    override fun readByteNoEmu(addr: Int): Byte {
        if (addr < backingArray.capacity()) {
            return backingArray.get(addr)
        } else {
            throw IndexOutOfBoundsException("when reading byte from RAMDevice")
        }
    }

    override fun writeByteNoEmu(addr: Int, data: Byte) {
        if (addr < backingArray.capacity()) {
            backingArray.put(addr, data)
        } else {
            throw IndexOutOfBoundsException("when writing byte to RAMDevice")
        }
    }

    override fun readWordNoEmu(addr: Int): Short {
        if (addr < backingArray.capacity()-1) {
            return backingArray.getShort(addr)
        } else {
            throw IndexOutOfBoundsException("when reading short from RAMDevice")
        }
    }

    override fun writeWordNoEmu(addr: Int, data: Short) {
        if (addr < backingArray.capacity()-1) {
            backingArray.putShort(addr, data)
        } else {
            throw IndexOutOfBoundsException("when writing short to RAMDevice")
        }
    }

    override fun readDoubleWordNoEmu(addr: Int): Int {
        if (addr < backingArray.capacity()-3) {
            return backingArray.getInt(addr)
        } else {
            throw IndexOutOfBoundsException("when reading int from RAMDevice")
        }
    }

    override fun writeDoubleWordNoEmu(addr: Int, data: Int) {
        if (addr < backingArray.capacity()-3) {
            backingArray.putInt(addr, data)
        } else {
            throw IndexOutOfBoundsException("when writing int to RAMDevice")
        }
    }

    override fun readByte(addr: Int, type: MemoryAccessType): ReadAccessResult<Byte> {
        if (type.isMemoryAccess()) {
            usedCycles += ramLatency
            return ReadAccessResult(readByteNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            usedCycles += failLatency
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeByte(addr: Int, data: Byte, type: MemoryAccessType): MemoryAccessStatus {
        if (type.isMemoryAccess()) {
            writeByteNoEmu(addr, data)
            usedCycles += ramLatency
            return MemoryAccessStatus.Success
        } else {
            usedCycles += failLatency
            return MemoryAccessStatus.Unsupported
        }
    }

    override fun readWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Short> {
        if (type.isMemoryAccess()) {
            usedCycles += ramLatency
            return ReadAccessResult(readWordNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            usedCycles += failLatency
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeWord(addr: Int, data: Short, type: MemoryAccessType): MemoryAccessStatus {
        if (type.isMemoryAccess()) {
            usedCycles += ramLatency
            writeWordNoEmu(addr, data)
            return MemoryAccessStatus.Success
        } else {
            usedCycles += failLatency
            return MemoryAccessStatus.Unsupported
        }
    }

    override fun readDoubleWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Int> {
        if (type.isMemoryAccess()) {
            usedCycles += ramLatency
            return ReadAccessResult(readDoubleWordNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            usedCycles += failLatency
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeDoubleWord(addr: Int, data: Int, type: MemoryAccessType): MemoryAccessStatus {
        if (type.isMemoryAccess()) {
            usedCycles += ramLatency
            writeDoubleWordNoEmu(addr, data)
            return MemoryAccessStatus.Success
        } else {
            usedCycles += failLatency
            return MemoryAccessStatus.Unsupported
        }
    }
}

// ROM access is instant :D If you want to change this, just implement all the cycle handling
class ROMDevice(backing: ByteBuffer): MemoryDevice {
    private val backingArray: ByteBuffer = backing
    override val deviceSize: Int = backing.capacity()-1

    override fun readByteNoEmu(addr: Int): Byte {
        if (addr < backingArray.capacity()) {
            return backingArray.get(addr)
        } else {
            throw IndexOutOfBoundsException("when reading byte from RAMDevice")
        }
    }

    override fun writeByteNoEmu(addr: Int, data: Byte) {
        throw IllegalAccessException("cannot write to ROMDevice")
    }

    override fun readWordNoEmu(addr: Int): Short {
        if (addr < backingArray.capacity()-1) {
            return backingArray.getShort(addr)
        } else {
            throw IndexOutOfBoundsException("when reading short from RAMDevice")
        }
    }

    override fun writeWordNoEmu(addr: Int, data: Short) {
        throw IllegalAccessException("cannot write to ROMDevice")
    }

    override fun readDoubleWordNoEmu(addr: Int): Int {
        if (addr < backingArray.capacity()-3) {
            return backingArray.getInt(addr)
        } else {
            throw IndexOutOfBoundsException("when reading int from RAMDevice")
        }
    }

    override fun writeDoubleWordNoEmu(addr: Int, data: Int) {
        throw IllegalAccessException("cannot write to ROMDevice")
    }

    override fun readByte(addr: Int, type: MemoryAccessType): ReadAccessResult<Byte> {
        if (type.isMemoryAccess()) {
            return ReadAccessResult(readByteNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeByte(addr: Int, data: Byte, type: MemoryAccessType): MemoryAccessStatus {
        return MemoryAccessStatus.Unsupported
    }

    override fun readWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Short> {
        if (type.isMemoryAccess()) {
            return ReadAccessResult(readWordNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeWord(addr: Int, data: Short, type: MemoryAccessType): MemoryAccessStatus {
        return MemoryAccessStatus.Unsupported
    }

    override fun readDoubleWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Int> {
        if (type.isMemoryAccess()) {
            return ReadAccessResult(readDoubleWordNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeDoubleWord(addr: Int, data: Int, type: MemoryAccessType): MemoryAccessStatus {
        return MemoryAccessStatus.Unsupported
    }
}