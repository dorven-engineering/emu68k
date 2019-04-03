package xyz.lonjil.risc5

import org.jetbrains.annotations.NotNull
import java.util.*

enum class MemoryAccessType {
    UserData,
    UserProgram,
    SuperData,
    SuperProgram,
    Hardware
}

enum class MemoryAccessStatus {
    Success,
    Failure,
    BadData,
    Unsupported,
}

data class ReadAccessResult<T>(val data: T, val status: MemoryAccessStatus) {
    fun expectValue(): T {
        if (status != MemoryAccessStatus.Success) {
            throw IllegalStateException("Memory bus handler failed to provide value that was required!")
        } else {
            return data
        }
    }
}

interface MemoryDevice : GenericDevice {
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

interface MemoryBusHandler : GenericDevice {
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