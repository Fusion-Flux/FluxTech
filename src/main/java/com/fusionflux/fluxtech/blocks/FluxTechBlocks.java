package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.entities.HopperBlockEntity;
import com.fusionflux.fluxtech.blocks.entities.StarCoreEntity;
import com.fusionflux.fluxtech.blocks.entities.UpperBlockEntity;
import com.fusionflux.fluxtech.config.FluxTechConfig;
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
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechBlocks {

    public static final FlowableFluid ENDURIUM = new Endurium.Source();
    public static final FlowableFluid ENDURIUM_FLOWING = new Endurium.Flowing();
    public static final Tag<Fluid> ENDURIUM_TAG = fluidTagRegister("endurium");
    public static final Block ENDURIUM_BLOCK = new EnduriumBlock(ENDURIUM, FabricBlockSettings.of(Material.WATER).noCollision().strength(100.0F, 100.0F).dropsNothing().velocityMultiplier(0.95F));

    public static final Block SMOOTH_END_STONE = new Block(FabricBlockSettings.of(Material.STONE).hardness(3.5f));
    public static final Block SMOOTH_END_STONE_SLAB = new SlabBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));
    public static final Block SMOOTH_END_STONE_STAIRS = new CustomStairs(FluxTechBlocks.SMOOTH_END_STONE);
    public static final Block SMOOTH_END_STONE_WALL = new WallBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));

    public static final HopperBlock SKIPPER_BLOCK = new HopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 2);
    public static final HopperBlock JUMPER_BLOCK = new HopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 3);
    public static final UpperBlock UPPER_BLOCK = new UpperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 1);
    public static final UpperBlock SKUPPER_BLOCK = new UpperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 2);
    public static final UpperBlock JUPPER_BLOCK = new UpperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 3);

    public static BlockEntityType<StarCoreEntity> STAR_CORE_ENTITY;
    public static BlockEntityType<HopperBlockEntity> HOPPER_BLOCK_ENTITY;
    public static BlockEntityType<UpperBlockEntity> UPPER_BLOCK_ENTITY;

    public static void registerBlocks() {
        if (FluxTechConfig.get().enabled.enableEndurium) {
            Registry.register(Registry.FLUID, new Identifier(FluxTech.MOD_ID, "endurium_still"), ENDURIUM);
            Registry.register(Registry.FLUID, new Identifier(FluxTech.MOD_ID, "endurium_flowing"), ENDURIUM_FLOWING);
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "endurium_block"), ENDURIUM_BLOCK);
        }
        if (FluxTechConfig.get().enabled.enableSmoothEndStone) {
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone"), SMOOTH_END_STONE);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone"), new BlockItem(SMOOTH_END_STONE, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_slab"), SMOOTH_END_STONE_SLAB);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_slab"), new BlockItem(SMOOTH_END_STONE_SLAB, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_stairs"), SMOOTH_END_STONE_STAIRS);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_stairs"), new BlockItem(SMOOTH_END_STONE_STAIRS, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_wall"), SMOOTH_END_STONE_WALL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_wall"), new BlockItem(SMOOTH_END_STONE_WALL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
        }
        HOPPER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "hopper_entity"), BlockEntityType.Builder.create(HopperBlockEntity::new, SKIPPER_BLOCK, JUMPER_BLOCK).build(null));

        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "skipper"), SKIPPER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "skipper"), new BlockItem(SKIPPER_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "jumper"), JUMPER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "jumper"), new BlockItem(JUMPER_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

        UPPER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "upper_entity"), BlockEntityType.Builder.create(UpperBlockEntity::new, UPPER_BLOCK, SKUPPER_BLOCK, JUPPER_BLOCK).build(null));

        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "upper"), UPPER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "upper"), new BlockItem(UPPER_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "skupper"), SKUPPER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "skupper"), new BlockItem(SKUPPER_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "jupper"), JUPPER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "jupper"), new BlockItem(JUPPER_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM_FLOWING, RenderLayer.getTranslucent());
    }

    public static Tag<Fluid> fluidTagRegister(String id) {
        return TagRegistry.fluid(new Identifier("c", id));

    }

}
