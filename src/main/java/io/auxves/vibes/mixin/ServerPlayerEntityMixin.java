package io.auxves.vibes.mixin;

import io.auxves.vibes.server.BridgesKt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
class ServerPlayerEntityMixin {
	ServerPlayerEntity that = ServerPlayerEntity.class.cast(this);

	@Inject(method = "sendPickup", at = @At("HEAD"))
	private void onPickup(Entity item, int count, CallbackInfo ci) {
		if (item instanceof ItemEntity) {
			BridgesKt.changePosition(((ItemEntity) item).getStack(), that);
		}
	}
}
