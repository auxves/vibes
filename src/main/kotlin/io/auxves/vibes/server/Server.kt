package io.auxves.vibes.server

import io.auxves.vibes.network.packet.*

fun initServer() {
	register<Play> { data, player -> player.send(data) }
	register<Stop> { data, player -> player.send(data) }
}