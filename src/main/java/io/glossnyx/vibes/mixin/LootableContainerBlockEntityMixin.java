package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootableContainerBlockEntity.class)
class LootableContainerBlockEntityMixin extends BlockEntity {
	public LootableContainerBlockEntityMixin(BlockEntityType<?> type) {
		super(type);
	}

	@Inject(method = "setStack", at = @At("HEAD"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		ServerNetworking.INSTANCE.changePositionProvider(stack, this);
	}

	@Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;", at = @At("RETURN"))
	private void onRemoveStack(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
		ServerNetworking.INSTANCE.changePositionProvider(cir.getReturnValue(), this.world);
	}
}