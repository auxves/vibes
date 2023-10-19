package dev.auxves.vibes.server

import dev.auxves.vibes.network.packet.*
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

fun initServer() {
	register<Play> { data, player -> player.world.sendAll(data) }
	register<Stop> { data, player -> player.world.sendAll(data) }

	ServerPlayConnectionEvents.DISCONNECT.register { handler, server ->
		server.execute { onDisconnect(handler.player) }
	}
}