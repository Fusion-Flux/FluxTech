package com.fusionflux.fluxtech.client;

import com.fusionflux.fluxtech.client.rendering.FluidRender;
import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FluxTechClient implements ClientModInitializer {
	public static final Identifier FLUID_STILL = new Identifier(FluxTech.MOD_ID, "block/endurium_still");
	public static final Identifier FLUID_FLOWING = new Identifier(FluxTech.MOD_ID, "block/endurium_flow");

	@Override
	public void onInitializeClient() {
		FluidRender.setupFluidRendering(FluxTechBlocks.ENDURIUM, FluxTechBlocks.ENDURIUM_FLOWING, FLUID_STILL, FLUID_FLOWING, 0x084537);
		FluxTechBlocks.registerRenderLayers();
	}
}


