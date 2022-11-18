package io.auxves.vibes.network.packet

import io.auxves.vibes.Vibes
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.util.UUID

data class Play(val uuid: UUID, val player: UUID, val sound: Identifier) : Packet {
	companion object : PacketCompanion {
		override val id = Vibes.id("play")

		override fun fromBuf(buf: PacketByteBuf) = Play(
			buf.readUuid(),
			buf.readUuid(),
			buf.readIdentifier()
		)
	}

	override val buffer = createPacket {
		writeUuid(uuid)
		writeUuid(player)
		writeIdentifier(sound)
	}
}