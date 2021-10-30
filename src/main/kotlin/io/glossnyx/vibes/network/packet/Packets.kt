package io.glossnyx.vibes.network.packet

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.World
import kotlin.reflect.full.companionObjectInstance

interface Packet {
	val buffer: PacketByteBuf
}

interface PacketCompanion {
	val id: Identifier
	fun fromBuf(buf: PacketByteBuf): Packet
}

inline fun <reified T : Packet> register(crossinline handler: (packet: T) -> Unit) {
	val companion = T::class.companionObjectInstance as PacketCompanion

	ClientPlayNetworking.registerGlobalReceiver(companion.id) { client, _, buf, _ ->
		val packet = companion.fromBuf(buf) as T
		client.execute { handler(packet) }
	}
}

inline fun <reified T : Packet> register(crossinline handler: (packet: T, player: ServerPlayerEntity) -> Unit) {
	val companion = T::class.companionObjectInstance as PacketCompanion

	ServerPlayNetworking.registerGlobalReceiver(companion.id) { server, player, _, buf, _ ->
		val packet = companion.fromBuf(buf) as T
		server.execute { handler(packet, player) }
	}
}

fun send(player: ServerPlayerEntity, packet: Packet) {
	val companion = packet::class.companionObjectInstance as PacketCompanion
	ServerPlayNetworking.send(player, companion.id, packet.buffer)
}

fun send(packet: Packet) {
	val companion = packet::class.companionObjectInstance as PacketCompanion
	ClientPlayNetworking.send(companion.id, packet.buffer)
}

fun World.sendAll(packet: Packet) = (this as? ServerWorld)?.players?.forEach { send(it, packet) }

fun createPacket(block: PacketByteBuf.() -> Unit) = PacketByteBufs.create().apply(block)!!
