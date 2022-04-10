package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.accessor.LaunchAccessors;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.util.FluxTechTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class HandheldPropulsionDevice extends Item {
    public HandheldPropulsionDevice(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        float magnitude = (float) FluxTechConfig.get().numbers.hPDLaunchPower;
        BlockPos blockPos;
        HitResult hitResult = context.getPlayer().raycast(5.0D, 0.0F, true);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockUsedOn = context.getPlayer().world.getBlockState(blockPos);
            if (FluxTechConfig.get().numbers.enableHPDLaunchPreventer && blockUsedOn.isIn(FluxTechTags.HPD_DENY_LAUNCH)) {
                magnitude /= 3.0F;
            }
        }

        //context.getPlayer().getInventory().remove()
        context.getPlayer().getItemCooldownManager().set(this, FluxTechConfig.get().numbers.hPDCooldown);

        List<Entity> list = context.getPlayer().world.getOtherEntities(null, context.getPlayer().getBoundingBox().expand(5.0D, 2.5D, 5.0D), e -> true);
        if (!list.isEmpty()) {
            Vec3d direction;
            for (Entity entity : list) {
                if (FluxTechConfig.get().numbers.hPDReturnsProjectiles && entity instanceof PersistentProjectileEntity) {
                    direction = entity.getVelocity().multiply(-1.0D);
                } else if (entity != context.getPlayer()) {
                    direction = (entity.getPos().subtract(context.getPlayer().getPos().add(0.0F, -2.0F, 0.0F))).normalize();
                } else {
                    direction = context.getPlayer().getRotationVector().multiply(-1.0F).normalize();
                }
                ((LaunchAccessors) entity).setLaunchVelocity(entity.getVelocity().add(direction.multiply(magnitude)));
                entity.setVelocity(entity.getVelocity().add(direction.multiply(magnitude)));

            }
            context.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 3F);
            context.getPlayer().world.addParticle(ParticleTypes.EXPLOSION_EMITTER, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), 0, 0, 0);
        }
        return ActionResult.PASS;
    }

    /*
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        float magnitude = (float) FluxTechConfig.get().numbers.hPDLaunchPower;
        BlockPos blockPos;
        HitResult hitResult = user.raycast(5.0D, 0.0F, true);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockUsedOn = user.world.getBlockState(blockPos);
            if (FluxTechConfig.get().numbers.enableHPDLaunchPreventer && blockUsedOn.isIn(FluxTechTags.HPD_DENY_LAUNCH)) {
                magnitude /= 3.0F;
            }
        }

        user.getItemCooldownManager().set(this, FluxTechConfig.get().numbers.hPDCooldown);

        List<Entity> list = user.world.getOtherEntities(null, user.getBoundingBox().expand(5.0D, 2.5D, 5.0D), null);
        if (!list.isEmpty()) {
            Vec3d direction;
            for (Entity entity : list) {
                if (FluxTechConfig.get().numbers.hPDReturnsProjectiles && entity instanceof PersistentProjectileEntity) {
                    direction = entity.getVelocity().multiply(-1.0D);
                } else if (entity != user) {
                    direction = (entity.getPos().subtract(user.getPos().add(0.0F, -2.0F, 0.0F))).normalize();
                } else {
                    direction = user.getRotationVector().multiply(-1.0F).normalize();
                }
                entity.setVelocity(entity.getVelocity().add(direction.multiply(magnitude)));
            }
            user.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 3F);
            user.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, user.getX(), user.getY(), user.getZ(), 0, 0, 0);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
    */
}
