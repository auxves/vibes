package io.glossnyx.vibes.network.packet

import io.glossnyx.vibes.Vibes
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.BlockPos
import java.util.UUID

data class ChangePositionBlock(val uuid: UUID, val blockPos: BlockPos) : Packet {
	companion object : PacketCompanion {
		override val id = Vibes.id("change-position-block")

		override fun fromBuf(buf: PacketByteBuf) = ChangePositionBlock(
			buf.readUuid(),
			buf.readBlockPos()
		)
	}

	override fun toBuf(): PacketByteBuf = PacketByteBufs.create()
		.writeUuid(uuid)
		.writeBlockPos(blockPos)
}