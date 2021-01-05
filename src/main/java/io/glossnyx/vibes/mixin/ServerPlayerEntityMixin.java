package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
class ServerPlayerEntityMixin {
	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("RETURN"))
	private void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
		ItemEntity entity = cir.getReturnValue();
		if (entity == null) return;
		ServerNetworking.INSTANCE.changePositionProvider(entity.getStack(), entity);
	}

	@Inject(method = "sendPickup", at = @At("HEAD"))
	private void onPickup(Entity item, int count, CallbackInfo ci) {
		ServerNetworking.INSTANCE.onPickup(ServerPlayerEntity.class.cast(this), item);
	}
}
