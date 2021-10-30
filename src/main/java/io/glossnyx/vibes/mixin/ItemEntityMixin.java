package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.util.ItemsKt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntity.class)
class ItemEntityMixin {
	@Redirect(
		method = "onPlayerCollision",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/ItemEntity;getStack()Lnet/minecraft/item/ItemStack;"
		)
	)
	private ItemStack onGetStack(ItemEntity entity) {
		if (ItemsKt.isPlaying(entity.getStack())) return entity.getStack().copy();
		return entity.getStack();
	}

	@Redirect(
		method = "onPlayerCollision",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"
		)
	)
	private void onSendPickup(PlayerEntity player, Entity item, int count) {
		player.sendPickup(item, count);
		ItemStack stack = ((ItemEntity) item).getStack();
		if (ItemsKt.isPlaying(stack)) stack.setCount(0);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V"))
	private void onDiscard(ItemEntity entity) {
		if (!ItemsKt.isPlaying(entity.getStack())) entity.discard();
	}
}
