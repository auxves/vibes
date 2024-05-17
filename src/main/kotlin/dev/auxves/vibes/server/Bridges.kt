package dev.auxves.vibes.server

import dev.auxves.vibes.mixin.EnderChestInventoryAccessor
import dev.auxves.vibes.network.sendAll
import dev.auxves.vibes.network.payloads.*
import dev.auxves.vibes.util.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.DataComponentTypes
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

fun onBreak(entity: Entity) {
	if (entity !is ItemEntity) return
	if (!entity.stack.contains(DataComponentTypes.CONTAINER)) return

	changePosition(entity.stack, entity)
}

fun stopPlaying(stack: ItemStack, world: World) {
	vibesIn(stack).map { it.uuid }.forEach {
		world.sendAll(Stop(it))
	}
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

fun changeDistance(stack: ItemStack, world: World, distance: Float) {
	vibesIn(stack).map { it.uuid }.forEach {
		world.sendAll(ChangeDistance(it, distance))
	}
}