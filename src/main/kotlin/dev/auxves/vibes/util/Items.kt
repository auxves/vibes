package dev.auxves.vibes.util

import dev.auxves.vibes.item.Vibe
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import java.util.UUID

enum class VibeType {
	VIBE,
	SHULKER
}

fun isPlaying(stack: ItemStack): Boolean = when (vibeTypeOf(stack)) {
	VibeType.VIBE -> stack.disc != null
	VibeType.SHULKER -> shulkerInventoryOf(stack).any(::isPlaying)
	else -> false
}

fun ensureUuid(stack: ItemStack): ItemStack {
	if (stack.item is Vibe && !stack.orCreateNbt.containsUuid(Tags.UUID)) {
		stack.uuid = UUID.randomUUID()
	}

	return stack
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
}.map(::ensureUuid).filter(::isPlaying)
