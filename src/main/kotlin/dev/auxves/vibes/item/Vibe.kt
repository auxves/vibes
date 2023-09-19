package dev.auxves.vibes.item

import dev.auxves.vibes.Vibes
import dev.auxves.vibes.network.packet.*
import dev.auxves.vibes.util.disc
import dev.auxves.vibes.util.isPlaying
import dev.auxves.vibes.util.uuid
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Blocks
import net.minecraft.block.JukeboxBlock
import net.minecraft.block.entity.JukeboxBlockEntity
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

private val settings = FabricItemSettings()
	.rarity(Rarity.RARE)
	.maxCount(1)

object Vibe : Item(settings) {
	val id = Vibes.id("vibe")

	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		stack.disc?.item?.appendTooltip(stack, world, tooltip, context)

		val helpKey = if (stack.disc == null) ".play" else ".stop"
		tooltip.add(Text.translatable(translationKey + helpKey).formatted(Formatting.DARK_GRAY))
	}

	override fun onStackClicked(stack: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity): Boolean {
		if (clickType != ClickType.RIGHT) return false

		val packet = when (val item = slot.stack.item) {
			is MusicDiscItem -> {
				val disc = stack.disc ?: ItemStack.EMPTY
				stack.disc = slot.stack
				slot.stack = disc
				slot.markDirty()

				Play(stack.uuid, player.uuid, item.sound.id)
			}

			Items.AIR -> {
				slot.stack = stack.disc ?: return false
				stack.disc = null
				slot.markDirty()

				Stop(stack.uuid)
			}

			else -> return false
		}

		if (player.world.isClient) send(packet)

		return true
	}

	override fun useOnBlock(context: ItemUsageContext): ActionResult {
		val block = Blocks.JUKEBOX as JukeboxBlock

		val blockPos = context.blockPos
		val stack = context.stack
		val world = context.world
		val player = context.player!!

		if (!isPlaying(stack)) return ActionResult.PASS

		val state = world.getBlockState(blockPos)
		if (!state.isOf(block) || state.get(JukeboxBlock.HAS_RECORD)) return ActionResult.PASS

		world.sendAll(ChangePositionBlock(stack.uuid, blockPos))
		world.sendAll(ChangeDistance(stack.uuid, 64F))

		val entity = world.getBlockEntity(blockPos) as JukeboxBlockEntity
		val blockState = world.getBlockState(blockPos)

		entity.stack = stack.copy()
		stack.decrement(1)

		world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(player, blockState))

		return ActionResult.success(world.isClient)
	}
}