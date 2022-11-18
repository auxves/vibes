package io.auxves.vibes.client

import io.auxves.vibes.mixin.SoundManagerAccessor
import io.auxves.vibes.mixin.SoundSystemAccessor
import io.auxves.vibes.network.packet.*
import io.auxves.vibes.sound.BlockPositionProvider
import io.auxves.vibes.sound.EntityPositionProvider
import io.auxves.vibes.sound.VibeInstance
import kotlinx.coroutines.*
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvent
import java.util.UUID

private val client = MinecraftClient.getInstance()
private val instances = mutableMapOf<UUID, VibeInstance>()

private fun getEntity(uuid: UUID) = client.world?.let { world ->
	world.players.asSequence().plus(world.entities).find { it.uuid == uuid }
}

private fun stop(uuid: UUID) = instances[uuid]?.let {
	client.soundManager.stop(it)
	instances.remove(uuid)
}

@Suppress("unused")
fun init() {
	val scope = CoroutineScope(Dispatchers.Unconfined)

	val sources by lazy {
		val soundManager = client.soundManager as SoundManagerAccessor
		val soundSystem = soundManager.soundSystem as SoundSystemAccessor
		soundSystem.sources
	}

	register<Play> { data ->
		stop(data.uuid)

		val entity = getEntity(data.player) ?: return@register
		val instance = VibeInstance(EntityPositionProvider(entity), SoundEvent(data.sound))
		instances[data.uuid] = instance

		client.soundManager.play(instance)
	}

	register<Stop> { data -> stop(data.uuid) }

	register<ChangePositionEntity> { data ->
		instances[data.uuid]?.let {
			scope.launch {
				delay(100)
				val entity = getEntity(data.entityUUID) ?: return@launch
				it.position = EntityPositionProvider(entity)
			}
		}
	}

	register<ChangePositionBlock> { data ->
		instances[data.uuid]?.let {
			it.position = BlockPositionProvider(data.blockPos)
		}
	}

	register<ChangeDistance> { data ->
		instances[data.uuid]?.let {
			sources[it]?.run { source -> source.setAttenuation(data.distance) }
		}
	}
}