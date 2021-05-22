package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.accessor.EnduriumToucher;
import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    protected abstract void applyBuoyancy();
    @Shadow
    public abstract ItemStack getStack();

    protected ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (this.getStack().isEmpty()) {
            this.remove();
        } else {
            float f = this.getStandingEyeHeight() - 0.11111111F;
            if (((EnduriumToucher) this).fluxtech$getTouchingEndurium() && this.getFluidHeight(FluxTechBlocks.ENDURIUM_TAG) > (double) f) {
                this.applyBuoyancy();
                this.addVelocity(0,0.045,0);
            }
        }
    }
}
