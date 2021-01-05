package io.glossnyx.vibes.network.packet

import io.glossnyx.vibes.Vibes
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import java.util.UUID

data class Stop(val uuid: UUID) : Packet {
	companion object : PacketCompanion {
		override val id = Vibes.id("stop")
		override fun fromBuf(buf: PacketByteBuf) = Stop(buf.readUuid())
	}

	override fun toBuf(): PacketByteBuf = PacketByteBufs.create().writeUuid(uuid)
}