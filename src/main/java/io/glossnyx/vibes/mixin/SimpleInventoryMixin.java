package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleInventory.class)
class SimpleInventoryMixin {
	@Inject(method = "setStack", at = @At("HEAD"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		ServerNetworking.INSTANCE.handleEnderChest((Inventory) this, stack);
	}
}