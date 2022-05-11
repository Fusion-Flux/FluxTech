package com.fusionflux.fluxtech;

import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.items.FluxTechItems;
import com.fusionflux.fluxtech.sound.FluxTechSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class FluxTech implements ModInitializer {
    public static final String MOD_ID = "fluxtech";
    public static final ItemGroup FLUXTECH_GROUP = FabricItemGroupBuilder.build(
            new Identifier("fluxtech", "general"),
            () -> new ItemStack(FluxTechItems.COPPER_CREEPER));

    @Override
    public void onInitialize() {
        FluxTechConfig.register();
        FluxTechItems.registerItems();
        FluxTechSounds.registerSounds();
    }
}
