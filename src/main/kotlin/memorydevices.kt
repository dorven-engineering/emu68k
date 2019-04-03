package xyz.lonjil.m68k

import java.nio.ByteBuffer


class RAMDevice(size: Int, val ramLatency: Int = 1, val failLatency: Int = 1) : MemoryDevice, ClockedDevice {
    private val backingArray: ByteBuffer = ByteBuffer.allocate(size)
    private lateinit var cycleManager: CycleManager

    override val deviceSize: Int = size

    override fun registerCycleManager(manager: CycleManager) {
        cycleManager = manager
    }

    override fun tick() {
        // literally 0 effort
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
        return ReadAccessResult(readByteNoEmu(addr), MemoryAccessStatus.Success)
    }

    override fun writeByte(addr: Int, data: Byte, type: MemoryAccessType): MemoryAccessStatus {
        writeByteNoEmu(addr, data)
        return MemoryAccessStatus.Success
    }

    override fun readWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Short> {
        return ReadAccessResult(readWordNoEmu(addr), MemoryAccessStatus.Success)
    }

    override fun writeWord(addr: Int, data: Short, type: MemoryAccessType): MemoryAccessStatus {
            writeWordNoEmu(addr, data)
            return MemoryAccessStatus.Success
    }

    override fun readDoubleWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Int> {
            return ReadAccessResult(readDoubleWordNoEmu(addr), MemoryAccessStatus.Success)
    }

    override fun writeDoubleWord(addr: Int, data: Int, type: MemoryAccessType): MemoryAccessStatus {
            writeDoubleWordNoEmu(addr, data)
            return MemoryAccessStatus.Success
    }
}

// ROM access is instant :D If you want to change this, just implement all the cycle handling
class ROMDevice(val backingArray: ByteBuffer) : MemoryDevice {
    override val deviceSize: Int = backingArray.capacity() - 1

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
        return ReadAccessResult(readByteNoEmu(addr), MemoryAccessStatus.Success)
    }

    override fun writeByte(addr: Int, data: Byte, type: MemoryAccessType): MemoryAccessStatus {
        return MemoryAccessStatus.Unsupported
    }

    override fun readWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Short> {
        return ReadAccessResult(readWordNoEmu(addr), MemoryAccessStatus.Success)
    }

    override fun writeWord(addr: Int, data: Short, type: MemoryAccessType): MemoryAccessStatus {
        return MemoryAccessStatus.Unsupported
    }

    override fun readDoubleWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Int> {
        return ReadAccessResult(readDoubleWordNoEmu(addr), MemoryAccessStatus.Success)
    }

    override fun writeDoubleWord(addr: Int, data: Int, type: MemoryAccessType): MemoryAccessStatus {
        return MemoryAccessStatus.Unsupported
    }
}