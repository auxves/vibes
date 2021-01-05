package io.glossnyx.vibes.sound

import net.minecraft.client.sound.MovingSoundInstance
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import java.util.UUID

class VibeInstance(val uuid: UUID, var position: PositionProvider, event: SoundEvent) : MovingSoundInstance(event, SoundCategory.RECORDS) {
	init {
		tick()
	}

	override fun tick() {
		x = position.getX()
		y = position.getY()
		z = position.getZ()
	}
}
