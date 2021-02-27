package io.glossnyx.vibes.network.packet

import io.glossnyx.vibes.Vibes
import net.minecraft.network.PacketByteBuf

data class RightClickStop(val slotID: Int) : Packet {
	companion object: PacketCompanion {
		override val id = Vibes.id("right-click-stop")
		override fun fromBuf(buf: PacketByteBuf) = RightClickStop(buf.readVarInt())
	}

	override val buffer = createPacket { writeVarInt(slotID) }
}