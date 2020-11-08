package com.fusionflux.fluxtech;

//import com.fusionflux.fluxtech.config.FluxTechConfig;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.delay.DelayedForLoopManager;
import com.fusionflux.fluxtech.items.FluxTechItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class FluxTech implements ModInitializer {

    public static final String MOD_ID = "fluxtech";


    @Override
    public void onInitialize() {
        FluxTechItems.registerItems();
        FluxTechBlocks.registerBlocks();
        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
            DelayedForLoopManager.tick();
        });

    }

}
