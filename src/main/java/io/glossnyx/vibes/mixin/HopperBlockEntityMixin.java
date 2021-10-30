package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import io.glossnyx.vibes.util.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
class HopperBlockEntityMixin {
	HopperBlockEntity that = HopperBlockEntity.class.cast(this);

	@Inject(method = "setStack", at = @At("HEAD"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		ServerNetworking.INSTANCE.changePositionProvider(stack, that);
	}

	@Redirect(
		method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V")
	)
	private static void onRemoveItem(ItemEntity entity) {
		if (!entity.world.isClient && ItemsKt.isPlaying(entity.getStack())) {
			entity.remove(Entity.RemovalReason.DISCARDED);
		} else {
			entity.discard();
		}
	}
}
