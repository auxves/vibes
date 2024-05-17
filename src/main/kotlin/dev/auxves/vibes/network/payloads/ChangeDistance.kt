package dev.auxves.vibes.network.payloads

import dev.auxves.vibes.Vibes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import java.util.UUID

data class ChangeDistance(val uuid: UUID, val distance: Float) : CustomPayload {
	companion object {
		val ID = CustomPayload.Id<ChangeDistance>(Vibes.id("change-distance"))

		val PACKET_CODEC = object: PacketCodec<ByteBuf, ChangeDistance> {
			override fun decode(buf: ByteBuf) = ChangeDistance(
				Uuids.PACKET_CODEC.decode(buf),
				PacketCodecs.FLOAT.decode(buf)
			)

			override fun encode(buf: ByteBuf, value: ChangeDistance) {
				Uuids.PACKET_CODEC.encode(buf, value.uuid)
				PacketCodecs.FLOAT.encode(buf, value.distance)
			}
		}
	}

	override fun getId() = ID
}
