package io.glossnyx.vibes.sound

import net.minecraft.entity.Entity

class EntityPositionProvider(private val entity: Entity) : PositionProvider {
	override fun getX() = entity.x
	override fun getY() = entity.y
	override fun getZ() = entity.z
}