package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntity.class)
abstract class ItemFrameEntityMixin extends AbstractDecorationEntity {
	protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "setHeldItemStack(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	private void onSetStack(ItemStack stack, CallbackInfo ci) {
		ServerNetworking.INSTANCE.changePositionProvider(stack, this);
	}

	@Redirect(
		method = "dropHeldStack",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"
		)
	)
	private ItemEntity onDropStack(ItemFrameEntity entity, ItemStack stack) {
		ItemEntity itemEntity = entity.dropStack(stack);
		ServerNetworking.INSTANCE.changePositionProvider(stack, itemEntity);
		return itemEntity;
	}
}
