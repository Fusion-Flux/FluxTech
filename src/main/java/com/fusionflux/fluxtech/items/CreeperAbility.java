package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.config.FluxTechConfig2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;

public class CreeperAbility extends Item {

    public CreeperAbility(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        assert world != null;
        if (!world.isClient) {
            world.createExplosion(null, user.getX(), user.getY(), user.getZ(), 3F, Explosion.DestructionType.BREAK);
            user.getItemCooldownManager().set(this, 200);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }



}
