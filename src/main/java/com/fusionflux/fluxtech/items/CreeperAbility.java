package com.fusionflux.fluxtech.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

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
