package io.glossnyx.vibes.network.packet

import io.glossnyx.vibes.Vibes
import net.minecraft.network.PacketByteBuf
import java.util.UUID

data class ChangePositionEntity(val uuid: UUID, val entityUUID: UUID) : Packet {
	companion object : PacketCompanion {
		override val id = Vibes.id("change-position-entity")

		override fun fromBuf(buf: PacketByteBuf) = ChangePositionEntity(
			buf.readUuid(),
			buf.readUuid()
		)
	}

	override val buffer = createPacket {
		writeUuid(uuid)
		writeUuid(entityUUID)
	}
}