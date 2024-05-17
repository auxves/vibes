package dev.auxves.vibes.network.payloads

import dev.auxves.vibes.Vibes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import java.util.UUID

data class ChangePositionEntity(val uuid: UUID, val entityUUID: UUID) : CustomPayload {
	companion object {
		val ID = CustomPayload.Id<ChangePositionEntity>(Vibes.id("change-position-entity"))

		val PACKET_CODEC = object: PacketCodec<ByteBuf, ChangePositionEntity> {
			override fun decode(buf: ByteBuf) = ChangePositionEntity(
				Uuids.PACKET_CODEC.decode(buf),
				Uuids.PACKET_CODEC.decode(buf)
			)

			override fun encode(buf: ByteBuf, value: ChangePositionEntity) {
				Uuids.PACKET_CODEC.encode(buf, value.uuid)
				Uuids.PACKET_CODEC.encode(buf, value.entityUUID)
			}
		}
	}

	override fun getId() = ID
}
