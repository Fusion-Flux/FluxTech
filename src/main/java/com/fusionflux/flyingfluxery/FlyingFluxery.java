package com.fusionflux.flyingfluxery;

import com.fusionflux.flyingfluxery.config.FluxTechConfig;
import com.fusionflux.flyingfluxery.items.FluxTechItems;
import com.fusionflux.flyingfluxery.sound.FluxTechSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class FlyingFluxery implements ModInitializer {
    public static final String MOD_ID = "flyingfluxery";
    public static final ItemGroup FLUXTECH_GROUP = FabricItemGroupBuilder.build(
            new Identifier("flyingfluxery", "general"),
            () -> new ItemStack(FluxTechItems.COPPER_CREEPER));

    @Override
    public void onInitialize() {
        FluxTechConfig.register();
        FluxTechItems.registerItems();
        FluxTechSounds.registerSounds();
    }
}
