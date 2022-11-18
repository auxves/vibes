package io.auxves.vibes.mixin;

import io.auxves.vibes.server.BridgesKt;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlock.class)
class ShulkerBoxBlockMixin {
	@Inject(method = "onPlaced", at = @At("HEAD"))
	private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity != null) BridgesKt.changePosition(itemStack, entity);
	}
}
