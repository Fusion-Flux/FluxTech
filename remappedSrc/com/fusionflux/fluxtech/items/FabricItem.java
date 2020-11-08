package com.fusionflux.fluxtech.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class FabricItem extends Item {
    public FabricItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (context.getPlayer().pitch > 49.9) {
            Vec3d velocity = context.getPlayer().getVelocity();
            context.getPlayer().getItemCooldownManager().set(this, 2);
            context.getPlayer().setVelocity(velocity.x, velocity.y + 2, velocity.z);

        }
        return super.useOnBlock(context);

    }


}
