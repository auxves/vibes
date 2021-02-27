package io.glossnyx.vibes.util

import io.glossnyx.vibes.item.Vibe
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.inventory.Inventories
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

enum class VibeType {
	VIBE,
	SHULKER
}

fun shulkerInventoryOf(stack: ItemStack): List<ItemStack>? {
	val tag = stack.getSubTag("BlockEntityTag") ?: return null
	val inventory = DefaultedList.ofSize(27, ItemStack.EMPTY)
	Inventories.fromTag(tag, inventory)
	return inventory.toList()
}

fun isPlaying(stack: ItemStack): Boolean {
	return when (vibeTypeOf(stack)) {
		VibeType.VIBE -> uuidOf(stack) != null && discOf(stack) != null
		VibeType.SHULKER -> shulkerInventoryOf(stack)?.find(::isPlaying) != null
		else -> false
	}
}

fun vibeTypeOf(stack: ItemStack): VibeType? {
	return when (val item = stack.item) {
		is Vibe -> VibeType.VIBE
		is BlockItem -> if (item.block is ShulkerBoxBlock) VibeType.SHULKER else null
		else -> null
	}
}

fun forEachVibe(stack: ItemStack, fn: (stack: ItemStack) -> Unit) {
	if (!isPlaying(stack)) return

	when (vibeTypeOf(stack)) {
		VibeType.VIBE -> fn(stack)
		VibeType.SHULKER -> shulkerInventoryOf(stack)?.filter(::isPlaying)?.forEach { forEachVibe(it, fn) }
	}
}