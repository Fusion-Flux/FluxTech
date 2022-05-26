package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.accessor.LaunchAccessors;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import me.andrew.gravitychanger.api.GravityChangerAPI;
import me.andrew.gravitychanger.util.RotationUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class CopperCreeper extends Item {
    public CopperCreeper(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        float magnitude = (float) FluxTechConfig.get().numbers.hPDLaunchPower;
        context.getPlayer().getItemCooldownManager().set(this, FluxTechConfig.get().numbers.hPDCooldown);

        Vec3d direction;
            direction = context.getPlayer().getRotationVector().multiply(-1.0F).normalize();
        ((LaunchAccessors) context.getPlayer()).setLaunchVelocity(context.getPlayer().getVelocity().add(direction.multiply(magnitude)));
        ((LaunchAccessors) context.getPlayer()).setBlastJumping(true);
        context.getPlayer().setVelocity(RotationUtil.vecWorldToPlayer(context.getPlayer().getVelocity().add(direction.multiply(magnitude)), GravityChangerAPI.getGravityDirection(context.getPlayer())));
            context.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2F, 3F);
            context.getPlayer().world.addParticle(ParticleTypes.EXPLOSION_EMITTER, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), 0, 0, 0);
        return ActionResult.PASS;
    }

}
