package io.glossnyx.vibes.mixin;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.inventory.EnderChestInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderChestInventory.class)
public interface EnderChestInventoryAccessor {
	@Accessor("activeBlockEntity")
	EnderChestBlockEntity getActiveBlockEntity();
}
