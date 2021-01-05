package io.glossnyx.vibes.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import java.util.UUID

object Tags {
	const val DISC = "Disc"
	const val UUID = "VibeUUID"
}

fun uuidOf(stack: ItemStack): UUID? {
	return if (stack.orCreateTag.containsUuid(Tags.UUID)) stack.orCreateTag.getUuid(Tags.UUID) else null
}

fun setUUIDOf(stack: ItemStack, uuid: UUID) = stack.orCreateTag.putUuid(Tags.UUID, uuid)

fun discOf(stack: ItemStack): ItemStack? {
	val tag = stack.getSubTag(Tags.DISC) ?: return null
	return ItemStack.fromTag(tag)
}

fun setDiscOf(stack: ItemStack, disc: ItemStack) {
	if (disc == ItemStack.EMPTY) return stack.removeSubTag(Tags.DISC)
	stack.putSubTag(Tags.DISC, disc.toTag(CompoundTag()))
}
