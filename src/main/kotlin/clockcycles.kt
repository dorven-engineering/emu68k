package xyz.lonjil.m68k

interface ClockedDevice : GenericDevice {
    // Is the cycle manager REQUIRED to actively call tick() on this device.
    // Currently always does, but may be used for optimization in future
    fun isActiveDevice(): Boolean {
        return false
    }

    fun tick()

    fun registerCycleManager(manager: CycleManager)
}

class CycleManager {
    val devices: ArrayList<ClockedDevice> = ArrayList()

    fun registerDevice(device: ClockedDevice) {
        devices.add(device)
        device.registerCycleManager(this)
    }

    fun tick() {
        for (i in devices) {
            i.tick()
        }
    }

    fun tickTimes(count: Int) {
        for (i in count.downTo(0)) {
            tick()
        }
    }
}