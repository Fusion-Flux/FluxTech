package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.items.FluxTechItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(at = @At("HEAD"), method = "onEntityLand", cancellable = true)
    private void bounce(BlockView world, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity && entity.getVelocity().length() > 0.5) {
            for (ItemStack stack : entity.getArmorItems()) {
                if (stack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) {
                    entity.setVelocity(entity.getVelocity().multiply(1.0F, -FluxTechConfig.get().numbers.slimeBounceMultiplier * (entity.isSneaking() ? 0 : 1), 1.0F));
                    entity.world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_SLIME_BLOCK_FALL, SoundCategory.PLAYERS, 1.0F, 0.5F);
                    ci.cancel();
                }
            }
        }
    }
}
