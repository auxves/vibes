package io.auxves.vibes.util

import io.auxves.vibes.item.Vibe
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack

enum class VibeType {
	VIBE,
	SHULKER
}

fun isPlaying(stack: ItemStack): Boolean = when (vibeTypeOf(stack)) {
	VibeType.VIBE -> stack.disc != null
	VibeType.SHULKER -> shulkerInventoryOf(stack).any(::isPlaying)
	else -> false
}

fun vibeTypeOf(stack: ItemStack): VibeType? = when {
	stack.item is Vibe -> VibeType.VIBE
	(stack.item as? BlockItem)?.block is ShulkerBoxBlock -> VibeType.SHULKER
	else -> null
}

fun vibesIn(stack: ItemStack): Sequence<ItemStack> = when (vibeTypeOf(stack)) {
	VibeType.VIBE -> sequenceOf(stack)
	VibeType.SHULKER -> shulkerInventoryOf(stack).asSequence().flatMap(::vibesIn)
	else -> sequenceOf()
}.filter(::isPlaying)
