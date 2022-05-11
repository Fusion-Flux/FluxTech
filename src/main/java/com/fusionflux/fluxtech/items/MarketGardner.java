package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.accessor.LaunchAccessors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MarketGardner extends Item {
    public MarketGardner(Settings settings) {
        super(settings);
    }

    boolean isjumping = false;

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity) {
            if(((LaunchAccessors)(Entity)attacker).getBlastJumping()) {
                target.damage(DamageSource.mob(attacker), 195);

            }
        }

        return false;
    }



    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        isjumping = ((LaunchAccessors) entity).getBlastJumping();
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return isjumping;
    }
}
