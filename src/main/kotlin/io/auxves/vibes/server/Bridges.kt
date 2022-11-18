package io.auxves.vibes.server

import io.auxves.vibes.mixin.EnderChestInventoryAccessor
import io.auxves.vibes.network.packet.*
import io.auxves.vibes.util.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EnderChestInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.world.World

fun onDisconnect(player: PlayerEntity) {
	player.inventory.main.forEach { stopPlaying(it, player.world) }
}

fun handleEnderChest(inventory: Inventory, stack: ItemStack) {
	if (inventory !is EnderChestInventory) return

	val world = (inventory as? EnderChestInventoryAccessor)?.activeBlockEntity?.world ?: return

	stopPlaying(stack, world)
}

fun onBreakShulkerBox(entity: Entity) {
	if (entity !is ItemEntity) return
	if (vibeTypeOf(entity.stack) != VibeType.SHULKER) return

	changePosition(entity.stack, entity)
}

fun stopPlaying(stack: ItemStack, world: World) {
	vibesIn(stack).map { it.uuid }.forEach { world.sendAll(Stop(it)) }
}

fun changePosition(stack: ItemStack, block: BlockEntity) {
	val world = block.world ?: return

	vibesIn(stack).map { it.uuid }.forEach {
		world.sendAll(ChangePositionBlock(it, block.pos))
	}
}

fun changePosition(stack: ItemStack, entity: Entity) {
	vibesIn(stack).map { it.uuid }.forEach {
		entity.world.sendAll(ChangePositionEntity(it, entity.uuid))
	}
}

fun setDistance(world: World, stack: ItemStack, distance: Float) {
	vibesIn(stack).map { it.uuid }.forEach {
		world.sendAll(ChangeDistance(it, distance))
	}
}