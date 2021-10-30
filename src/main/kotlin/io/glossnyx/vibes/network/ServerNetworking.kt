package io.glossnyx.vibes.network

import io.glossnyx.vibes.network.packet.*
import io.glossnyx.vibes.mixin.EnderChestInventoryAccessor
import io.glossnyx.vibes.util.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EnderChestInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.world.World

object ServerNetworking {
	fun onDisconnect(player: PlayerEntity) {
		player.inventory.main.forEach { stopPlaying(it, player.world) }
	}

	fun stopPlaying(stack: ItemStack, world: World) {
		forEachVibe(stack) {
			val uuid = uuidOf(it) ?: return@forEachVibe
			world.sendAll(Stop(uuid))
		}
	}

	fun handleEnderChest(inventory: Inventory, stack: ItemStack) {
		if (inventory !is EnderChestInventory) return
		if (!isPlaying(stack)) return

		val world = (inventory as? EnderChestInventoryAccessor)?.activeBlockEntity?.world ?: return
		if (world.isClient) return

		stopPlaying(stack, world)
	}

	fun onBreakShulkerBox(entity: Entity) {
		if (entity !is ItemEntity) return
		if (vibeTypeOf(entity.stack) != VibeType.SHULKER) return

		changePositionProvider(entity.stack, entity)
	}

	fun onPickup(player: PlayerEntity, entity: Entity?) {
		if (entity !is ItemEntity) return

		changePositionProvider(entity.stack, player)
	}

	fun changePositionProvider(stack: ItemStack, block: BlockEntity?) {
		val world = block?.world ?: return
		if (world.isClient) return

		forEachVibe(stack) {
			val uuid = uuidOf(it) ?: return@forEachVibe
			world.sendAll(ChangePositionBlock(uuid, block.pos))
		}
	}

	fun changePositionProvider(stack: ItemStack, entity: Entity?) {
		if (entity == null) return
		if (entity.world.isClient) return

		forEachVibe(stack) {
			val uuid = uuidOf(it) ?: return@forEachVibe
			entity.world.sendAll(ChangePositionEntity(uuid, entity.uuid))
		}
	}

	fun init() {
		register<Play> { data, player -> player.world.sendAll(data) }
		register<Stop> { data, player -> player.world.sendAll(data) }
	}
}
