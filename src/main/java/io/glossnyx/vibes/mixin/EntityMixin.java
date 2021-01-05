package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
class EntityMixin {
	@Shadow public World world;

	@Inject(method = "remove", at = @At("HEAD"))
	private void onRemove(CallbackInfo ci) {
		if (this.world.isClient) return;

		//noinspection ConstantConditions
		if (!ItemEntity.class.isInstance(this)) return;

		ServerNetworking.INSTANCE.stopPlaying(ItemEntity.class.cast(this).getStack(), world);
	}
}
