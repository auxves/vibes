package io.glossnyx.vibes.network

import io.glossnyx.vibes.network.packet.*
import io.glossnyx.vibes.sound.*
import io.glossnyx.vibes.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.MinecraftClient
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
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
	private val vibeInstances = mutableListOf<VibeInstance>()

	private fun getInstance(uuid: UUID): VibeInstance? {
		return vibeInstances.find { it.uuid == uuid }
	}

	private fun getEntity(world: ClientWorld, uuid: UUID): Entity {
		return world.players.find { it.uuid == uuid }
			?: world.entities.find { it.uuid == uuid }
			?: throw Exception("entity with uuid $uuid not found")
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
				if (player.isCreative) slot.stack = discOf(cursorStack)

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
			val client = MinecraftClient.getInstance()

			getInstance(data.uuid)?.let {
				client.soundManager.stop(it)
				vibeInstances.remove(it)
			}

			val world = client.world ?: throw Exception("no world")
			val entity = getEntity(world, data.entityUUID)

			val instance = VibeInstance(data.uuid, EntityPositionProvider(entity), SoundEvent(data.identifier))
			vibeInstances.add(instance)

			client.soundManager.play(instance)
		}

		register<Stop> { data ->
			getInstance(data.uuid)?.let {
				MinecraftClient.getInstance().soundManager.stop(it)
				vibeInstances.remove(it)
			}
		}

		register<ChangePositionEntity> { data ->
			getInstance(data.uuid)?.let {
				GlobalScope.launch {
					val world = MinecraftClient.getInstance().world ?: throw Exception("no world")

					val entity = runCatching { getEntity(world, data.entityUUID) }.getOrElse {
						delay(100)
						getEntity(world, data.entityUUID)
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
