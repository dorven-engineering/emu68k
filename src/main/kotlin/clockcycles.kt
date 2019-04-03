package xyz.lonjil.m68k

import kotlin.math.max

interface ClockedDevice : GenericDevice {
    var usedCycles: Long

    // Is the cycle manager REQUIRED to actively call tick() on this device.
    // Currently always does, but may be used for optimization in future
    fun isActiveDevice(): Boolean {
        return false
    }

    fun tick()

    // lateCycles may be zero. If this happens, do nothing.
    fun catchUp(lateCycles: Long)

    fun registerCycleManager(manager: CycleManager)
}

class CycleManager {
    val devices: ArrayList<ClockedDevice> = ArrayList()

    fun registerDevice(device: ClockedDevice) {
        devices.add(device)
        device.registerCycleManager(this)
    }

    fun tick() {
        var highestCycles: Long = 0

        for (i in devices) {
            i.tick()
            highestCycles = max(highestCycles, highestCycles + i.usedCycles)
        }

        // catchup loop
        for (i in devices) {
            if (i.usedCycles < highestCycles) {
                i.catchUp(highestCycles - i.usedCycles)
            }
        }
    }

    fun tickTimes(count: Int) {
        for (i in count.downTo(0)) {
            tick()
        }
    }
}