package dev.auxves.vibes

import dev.auxves.vibes.item.Vibe
import dev.auxves.vibes.network.initNetwork
import dev.auxves.vibes.server.initServer
import dev.auxves.vibes.util.DataComponents
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
	Registry.register(Registries.DATA_COMPONENT_TYPE, Vibes.id("uuid"), DataComponents.UUID)
	Registry.register(Registries.DATA_COMPONENT_TYPE, Vibes.id("disc"), DataComponents.DISC)

	Registry.register(Registries.ITEM, Vibe.id, Vibe)

	ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register {
		it.add(Vibe)
	}

	initNetwork()
	initServer()
}
