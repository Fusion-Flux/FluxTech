package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.blockentities.*;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.fluids.Endurium;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechBlocks {

    public static final FlowableFluid ENDURIUM = new Endurium.Source();
    public static final FlowableFluid ENDURIUM_FLOWING = new Endurium.Flowing();
    public static final Block ENDURIUM_BLOCK = new EnduriumBlock(ENDURIUM);

    public static final Block SMOOTH_END_STONE = new Block(FabricBlockSettings.of(Material.STONE).hardness(3.5f));
    public static final Block SMOOTH_END_STONE_SLAB = new SlabBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));
    public static final Block SMOOTH_END_STONE_STAIRS = new CustomStairs(FluxTechBlocks.SMOOTH_END_STONE);
    public static final Block SMOOTH_END_STONE_WALL = new WallBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));


    //public static Tag<Block> MY_TAG = TagRegistry.block(new Identifier("fluxtech", "hpd_deny_launch"));

    public static BlockEntityType<StarCoreEntity> STAR_CORE_ENTITY;


    public static void registerBlocks() {
        if (FluxTechConfig2.get().enabled.enableEndurium) {
            Registry.register(Registry.FLUID, new Identifier(FluxTech.MOD_ID, "endurium_still"), ENDURIUM);
            Registry.register(Registry.FLUID, new Identifier(FluxTech.MOD_ID, "endurium_flowing"), ENDURIUM_FLOWING);
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "endurium_block"), ENDURIUM_BLOCK);
        }
        if (FluxTechConfig2.get().enabled.enableSmoothEndStone) {
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone"), SMOOTH_END_STONE);
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_slab"), SMOOTH_END_STONE_SLAB);
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_stairs"), SMOOTH_END_STONE_STAIRS);
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_wall"), SMOOTH_END_STONE_WALL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone"), new BlockItem(SMOOTH_END_STONE, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_slab"), new BlockItem(SMOOTH_END_STONE_SLAB, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_stairs"), new BlockItem(SMOOTH_END_STONE_STAIRS, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_wall"), new BlockItem(SMOOTH_END_STONE_WALL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
        }


    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM_FLOWING, RenderLayer.getTranslucent());
    }

    public static final Tag<Fluid> ENDURIUM_TAG = fluidTagRegister("endurium");

    public static Tag<Fluid> fluidTagRegister(String id) {
        return TagRegistry.fluid(new Identifier("c", id));

    }

}
