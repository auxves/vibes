package dev.auxves.vibes.mixin;

import dev.auxves.vibes.server.BridgesKt;
import dev.auxves.vibes.util.ItemsKt;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntity.class)
class ItemEntityMixin {
	@Shadow private int itemAge;

	@Redirect(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;getStack()Lnet/minecraft/item/ItemStack;"))
	private ItemStack onGetStack(ItemEntity entity) {
		if (ItemsKt.isPlaying(entity.getStack())) return entity.getStack().copy();
		return entity.getStack();
	}

	@Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V"))
	private void stopPlayingWhenKilled(ItemEntity entity) {
		BridgesKt.stopPlaying(entity.getStack(), entity.getWorld());
		entity.discard();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V"))
	private void keepAlive(ItemEntity entity) {
		if (!ItemsKt.isPlaying(entity.getStack())) entity.discard();
		else itemAge = 0;
	}
}
