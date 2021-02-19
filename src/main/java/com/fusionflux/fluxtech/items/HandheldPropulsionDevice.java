package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.util.FluxTechTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.Random;

public class HandheldPropulsionDevice extends Item {
    public HandheldPropulsionDevice(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Random random = new Random();
        BlockState blockUsedOn = context.getWorld().getBlockState(context.getBlockPos());
        if (FluxTechConfig2.get().numbers.enableHPDLaunchPreventer) {
            if (Objects.requireNonNull(context.getPlayer()).pitch > 49.9 && !blockUsedOn.isIn(FluxTechTags.HPD_DENY_LAUNCH)) {
                Vec3d velocity = context.getPlayer().getVelocity();
                context.getPlayer().getItemCooldownManager().set(this, FluxTechConfig2.get().numbers.hPDCooldown);
                context.getPlayer().setVelocity(velocity.x, velocity.y + FluxTechConfig2.get().numbers.hPDLaunchPower, velocity.z);
                context.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 3F);
                context.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), 0, 0, 0);
            }
            return super.useOnBlock(context);
        } else {
            if (Objects.requireNonNull(context.getPlayer()).pitch > 49.9) {
                Vec3d velocity = context.getPlayer().getVelocity();
                context.getPlayer().getItemCooldownManager().set(this, FluxTechConfig2.get().numbers.hPDCooldown);
                context.getPlayer().setVelocity(velocity.x, velocity.y + FluxTechConfig2.get().numbers.hPDLaunchPower, velocity.z);
                context.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 3F);
                context.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), 0, 0, 0);
            }
            return super.useOnBlock(context);
        }
    }
}
