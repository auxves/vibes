package dev.auxves.vibes.util

import net.minecraft.component.DataComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.util.Uuids
import java.util.*

object DataComponents {
	val UUID: DataComponentType<UUID> = DataComponentType.builder<UUID>()
		.codec(Uuids.CODEC)
		.packetCodec(Uuids.PACKET_CODEC)
		.build()

	val DISC: DataComponentType<ItemStack> = DataComponentType.builder<ItemStack>()
		.codec(ItemStack.CODEC)
		.packetCodec(ItemStack.PACKET_CODEC)
		.build()
}

var ItemStack.uuid: UUID
	get() = get(DataComponents.UUID) ?: run {
		uuid = UUID.randomUUID()
		uuid
	}
	set(new) { set(DataComponents.UUID, new) }

var ItemStack.disc: ItemStack?
	get() = get(DataComponents.DISC)
	set(stack) {
		if (stack == null || stack == ItemStack.EMPTY) remove(DataComponents.DISC)
		else set(DataComponents.DISC, stack)
	}

fun containerInventoryOf(stack: ItemStack): List<ItemStack> {
	val data = stack.get(DataComponentTypes.CONTAINER) ?: return listOf()
	return data.stream().toList()
}