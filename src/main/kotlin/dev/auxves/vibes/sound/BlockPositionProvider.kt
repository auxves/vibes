package dev.auxves.vibes.sound

import net.minecraft.util.math.BlockPos

class BlockPositionProvider(blockPos: BlockPos) : PositionProvider {
	override val x = blockPos.x.toDouble() + 0.5
	override val y = blockPos.y.toDouble() + 0.5
	override val z = blockPos.z.toDouble() + 0.5
}