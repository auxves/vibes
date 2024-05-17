package dev.auxves.vibes.network.payloads

import dev.auxves.vibes.Vibes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import net.minecraft.util.math.BlockPos
import java.util.UUID

data class ChangePositionBlock(val uuid: UUID, val blockPos: BlockPos) : CustomPayload {
	companion object {
		val ID = CustomPayload.Id<ChangePositionBlock>(Vibes.id("change-position-block"))

		val PACKET_CODEC = object: PacketCodec<ByteBuf, ChangePositionBlock> {
			override fun decode(buf: ByteBuf) = ChangePositionBlock(
				Uuids.PACKET_CODEC.decode(buf),
				BlockPos.PACKET_CODEC.decode(buf)
			)

			override fun encode(buf: ByteBuf, value: ChangePositionBlock) {
				Uuids.PACKET_CODEC.encode(buf, value.uuid)
				BlockPos.PACKET_CODEC.encode(buf, value.blockPos)
			}
		}
	}

	override fun getId() = ID
}
