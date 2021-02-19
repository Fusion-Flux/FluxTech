package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.util.FluxTechTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class HandheldPropulsionDevice extends Item {
    public HandheldPropulsionDevice(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        float magnitude = (float) FluxTechConfig2.get().numbers.hPDLaunchPower;
        BlockPos blockPos;
        HitResult hitResult = user.raycast(5.0D, 0.0F, true);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockUsedOn = user.world.getBlockState(blockPos);
            if (FluxTechConfig2.get().numbers.enableHPDLaunchPreventer && blockUsedOn.isIn(FluxTechTags.HPD_DENY_LAUNCH)) {
                magnitude /= 3.0F;
            }
        }

        user.getItemCooldownManager().set(this, FluxTechConfig2.get().numbers.hPDCooldown);

        List<Entity> list = user.world.getOtherEntities(null, user.getBoundingBox().expand(5.0D, 2.5D, 5.0D), null);
        if (!list.isEmpty()) {
            Vec3d direction;
            for (Entity entity : list) {
                if (FluxTechConfig2.get().numbers.hPDReturnsProjectiles && entity instanceof PersistentProjectileEntity) {
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
}
