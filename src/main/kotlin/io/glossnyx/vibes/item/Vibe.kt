package io.glossnyx.vibes.item

import io.glossnyx.vibes.util.Tags
import io.glossnyx.vibes.Vibes
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World

private val settings: Item.Settings = Item.Settings()
	.group(ItemGroup.MISC)
	.maxCount(1)

object Vibe : Item(settings) {
	val id = Vibes.id("vibe")

	override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?) {
		if (tooltip == null || stack == null) return

		val tag = stack.getSubTag(Tags.DISC) ?: return

		ItemStack.fromTag(tag).item.appendTooltip(stack, world, tooltip, context)
	}
}