package dev.auxves.vibes.server

import dev.auxves.vibes.network.payloads.*
import dev.auxves.vibes.network.sendAll
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

fun initServer() {
	ServerPlayNetworking.registerGlobalReceiver(Play.ID) { data, ctx -> ctx.player().world.sendAll(data)  }
	ServerPlayNetworking.registerGlobalReceiver(Stop.ID) { data, ctx -> ctx.player().world.sendAll(data)  }

	ServerPlayConnectionEvents.DISCONNECT.register { handler, server ->
		server.execute { onDisconnect(handler.player) }
	}
}