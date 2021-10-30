package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
class ServerPlayerEntityMixin {
	@Inject(method = "sendPickup", at = @At("HEAD"))
	private void onPickup(Entity item, int count, CallbackInfo ci) {
		ServerNetworking.INSTANCE.onPickup(ServerPlayerEntity.class.cast(this), item);
	}
}
