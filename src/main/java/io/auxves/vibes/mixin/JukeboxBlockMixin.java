package io.auxves.vibes.mixin;

import io.auxves.vibes.network.packet.ChangeDistance;
import io.auxves.vibes.server.BridgesKt;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JukeboxBlock.class)
class JukeboxBlockMixin {
	@Redirect(method = "removeRecord", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private boolean onSpawnEntity(World world, Entity entity) {
		ItemEntity itemEntity = (ItemEntity) entity;
		ItemStack stack = itemEntity.getStack();
		BridgesKt.changePosition(stack, entity);
		BridgesKt.changeDistance(stack, entity.world, ChangeDistance.Normal);
		return world.spawnEntity(entity);
	}
}
