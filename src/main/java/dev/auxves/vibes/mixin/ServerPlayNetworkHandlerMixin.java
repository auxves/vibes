package dev.auxves.vibes.mixin;

import dev.auxves.vibes.server.BridgesKt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
class ServerPlayNetworkHandlerMixin {
	@Shadow public ServerPlayerEntity player;
	@Shadow @Final private MinecraftServer server;

	@Inject(method = "onDisconnected", at = @At("HEAD"))
	private void onDisconnect(Text reason, CallbackInfo ci) {
		server.execute(() -> BridgesKt.onDisconnect(player));
	}
}