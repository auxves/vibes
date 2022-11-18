package io.auxves.vibes.sound

import net.minecraft.client.sound.MovingSoundInstance
import net.minecraft.client.sound.SoundInstance
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent

class VibeInstance(var position: PositionProvider, event: SoundEvent) :
	MovingSoundInstance(event, SoundCategory.RECORDS, SoundInstance.createRandom())
{
	init {
		tick()
	}

	override fun tick() {
		x = position.x
		y = position.y
		z = position.z
	}
}
