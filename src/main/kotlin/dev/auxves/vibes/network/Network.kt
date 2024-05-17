package dev.auxves.vibes.network

import dev.auxves.vibes.network.payloads.*
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.packet.CustomPayload
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

fun initNetwork() {
	PayloadTypeRegistry.playC2S().register(Play.ID, Play.PACKET_CODEC)
	PayloadTypeRegistry.playS2C().register(Play.ID, Play.PACKET_CODEC)

	PayloadTypeRegistry.playC2S().register(Stop.ID, Stop.PACKET_CODEC)
	PayloadTypeRegistry.playS2C().register(Stop.ID, Stop.PACKET_CODEC)

	PayloadTypeRegistry.playS2C().register(ChangeDistance.ID, ChangeDistance.PACKET_CODEC)
	PayloadTypeRegistry.playS2C().register(ChangePositionBlock.ID, ChangePositionBlock.PACKET_CODEC)
	PayloadTypeRegistry.playS2C().register(ChangePositionEntity.ID, ChangePositionEntity.PACKET_CODEC)
}

/** Sends a packet to every client */
fun World.sendAll(payload: CustomPayload) {
	val world = this as? ServerWorld ?: return
	world.players.forEach { ServerPlayNetworking.send(it, payload) }
}
