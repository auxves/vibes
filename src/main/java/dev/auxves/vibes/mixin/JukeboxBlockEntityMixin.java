package dev.auxves.vibes.mixin;

import dev.auxves.vibes.item.Vibe;
import dev.auxves.vibes.server.BridgesKt;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JukeboxBlockEntity.class)
class JukeboxBlockEntityMixin {
	@Redirect(method = "dropRecord", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private boolean onSpawnEntity(World world, Entity entity) {
		ItemEntity itemEntity = (ItemEntity) entity;
		ItemStack stack = itemEntity.getStack();
		BridgesKt.changePosition(stack, entity);
		BridgesKt.changeDistance(stack, entity.getWorld(), 16F);
		return world.spawnEntity(entity);
	}

	@Redirect(method = "setStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean onIsIn(ItemStack instance, TagKey<Item> tag) {
		if (instance.getItem() instanceof Vibe) return true;
		return instance.isIn(tag);
	}
}
