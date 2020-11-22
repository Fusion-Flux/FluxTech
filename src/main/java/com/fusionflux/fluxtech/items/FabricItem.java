package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.sound.FluxTechSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class FabricItem extends Item {
    public FabricItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Random random = new Random();
        BlockState blockUsedOn = context.getWorld().getBlockState(context.getBlockPos());
        if (FluxTechConfig.ENABLED.ENABLED_HPD_LAUNCH_PREVENTER.getValue()) {
            if (Objects.requireNonNull(context.getPlayer()).pitch > 49.9 && !blockUsedOn.isOf(FluxTechBlocks.SMOOTH_GREY_PANEL) &&
                    !blockUsedOn.isOf(FluxTechBlocks.BOTTOM_2X2_SMOOTH_GREY_PANEL) && !blockUsedOn.isOf(FluxTechBlocks.BOTTOM_SMOOTH_GREY_PANEL) &&
                    !blockUsedOn.isOf(FluxTechBlocks.CHISELED_SMOOTH_GREY_PANEL) && !blockUsedOn.isOf(FluxTechBlocks.TOP_2X2_SMOOTH_GREY_PANEL) &&
                    !blockUsedOn.isOf(FluxTechBlocks.TOP_SMOOTH_GREY_PANEL) && !blockUsedOn.isOf(FluxTechBlocks.PADDED_GREY_PANEL) &&
                    !blockUsedOn.isOf(FluxTechBlocks.BOTTOM_2X2_PADDED_GREY_PANEL) && !blockUsedOn.isOf(FluxTechBlocks.BOTTOM_PADDED_GREY_PANEL) &&
                    !blockUsedOn.isOf(FluxTechBlocks.CHISELED_PADDED_GREY_PANEL) && !blockUsedOn.isOf(FluxTechBlocks.TOP_2X2_PADDED_GREY_PANEL) &&
                    !blockUsedOn.isOf(FluxTechBlocks.TOP_PADDED_GREY_PANEL) && !blockUsedOn.isOf(FluxTechBlocks.GEL) && !blockUsedOn.isOf(FluxTechBlocks.REPULSION_GEL)) {
                Vec3d velocity = context.getPlayer().getVelocity();
                context.getPlayer().getItemCooldownManager().set(this, FluxTechConfig.INTEGER_VALUES.HPD_COOLDOWN.getValue());
                context.getPlayer().setVelocity(velocity.x, velocity.y + FluxTechConfig.VALUES.HPD_LAUNCH_POWER.getValue(), velocity.z);
                context.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 3F);
                context.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), 0, 0, 0);
            }
            return super.useOnBlock(context);
        }else{
            if (Objects.requireNonNull(context.getPlayer()).pitch > 49.9) {
                Vec3d velocity = context.getPlayer().getVelocity();
                context.getPlayer().getItemCooldownManager().set(this, FluxTechConfig.INTEGER_VALUES.HPD_COOLDOWN.getValue());
                context.getPlayer().setVelocity(velocity.x, velocity.y + FluxTechConfig.VALUES.HPD_LAUNCH_POWER.getValue(), velocity.z);
                context.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 3F);
                context.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), 0, 0, 0);
            }
            return super.useOnBlock(context);
        }
    }
}
