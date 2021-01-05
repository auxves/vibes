package io.glossnyx.vibes

import io.glossnyx.vibes.item.Vibe
import io.glossnyx.vibes.network.ClientNetworking
import io.glossnyx.vibes.network.ServerNetworking
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Vibes {
	fun id(path: String) = Identifier("vibes", path)
}

fun init() {
	Registry.register(Registry.ITEM, Vibe.id, Vibe)

	ServerNetworking.init()
}

fun initClient() {
	ClientNetworking.init()
}
