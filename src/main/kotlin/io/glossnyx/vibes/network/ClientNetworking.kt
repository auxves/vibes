package io.glossnyx.vibes.network

import io.glossnyx.vibes.network.packet.*
import io.glossnyx.vibes.sound.*
import io.glossnyx.vibes.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.MusicDiscItem
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.sound.SoundEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.UUID

object ClientNetworking {
	private val client = MinecraftClient.getInstance()
	private val vibeInstances = mutableListOf<VibeInstance>()

	private fun getInstance(uuid: UUID) = vibeInstances.find { it.uuid == uuid }

	private fun getEntity(uuid: UUID) = client.world?.let { world ->
		world.players.plus(world.entities).find { it.uuid == uuid }
	} ?: throw Exception("entity with uuid $uuid not found")

	private fun stop(uuid: UUID) = getInstance(uuid)?.let {
		client.soundManager.stop(it)
		vibeInstances.remove(it)
	}

	fun onRightClick(slot: Slot, clickType: Int, actionType: SlotActionType, player: PlayerEntity, cir: CallbackInfoReturnable<ItemStack>) {
		if (!player.world.isClient) return
		if (clickType != 1) return
		if (actionType != SlotActionType.PICKUP) return

		val cursorStack = player.inventory.cursorStack
		if (vibeTypeOf(cursorStack) != VibeType.VIBE) return

		when (val item = slot.stack.item) {
			is MusicDiscItem -> {
				val uuid = uuidOf(cursorStack) ?: UUID.randomUUID()
				val newSlotStack = discOf(cursorStack) ?: ItemStack.EMPTY
				setDiscOf(cursorStack, slot.stack)

				if (player.isCreative) slot.stack = newSlotStack

				send(RightClickPlay(slot.id, uuid, item.sound.id))
			}

			Items.AIR -> {
				val disc = discOf(cursorStack) ?: return
				if (player.isCreative) slot.stack = disc
				setDiscOf(cursorStack, ItemStack.EMPTY)

				send(RightClickStop(slot.id))
			}

			else -> return
		}

		cir.returnValue = ItemStack.EMPTY
		cir.cancel()
	}

	fun init() {
		register<Play> { data ->
			stop(data.uuid)

			val entity = getEntity(data.entityUUID)
			val instance = VibeInstance(data.uuid, EntityPositionProvider(entity), SoundEvent(data.identifier))
			vibeInstances.add(instance)

			client.soundManager.play(instance)
		}

		register<Stop> { data -> stop(data.uuid) }

		register<ChangePositionEntity> { data ->
			getInstance(data.uuid)?.let {
				GlobalScope.launch {
					val entity = runCatching { getEntity(data.entityUUID) }.getOrElse {
						delay(100)
						getEntity(data.entityUUID)
					}

					it.position = EntityPositionProvider(entity)
				}
			}
		}

		register<ChangePositionBlock> { data ->
			getInstance(data.uuid)?.let {
				it.position = BlockPositionProvider(data.blockPos)
			}
		}
	}
}
