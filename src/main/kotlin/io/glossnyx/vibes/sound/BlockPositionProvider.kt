package io.glossnyx.vibes.sound

import net.minecraft.util.math.BlockPos

class BlockPositionProvider(blockPos: BlockPos) : PositionProvider {
	private val x = blockPos.x.toDouble() + 0.5
	private val y = blockPos.y.toDouble() + 0.5
	private val z = blockPos.z.toDouble() + 0.5

	override fun getX() = x
	override fun getY() = y
	override fun getZ() = z
}