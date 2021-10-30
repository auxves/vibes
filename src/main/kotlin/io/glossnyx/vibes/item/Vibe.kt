package io.glossnyx.vibes.item

import io.glossnyx.vibes.Vibes
import io.glossnyx.vibes.network.packet.*
import io.glossnyx.vibes.util.*
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.*
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.world.World
import java.util.*

private val settings: Item.Settings = Item.Settings()
	.group(ItemGroup.MISC)
	.maxCount(1)

object Vibe : Item(settings) {
	val id = Vibes.id("vibe")

	override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?) {
		discOf(stack)?.item?.appendTooltip(stack, world, tooltip, context)
	}
	
	override fun onStackClicked(stack: ItemStack?, slot: Slot?, clickType: ClickType?, player: PlayerEntity?): Boolean {
		if (stack == null) return false
		if (slot == null) return false
		if (player == null) return false
		if (vibeTypeOf(stack) != VibeType.VIBE) return false
		if (clickType != ClickType.RIGHT) return false

		val uuid = uuidOf(stack) ?: UUID.randomUUID()
		setUUIDOf(stack, uuid)

		return when (val item = slot.stack.item) {
			is MusicDiscItem -> {
				val disc = discOf(stack) ?: ItemStack.EMPTY
				setDiscOf(stack, slot.stack)
				slot.stack = disc
				slot.markDirty()

				if (player.world.isClient) send(Play(uuid, player.uuid, item.sound.id))

				true
			}

			Items.AIR -> {
				slot.stack = discOf(stack) ?: return false
				setDiscOf(stack, ItemStack.EMPTY)
				slot.markDirty()

				if (player.world.isClient) send(Stop(uuid))

				true
			}

			else -> false
		}
	}
}