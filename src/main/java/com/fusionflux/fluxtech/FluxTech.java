package com.fusionflux.fluxtech;

//import com.fusionflux.fluxtech.config.FluxTechConfig;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.client.rendering.BoxScreen;
import com.fusionflux.fluxtech.client.rendering.BoxScreenHandler;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.delay.DelayedForLoopManager;
import com.fusionflux.fluxtech.effects.CustomEffects;
import com.fusionflux.fluxtech.items.FluxTechItems;
import com.fusionflux.fluxtech.sound.FluxTechSounds;
//import com.oroarmor.util.config.ConfigItemGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

public class FluxTech implements ModInitializer {

    public static final FluxTechConfig CONFIG = new FluxTechConfig();



    public static final String MOD_ID = "fluxtech";

    //public static final ScreenHandlerType<BoxScreenHandler> BOX_SCREEN_HANDLER;

    public static final ItemGroup FLUXTECH_GROUP = FabricItemGroupBuilder.build(
            new Identifier("fluxtech", "general"),
            () -> new ItemStack(FluxTechItems.HANDHELD_PROPULSION_DEVICE));

    static{
       // BOX_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "core"), BoxScreenHandler::new);
    }

    @Override
    public void onInitialize() {
        //ScreenRegistry.register(FluxTech.BOX_SCREEN_HANDLER, BoxScreen::new);
        //processConfig();
        FluxTechConfig2.register();
        CustomEffects.registerEffects();
        FluxTechItems.registerItems();
        FluxTechBlocks.registerBlocks();
        FluxTechSounds.registerSounds();
        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
            DelayedForLoopManager.tick();
        });

    }
   /* private void processConfig() {
        CONFIG.readConfigFromFile();

        ServerLifecycleEvents.SERVER_STOPPED.register(l -> CONFIG.saveConfigToFile());
    }*/
}
