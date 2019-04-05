package xyz.lonjil.risc5

import org.apache.commons.lang3.BitField
import kotlin.reflect.KProperty

sealed class DecoderResult {
    data class NeedMoreParcels(val remaining_parcels: Int): DecoderResult()
    data class DecodedInstruction(val instr: Instruction): DecoderResult()
    open class Error: DecoderResult() {
        class ShortNotImplementedYet : Error()
        data class LongNotImplementedYet(val length: Int) : Error()
    }
}



class Decoder {
    var prevParcelAddr = 0L
    var prevParcel = 0

    val rdMask = BitField(0xF80)
    val len1 = BitField(0x3)
    val len2 = BitField(0x1C)

    fun decode(parcel: Int, parcel_addr: Long): DecoderResult {
        if (prevParcel != 0 && prevParcelAddr + 2 == parcel_addr) {
            val inst = parcel.shl(16) + prevParcel
            if (!len1.isAllSet(inst) || len2.isAllSet(inst)) {
                // TODO("Tell emulator how many parcels would be expected")
                return DecoderResult.Error.LongNotImplementedYet(-1)
            }

            val opcode = BaseOp.fromRawInstruction(inst)


        } else {
            // TODO("Implement short instructions")
            if (!len1.isAllSet(parcel)) {
                return DecoderResult.Error.ShortNotImplementedYet()
            }
            prevParcelAddr = parcel_addr
            prevParcel = parcel
            return DecoderResult.NeedMoreParcels(1)
        }
        return DecoderResult.Error()
    }

}
