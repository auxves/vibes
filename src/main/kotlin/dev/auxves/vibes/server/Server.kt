package dev.auxves.vibes.server

import dev.auxves.vibes.network.packet.*

fun initServer() {
	register<Play> { data, player -> player.send(data) }
	register<Stop> { data, player -> player.send(data) }
}