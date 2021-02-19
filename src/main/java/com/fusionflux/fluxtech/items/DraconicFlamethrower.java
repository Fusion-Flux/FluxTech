package com.fusionflux.fluxtech.items;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DraconicFlamethrower extends Item {

    private AreaEffectCloudEntity field_7051;

    public DraconicFlamethrower(Settings settings) {
        super(settings);

    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Vec3d vec3d = user.getVelocity();
        vec3d.rotateY(-0.7853982F);
        double d = user.getX();
        double e = user.getBodyY(0.5D);
        double f = user.getZ();

        for (int i = 0; i < 8; ++i) {
            double g = d + user.getRandom().nextGaussian() / 2.0D;
            double h = e + user.getRandom().nextGaussian() / 2.0D;
            double j = f + user.getRandom().nextGaussian() / 2.0D;

            for (int k = 0; k < 6; ++k) {
                user.world.addParticle(ParticleTypes.DRAGON_BREATH, g, h, j, -vec3d.x * 0.07999999821186066D * (double) k, -vec3d.y * 0.6000000238418579D, -vec3d.z * 0.07999999821186066D * (double) k);
            }
        }
        Vec3d vec3d2 = (new Vec3d(user.getX() - user.getX(), 0.0D, user.getZ() - user.getZ())).normalize();
        float o = 5.0F;
        double j = user.getX() + vec3d2.x * 5.0D / 2.0D;
        double b = user.getZ() + vec3d2.z * 5.0D / 2.0D;
        double g = user.getBodyY(0.5D);
        double h = g;
        BlockPos.Mutable mutable = new BlockPos.Mutable(j, g, b);

        while(user.world.isAir(mutable)) {
            --h;
            if (h < 0.0D) {
                h = g;
                break;
            }

            mutable.set(j, h, b);
        }

        h = (double)(MathHelper.floor(h) + 1);
        this.field_7051 = new AreaEffectCloudEntity(user.world, j, h, b);
        this.field_7051.setOwner(user);
        this.field_7051.setRadius(5.0F);
        this.field_7051.setDuration(200);
        this.field_7051.setParticleType(ParticleTypes.DRAGON_BREATH);
        this.field_7051.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE));
        user.world.spawnEntity(this.field_7051);
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
