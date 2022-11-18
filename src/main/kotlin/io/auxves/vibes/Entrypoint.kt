package io.auxves.vibes

import io.auxves.vibes.item.Vibe
import io.auxves.vibes.network.packet.*
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Vibes {
	fun id(path: String) = Identifier("vibes", path)
}

@Suppress("unused")
fun init() {
	Registry.register(Registry.ITEM, Vibe.id, Vibe)

	io.auxves.vibes.server.initServer()
}
