package io.auxves.vibes.util

import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.collection.DefaultedList
import java.util.UUID

object Tags {
	const val DISC = "Disc"
	const val UUID = "VibeUUID"

	const val SHULKER = "BlockEntityTag"
}

var ItemStack.uuid: UUID
	get() {
		if (orCreateNbt.containsUuid(Tags.UUID)) return orCreateNbt.getUuid(Tags.UUID)
		val new = UUID.randomUUID()
		uuid = new
		return new
	}
	set(new) = orCreateNbt.putUuid(Tags.UUID, new)

var ItemStack.disc: ItemStack?
	get() = getSubNbt(Tags.DISC)?.let { ItemStack.fromNbt(it) }
	set(stack) {
		if (stack == null || stack == ItemStack.EMPTY) return removeSubNbt(Tags.DISC)
		setSubNbt(Tags.DISC, stack.writeNbt(NbtCompound()))
	}

fun shulkerInventoryOf(stack: ItemStack): List<ItemStack> {
	val nbt = stack.getSubNbt(Tags.SHULKER) ?: return listOf()
	val inventory = DefaultedList.ofSize(27, ItemStack.EMPTY)
	Inventories.readNbt(nbt, inventory)
	return inventory.toList()
}