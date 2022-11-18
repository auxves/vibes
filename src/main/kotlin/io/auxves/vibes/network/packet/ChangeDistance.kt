package io.auxves.vibes.network.packet

import io.auxves.vibes.Vibes
import net.minecraft.network.PacketByteBuf
import java.util.UUID

data class ChangeDistance(val uuid: UUID, val distance: Float) : Packet {
	companion object : PacketCompanion {
		const val Normal = 16F
		const val Boosted = 64F

		override val id = Vibes.id("change-distance")

		override fun fromBuf(buf: PacketByteBuf) = ChangeDistance(
			buf.readUuid(),
			buf.readFloat()
		)
	}

	override val buffer = createPacket {
		writeUuid(uuid)
		writeFloat(distance)
	}
}