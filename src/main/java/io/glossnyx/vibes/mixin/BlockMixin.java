package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
class BlockMixin {
	@Redirect(
		method = "dropStack*",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
		)
	)
	private static boolean onSpawnEntity(World world, Entity entity) {
		ServerNetworking.INSTANCE.onBreakShulkerBox(entity);
		return world.spawnEntity(entity);
	}
}
