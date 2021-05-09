package com.fusionflux.fluxtech.dim;

import com.fusionflux.fluxtech.FluxTech;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class CloakDimension {
    public static final Identifier DIM_ID = new Identifier(FluxTech.MOD_ID, "cloaked");

    // The dimension options refer to the JSON-file in the dimension subfolder of the datapack,
    // which will always share it's ID with the world that is created from it
    private static final RegistryKey<DimensionOptions> DIMENSION_KEY = RegistryKey.of(
            Registry.DIMENSION_OPTIONS,
            DIM_ID
    );

    public static RegistryKey<World> WORLD_KEY = RegistryKey.of(
            Registry.DIMENSION,
            DIM_ID
    );

    private static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY,
            new Identifier(FluxTech.MOD_ID, "cloaked_type")
    );

    public static void registerCloakedDimension() {
        Registry.register(Registry.CHUNK_GENERATOR, DIM_ID, CloakedChunkGenerator.CODEC);
    }
}
