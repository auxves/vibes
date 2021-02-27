package io.glossnyx.vibes.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import java.util.UUID

object Tags {
	const val DISC = "Disc"
	const val UUID = "VibeUUID"
}

fun uuidOf(stack: ItemStack) = stack.tag?.let {
	if (it.containsUuid(Tags.UUID)) it.getUuid(Tags.UUID) else null
}

fun setUUIDOf(stack: ItemStack, uuid: UUID) = stack.orCreateTag.putUuid(Tags.UUID, uuid)

fun discOf(stack: ItemStack) = stack.getSubTag(Tags.DISC)?.let {
	ItemStack.fromTag(it)
}

fun setDiscOf(stack: ItemStack, disc: ItemStack) {
	if (disc == ItemStack.EMPTY) return stack.removeSubTag(Tags.DISC)
	stack.putSubTag(Tags.DISC, disc.toTag(CompoundTag()))
}
