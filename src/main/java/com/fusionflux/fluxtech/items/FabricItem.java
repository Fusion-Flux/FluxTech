package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.sound.FluxTechSounds;
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

import java.util.Random;

public class FabricItem extends Item {
    public FabricItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Random random = new Random();
        if (context.getPlayer().pitch > 49.9) {
            Vec3d velocity = context.getPlayer().getVelocity();
            context.getPlayer().getItemCooldownManager().set(this, 2);
            context.getPlayer().setVelocity(velocity.x, velocity.y + 2, velocity.z);
context.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F,1F);
context.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, context.getPlayer().getX(),context.getPlayer().getY(),context.getPlayer().getZ(),0,0,0);
        }



        return super.useOnBlock(context);

    }



}
