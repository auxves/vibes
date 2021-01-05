package io.glossnyx.vibes.mixin;

import io.glossnyx.vibes.network.ClientNetworking;
import io.glossnyx.vibes.network.ServerNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ScreenHandler.class)
class ScreenHandlerMixin {
	@Shadow @Final public List<Slot> slots;

	@Inject(method = "method_30010", at = @At("HEAD"), cancellable = true)
	private void onRemoveStack(int slotIndex, int clickType, SlotActionType actionType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
		if (slotIndex < 0) return;

		Slot slot = slots.get(slotIndex);
		if (slot == null) return;

		ServerNetworking.INSTANCE.onQuickMove(slot, actionType, player);
		ClientNetworking.INSTANCE.onRightClick(slot, clickType, actionType, player, cir);
	}
}
