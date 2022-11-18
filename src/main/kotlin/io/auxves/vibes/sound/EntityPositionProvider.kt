package io.auxves.vibes.sound

import net.minecraft.entity.Entity

class EntityPositionProvider(val entity: Entity) : PositionProvider {
	override val x get() = entity.x
	override val y get() = entity.y
	override val z get() = entity.z
}