package com.fusionflux.fluxtech.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor
    void setDimensions(EntityDimensions dimensions);

    @Accessor
    void setStandingEyeHeight(float eyeHeight);

    @Invoker
    float callGetEyeHeight(EntityPose pose, EntityDimensions dimensions);
}
