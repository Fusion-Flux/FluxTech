package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.accessor.HopperInput;
import net.minecraft.block.entity.Hopper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Hopper.class)
public interface HopperMixin extends HopperInput {

}
