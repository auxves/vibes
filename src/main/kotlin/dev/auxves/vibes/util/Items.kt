package dev.auxves.vibes.util

import dev.auxves.vibes.item.Vibe
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack

fun isPlaying(stack: ItemStack): Boolean = when {
	stack.item is Vibe -> stack.disc != null
	stack.contains(DataComponentTypes.CONTAINER) -> containerInventoryOf(stack).any(::isPlaying)
	else -> false
}

fun vibesIn(stack: ItemStack): Sequence<ItemStack> = when {
	stack.item is Vibe -> sequenceOf(stack)
	stack.contains(DataComponentTypes.CONTAINER) -> containerInventoryOf(stack).asSequence().flatMap(::vibesIn)
	else -> sequenceOf()
}.filter(::isPlaying)
