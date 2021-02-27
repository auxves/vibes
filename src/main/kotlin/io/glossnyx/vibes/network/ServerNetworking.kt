package io.glossnyx.vibes.network

import io.glossnyx.vibes.network.packet.*
import io.glossnyx.vibes.mixin.EnderChestInventoryAccessor
import io.glossnyx.vibes.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EnderChestInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.world.World

object ServerNetworking {
	/** Stops sounds when source player disconnects */
	fun onDisconnect(player: PlayerEntity) {
		player.inventory.main.forEach { stopPlaying(it, player.world) }
	}

	/** Stops playing sounds */
	fun stopPlaying(stack: ItemStack, world: World) {
		forEachVibe(stack) {
			world.sendAll(Stop(uuidOf(it) ?: return@forEachVibe))
		}
	}

	/** When vibe is placed into ender chest */
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

	/** When vibe is picked by a player */
	fun onPickup(player: PlayerEntity, entity: Entity?) {
		if (entity == null) return
		if (entity !is ItemEntity) return

		changePositionProvider(entity.stack, player)
	}

	/** When player quick moves vibe out of block */
	fun onQuickMove(slot: Slot, actionType: SlotActionType, player: PlayerEntity) {
		if (player.world.isClient) return
		if (actionType != SlotActionType.QUICK_MOVE) return
		if (!isPlaying(slot.stack)) return
		if (player.inventory.main.find { it == slot.stack } != null) return

		changePositionProvider(slot.stack, player)
	}

	/** When player removes vibe from block */
	fun changePositionProvider(stack: ItemStack, world: World?) {
		if (world == null) return
		if (world.isClient) return
		if (!isPlaying(stack)) return

		GlobalScope.launch {
			delay(100)
			val player = world.players.find { it.inventory.cursorStack == stack } ?: return@launch
			changePositionProvider(stack, player)
		}
	}

	/** When player puts vibe into block */
	fun changePositionProvider(stack: ItemStack, block: BlockEntity?, oldStack: ItemStack?) {
		val world = block?.world ?: return
		if (world.isClient) return

		if (oldStack != null) changePositionProvider(oldStack, world)

		forEachVibe(stack) {
			val uuid = uuidOf(it) ?: return@forEachVibe
			world.sendAll(ChangePositionBlock(uuid, block.pos))
		}
	}

	/** When player puts vibe into entity */
	fun changePositionProvider(stack: ItemStack, entity: Entity?) {
		if (entity == null) return
		if (entity.world.isClient) return

		forEachVibe(stack) {
			val uuid = uuidOf(it) ?: return@forEachVibe
			entity.world.sendAll(ChangePositionEntity(uuid, entity.uuid))
		}
	}

	fun init() {
		register<RightClickPlay> { data, player ->
			val slots = player.currentScreenHandler.slots
			val slot = if (data.slotID >= slots.size - 1) slots[data.slotID - 9] else slots[data.slotID]
			val cursorStack = player.inventory.cursorStack

			if (uuidOf(cursorStack) == null) setUUIDOf(cursorStack, data.uuid)

			val newCursorStack = discOf(cursorStack) ?: ItemStack.EMPTY

			setDiscOf(cursorStack, slot.stack)

			if (player.isCreative) {
				slot.stack = newCursorStack
			} else {
				slot.stack = cursorStack.copy()
				player.inventory.cursorStack = newCursorStack
			}

			slot.markDirty()

			player.world.sendAll(Play(data.uuid, player.uuid, data.identifier))
		}

		register<RightClickStop> { data, player ->
			val slots = player.currentScreenHandler.slots
			val slot = if (data.slotID >= slots.size - 1) slots[data.slotID - 9] else slots[data.slotID]
			val cursorStack = player.inventory.cursorStack

			val uuid = uuidOf(cursorStack) ?: return@register
			val disc = discOf(cursorStack) ?: return@register

			if (player.isCreative) {
				slot.stack = disc
				setDiscOf(cursorStack, ItemStack.EMPTY)
			} else {
				player.inventory.cursorStack = disc
				slot.stack = cursorStack.copy()
				setDiscOf(slot.stack, ItemStack.EMPTY)
			}

			slot.markDirty()

			player.world.sendAll(Stop(uuid))
		}
	}
}
