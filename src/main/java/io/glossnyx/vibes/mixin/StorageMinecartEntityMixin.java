package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StorageMinecartEntity.class)
class StorageMinecartEntityMixin extends AbstractMinecartEntity {
	protected StorageMinecartEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "setStack", at = @At("HEAD"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		ServerNetworking.INSTANCE.changePositionProvider(stack, this);
	}

	@Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;", at = @At("RETURN"))
	private void onRemoveStack(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
		ServerNetworking.INSTANCE.changePositionProvider(cir.getReturnValue(), world);
	}

	@Override
	public Type getMinecartType() {
		return null;
	}
}
