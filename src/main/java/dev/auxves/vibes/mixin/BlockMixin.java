package dev.auxves.vibes.mixin;

import dev.auxves.vibes.server.BridgesKt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
class BlockMixin {
	@Redirect(
		method = "dropStack(Lnet/minecraft/world/World;Ljava/util/function/Supplier;Lnet/minecraft/item/ItemStack;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
	)
	private static boolean onSpawnEntity(World world, Entity entity) {
		BridgesKt.onBreak(entity);
		return world.spawnEntity(entity);
	}

	@Inject(method = "onPlaced", at = @At("HEAD"))
	private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity != null && itemStack.contains(DataComponentTypes.CONTAINER)) BridgesKt.changePosition(itemStack, entity);
	}
}
