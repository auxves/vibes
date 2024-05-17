package dev.auxves.vibes.network.payloads

import dev.auxves.vibes.Vibes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import java.util.UUID

data class Stop(val uuid: UUID) : CustomPayload {
	companion object {
		val ID = CustomPayload.Id<Stop>(Vibes.id("stop"))

		val PACKET_CODEC = object: PacketCodec<ByteBuf, Stop> {
			override fun decode(buf: ByteBuf) = Stop(
				Uuids.PACKET_CODEC.decode(buf)
			)

			override fun encode(buf: ByteBuf, value: Stop) {
				Uuids.PACKET_CODEC.encode(buf, value.uuid)
			}
		}
	}

	override fun getId() = ID
}