package dev.auxves.vibes

import dev.auxves.vibes.item.Vibe
import dev.auxves.vibes.server.initServer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object Vibes {
	fun id(path: String) = Identifier("vibes", path)
}

@Suppress("unused")
fun init() {
	Registry.register(Registries.ITEM, Vibe.id, Vibe)

	ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register {
		it.add(Vibe)
	}

	initServer()
}
