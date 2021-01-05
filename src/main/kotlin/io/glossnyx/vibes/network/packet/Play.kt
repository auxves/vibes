package io.glossnyx.vibes.network.packet

import io.glossnyx.vibes.Vibes
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.util.UUID

data class Play(val uuid: UUID, val entityUUID: UUID, val identifier: Identifier) : Packet {
	companion object : PacketCompanion {
		override val id = Vibes.id("play")

		override fun fromBuf(buf: PacketByteBuf) = Play(
			buf.readUuid(),
			buf.readUuid(),
			buf.readIdentifier()
		)
	}

	override fun toBuf(): PacketByteBuf = PacketByteBufs.create()
		.writeUuid(uuid)
		.writeUuid(entityUUID)
		.writeIdentifier(identifier)
}