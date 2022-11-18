package io.auxves.vibes.mixin;

import io.auxves.vibes.server.BridgesKt;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageMinecartEntity.class)
class StorageMinecartEntityMixin {
	StorageMinecartEntity that = StorageMinecartEntity.class.cast(this);

	@Inject(method = "setStack", at = @At("HEAD"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		BridgesKt.changePosition(stack, that);
	}
}
