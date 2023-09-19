package dev.auxves.vibes.mixin;

import dev.auxves.vibes.server.BridgesKt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
class PlayerInventoryMixin {
	@Shadow @Final public PlayerEntity player;

	@Inject(method = "setStack", at = @At("HEAD"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		BridgesKt.changePosition(stack, player);
	}

	@Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"))
	private void onInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		BridgesKt.changePosition(stack, player);
	}
}
