package io.glossnyx.vibes.network.packet

import io.glossnyx.vibes.Vibes
import net.minecraft.network.PacketByteBuf
import java.util.UUID

data class Stop(val uuid: UUID) : Packet {
	companion object : PacketCompanion {
		override val id = Vibes.id("stop")
		override fun fromBuf(buf: PacketByteBuf) = Stop(buf.readUuid())
	}

	override val buffer = createPacket { writeUuid(uuid) }
}