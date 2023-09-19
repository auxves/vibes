package dev.auxves.vibes.mixin;

import dev.auxves.vibes.server.BridgesKt;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntity.class)
class ItemFrameEntityMixin {
	@Unique
	final ItemFrameEntity that = ItemFrameEntity.class.cast(this);

	@Inject(method = "setHeldItemStack(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	private void onSetStack(ItemStack stack, CallbackInfo ci) {
		BridgesKt.changePosition(stack, that);
	}

	@Redirect(method = "dropHeldStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"))
	private ItemEntity onDropStack(ItemFrameEntity entity, ItemStack stack) {
		ItemEntity itemEntity = entity.dropStack(stack);
		if (itemEntity != null) BridgesKt.changePosition(stack, itemEntity);
		return itemEntity;
	}
}
