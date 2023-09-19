package dev.auxves.vibes.mixin;

import dev.auxves.vibes.server.BridgesKt;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
class BlockMixin {
	@Redirect(
		method = "dropStack(Lnet/minecraft/world/World;Ljava/util/function/Supplier;Lnet/minecraft/item/ItemStack;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
	)
	private static boolean onSpawnEntity(World world, Entity entity) {
		BridgesKt.onBreakShulkerBox(entity);
		return world.spawnEntity(entity);
	}
}
