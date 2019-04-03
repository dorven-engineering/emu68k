package xyz.lonjil.m68k

import org.jetbrains.annotations.NotNull
import java.nio.ByteBuffer
import java.util.*

enum class MemoryAccessType {
    Reserved0,
    UserData,
    UserProgram,
    Custom,
    Reserved4,
    SuperData,
    SuperProgram,
    CPUSpace;

    fun isMemoryAccess(): Boolean {
        if (this == Reserved0 || this == Custom || this == Reserved4 || this == CPUSpace) {
            return false
        }
        return true
    }
}

enum class MemoryAccessStatus {
    Success,
    Failure,
    BadData,
    Unsupported,
}

data class ReadAccessResult<T>(val data: T, val status: MemoryAccessStatus)

interface MemoryDevice {
    val deviceSize: Int

    @NotNull
    fun needsEmulatedCPUSpaceCycles(): Boolean {
        return false
    }

    @NotNull
    fun supportsCustomAccessType(): Boolean {
        return false
    }

    @NotNull
    fun readByte(addr: Int, type: MemoryAccessType): ReadAccessResult<Byte>
    @NotNull
    fun writeByte(addr: Int, data: Byte, type: MemoryAccessType): MemoryAccessStatus

    @NotNull
    fun readWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Short>
    @NotNull
    fun writeWord(addr: Int, data: Short, type: MemoryAccessType): MemoryAccessStatus

    @NotNull
    fun readDoubleWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Int>
    @NotNull
    fun writeDoubleWord(addr: Int, data: Int, type: MemoryAccessType): MemoryAccessStatus

    // TL;DR the NoEmu functions do not simulate bus cycles on use.

    @NotNull
    fun readByteNoEmu(addr: Int): Byte
    @NotNull
    fun writeByteNoEmu(addr: Int, data: Byte)

    @NotNull
    fun readWordNoEmu(addr: Int): Short
    @NotNull
    fun writeWordNoEmu(addr: Int, data: Short)

    @NotNull
    fun readDoubleWordNoEmu(addr: Int): Int
    @NotNull
    fun writeDoubleWordNoEmu(addr: Int, data: Int)
}

class RAMDevice(size: Int, ramLatency: Int = 1): MemoryDevice {
    private val backingArray: ByteBuffer = ByteBuffer.allocate(size)
    private val ramLatency: Int = ramLatency // Preserve provided latency;

    override val deviceSize: Int = size;

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
            return ReadAccessResult(readByteNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeByte(addr: Int, data: Byte, type: MemoryAccessType): MemoryAccessStatus {
        if (type.isMemoryAccess()) {
            writeByteNoEmu(addr, data)
            return MemoryAccessStatus.Success
        } else {
            return MemoryAccessStatus.Unsupported
        }
    }

    override fun readWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Short> {
        if (type.isMemoryAccess()) {
            return ReadAccessResult(readWordNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeWord(addr: Int, data: Short, type: MemoryAccessType): MemoryAccessStatus {
        if (type.isMemoryAccess()) {
            writeWordNoEmu(addr, data)
            return MemoryAccessStatus.Success
        } else {
            return MemoryAccessStatus.Unsupported
        }
    }

    override fun readDoubleWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Int> {
        if (type.isMemoryAccess()) {
            return ReadAccessResult(readDoubleWordNoEmu(addr), MemoryAccessStatus.Success)
        } else {
            return ReadAccessResult(0, MemoryAccessStatus.Unsupported)
        }
    }

    override fun writeDoubleWord(addr: Int, data: Int, type: MemoryAccessType): MemoryAccessStatus {
        if (type.isMemoryAccess()) {
            writeDoubleWordNoEmu(addr, data)
            return MemoryAccessStatus.Success
        } else {
            return MemoryAccessStatus.Unsupported
        }
    }
}

interface MemoryBusHandler {
    val addressSpaceSize: Int

    @NotNull
    fun needsEmulatedCPUSpaceCycles(): Boolean {
        return false
    }

    @NotNull
    fun supportsCustomAccessType(): Boolean {
        return false
    }

    @NotNull
    fun readByte(addr: Int, type: MemoryAccessType): ReadAccessResult<Byte>
    @NotNull
    fun writeByte(addr: Int, data: Byte, type: MemoryAccessType): MemoryAccessStatus

    @NotNull
    fun readWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Short>
    @NotNull
    fun writeWord(addr: Int, data: Short, type: MemoryAccessType): MemoryAccessStatus

    @NotNull
    fun readDoubleWord(addr: Int, type: MemoryAccessType): ReadAccessResult<Int>
    @NotNull
    fun writeDoubleWord(addr: Int, data: Int, type: MemoryAccessType): MemoryAccessStatus

    // TL;DR the NoEmu functions do not simulate bus cycles on use.

    @NotNull
    fun readByteNoEmu(addr: Int): ReadAccessResult<Byte>
    @NotNull
    fun writeByteNoEmu(addr: Int, data: Byte): MemoryAccessStatus

    @NotNull
    fun readWordNoEmu(addr: Int): ReadAccessResult<Short>
    @NotNull
    fun writeWordNoEmu(addr: Int, data: Short): MemoryAccessStatus

    @NotNull
    fun readDoubleWordNoEmu(addr: Int): ReadAccessResult<Int>
    @NotNull
    fun writeDoubleWordNoEmu(addr: Int, data: Int): MemoryAccessStatus

    @NotNull
    fun addDevice(baseAddr: Int, device: MemoryDevice): MemoryBusHandler
    @NotNull
    fun getDevice(coveredAddr: Int): Optional<MemoryDevice>

    // Should only be called ONCE
    fun registerToCycleManager(): MemoryBusHandler
}