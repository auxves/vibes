package dev.auxves.vibes.network.payloads

import dev.auxves.vibes.Vibes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import net.minecraft.util.Uuids
import java.util.UUID

data class Play(val uuid: UUID, val player: UUID, val sound: Identifier) : CustomPayload {
	companion object {
		val ID = CustomPayload.Id<Play>(Vibes.id("play"))

		val PACKET_CODEC = object: PacketCodec<ByteBuf, Play> {
			override fun decode(buf: ByteBuf) = Play(
				Uuids.PACKET_CODEC.decode(buf),
				Uuids.PACKET_CODEC.decode(buf),
				Identifier.PACKET_CODEC.decode(buf)
			)

			override fun encode(buf: ByteBuf, value: Play) {
				Uuids.PACKET_CODEC.encode(buf, value.uuid)
				Uuids.PACKET_CODEC.encode(buf, value.player)
				Identifier.PACKET_CODEC.encode(buf, value.sound)
			}
		}
	}

	override fun getId() = ID
}