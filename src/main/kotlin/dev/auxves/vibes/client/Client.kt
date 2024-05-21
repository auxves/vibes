package dev.auxves.vibes.client

import dev.auxves.vibes.mixin.SoundManagerAccessor
import dev.auxves.vibes.mixin.SoundSystemAccessor
import dev.auxves.vibes.network.payloads.*
import dev.auxves.vibes.sound.BlockPositionProvider
import dev.auxves.vibes.sound.EntityPositionProvider
import dev.auxves.vibes.sound.VibeInstance
import kotlinx.coroutines.*
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvent
import java.util.*

private val client = MinecraftClient.getInstance()
private val instances = mutableMapOf<UUID, VibeInstance>()

private fun getEntity(uuid: UUID) = client.world?.let { world ->
	world.players.asSequence().plus(world.entities).find { it.uuid == uuid }
}

private fun stop(uuid: UUID) = instances[uuid]?.let {
	client.soundManager.stop(it)
	instances.remove(uuid)
}

fun init() {
	val scope = CoroutineScope(Dispatchers.Unconfined)

	val sources by lazy {
		val soundManager = client.soundManager as SoundManagerAccessor
		val soundSystem = soundManager.soundSystem as SoundSystemAccessor
		soundSystem.sources
	}

	ClientPlayNetworking.registerGlobalReceiver(Play.ID) { data, _ ->
		stop(data.uuid)

		val entity = getEntity(data.player) ?: return@registerGlobalReceiver
		val instance = VibeInstance(EntityPositionProvider(entity), SoundEvent.of(data.sound))
		instances[data.uuid] = instance

		client.soundManager.play(instance)
	}

	ClientPlayNetworking.registerGlobalReceiver(Stop.ID) { data, _ -> stop(data.uuid) }

	ClientPlayNetworking.registerGlobalReceiver(ChangePositionEntity.ID) { data, _ ->
		instances[data.uuid]?.let {
			scope.launch {
				delay(100)
				val entity = getEntity(data.entityUUID) ?: return@launch
				it.position = EntityPositionProvider(entity)
			}
		}
	}

	ClientPlayNetworking.registerGlobalReceiver(ChangePositionBlock.ID) { data, _ ->
		instances[data.uuid]?.let {
			it.position = BlockPositionProvider(data.blockPos)
		}
	}

	ClientPlayNetworking.registerGlobalReceiver(ChangeDistance.ID) { data, _ ->
		instances[data.uuid]?.let {
			sources[it]?.run { source -> source.setAttenuation(data.distance) }
		}
	}
}