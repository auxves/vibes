package io.glossnyx.vibes.network.packet

import io.glossnyx.vibes.Vibes
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.util.UUID

data class RightClickPlay(val slotID: Int, val uuid: UUID, val identifier: Identifier) : Packet {
	companion object : PacketCompanion {
		override val id = Vibes.id("right-click-play")

		override fun fromBuf(buf: PacketByteBuf) = RightClickPlay(
			buf.readVarInt(),
			buf.readUuid(),
			buf.readIdentifier()
		)
	}

	override val buffer = createPacket {
		writeVarInt(slotID)
		writeUuid(uuid)
		writeIdentifier(identifier)
	}
}