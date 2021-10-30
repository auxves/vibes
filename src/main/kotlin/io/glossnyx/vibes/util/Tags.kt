package io.glossnyx.vibes.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import java.util.UUID

object Tags {
	const val DISC = "Disc"
	const val UUID = "VibeUUID"
}

fun uuidOf(stack: ItemStack) = stack.nbt?.let {
	if (it.containsUuid(Tags.UUID)) it.getUuid(Tags.UUID) else null
}

fun setUUIDOf(stack: ItemStack, uuid: UUID) = stack.orCreateNbt.putUuid(Tags.UUID, uuid)

fun discOf(stack: ItemStack?) = stack?.getSubNbt(Tags.DISC)?.let {
	ItemStack.fromNbt(it)
}

fun setDiscOf(stack: ItemStack, disc: ItemStack) {
	if (disc == ItemStack.EMPTY) return stack.removeSubNbt(Tags.DISC)
	stack.setSubNbt(Tags.DISC, disc.writeNbt(NbtCompound()))
}
