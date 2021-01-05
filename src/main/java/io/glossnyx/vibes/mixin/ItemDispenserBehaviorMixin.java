package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemDispenserBehavior.class)
class ItemDispenserBehaviorMixin {
	@Redirect(method = "spawnItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private static boolean onSpawnEntity(World world, Entity entity) {
		ItemEntity itemEntity = (ItemEntity) entity;
		ServerNetworking.INSTANCE.changePositionProvider(itemEntity.getStack(), itemEntity);
		return world.spawnEntity(entity);
	}
}
