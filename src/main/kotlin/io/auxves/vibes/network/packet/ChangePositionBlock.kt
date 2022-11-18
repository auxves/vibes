package io.auxves.vibes.network.packet

import io.auxves.vibes.Vibes
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

	override val buffer = createPacket {
		writeUuid(uuid)
		writeBlockPos(blockPos)
	}
}