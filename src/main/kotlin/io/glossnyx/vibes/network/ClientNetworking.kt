package io.glossnyx.vibes.network

import io.glossnyx.vibes.network.packet.*
import io.glossnyx.vibes.sound.*
import kotlinx.coroutines.*
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvent
import java.util.UUID

object ClientNetworking {
	private val client = MinecraftClient.getInstance()
	private val vibeInstances = mutableListOf<VibeInstance>()

	private fun getInstance(uuid: UUID) = vibeInstances.find { it.uuid == uuid }

	private fun getEntity(uuid: UUID) = client.world?.let { world ->
		world.players.plus(world.entities).find { it.uuid == uuid }
	}

	private fun stop(uuid: UUID) = getInstance(uuid)?.let {
		client.soundManager.stop(it)
		vibeInstances.remove(it)
	}

	fun init() {
		val scope = CoroutineScope(Dispatchers.Unconfined)

		register<Play> { data ->
			stop(data.uuid)

			val entity = getEntity(data.entityUUID) ?: return@register
			val instance = VibeInstance(data.uuid, EntityPositionProvider(entity), SoundEvent(data.identifier))
			vibeInstances.add(instance)

			client.soundManager.play(instance)
		}

		register<Stop> { data -> stop(data.uuid) }

		register<ChangePositionEntity> { data ->
			getInstance(data.uuid)?.let {
				scope.launch {
					delay(100)
					val entity = getEntity(data.entityUUID) ?: return@launch
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
