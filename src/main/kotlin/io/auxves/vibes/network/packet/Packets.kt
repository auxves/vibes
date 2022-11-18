package io.auxves.vibes.network.packet

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

/** Registers a server receiver */
inline fun <reified T : Packet> register(crossinline handler: (packet: T) -> Unit) {
	val companion = T::class.companionObjectInstance as PacketCompanion

	ClientPlayNetworking.registerGlobalReceiver(companion.id) { client, _, buf, _ ->
		val packet = companion.fromBuf(buf) as T
		client.execute { handler(packet) }
	}
}

/** Sends a packet from the server to the client associated with this player */
fun ServerPlayerEntity.send(packet: Packet) {
	val companion = packet::class.companionObjectInstance as PacketCompanion
	ServerPlayNetworking.send(this, companion.id, packet.buffer)
}

/** Registers a client receiver */
inline fun <reified T : Packet> register(crossinline handler: (packet: T, player: ServerPlayerEntity) -> Unit) {
	val companion = T::class.companionObjectInstance as PacketCompanion

	ServerPlayNetworking.registerGlobalReceiver(companion.id) { server, player, _, buf, _ ->
		val packet = companion.fromBuf(buf) as T
		server.execute { handler(packet, player) }
	}
}

/** Sends a packet from the client to the server */
fun send(packet: Packet) {
	val companion = packet::class.companionObjectInstance as PacketCompanion
	ClientPlayNetworking.send(companion.id, packet.buffer)
}

/** Sends a packet to every client */
fun World.sendAll(packet: Packet) {
	val world = this as? ServerWorld ?: return
	world.players.forEach { it.send(packet) }
}

fun createPacket(block: PacketByteBuf.() -> Unit) = PacketByteBufs.create().apply(block)!!
