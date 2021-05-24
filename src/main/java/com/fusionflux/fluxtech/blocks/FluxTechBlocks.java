package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.entities.*;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.fluids.Endurium;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.world.poi.PointOfInterestType;

public class FluxTechBlocks {

    // public static Tag<Block> MY_TAG = TagRegistry.block(new Identifier("fluxtech", "hpd_deny_launch"));

    public static final FlowableFluid ENDURIUM = new Endurium.Source();
    public static final FlowableFluid ENDURIUM_FLOWING = new Endurium.Flowing();
    public static final Tag<Fluid> ENDURIUM_TAG = fluidTagRegister("endurium");
    public static final Block ENDURIUM_BLOCK = new EnduriumBlock(ENDURIUM);

    public static final Block SMOOTH_END_STONE = new Block(FabricBlockSettings.of(Material.STONE).hardness(3.5f));
    public static final Block SMOOTH_END_STONE_SLAB = new SlabBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));
    public static final Block SMOOTH_END_STONE_STAIRS = new CustomStairs(FluxTechBlocks.SMOOTH_END_STONE);
    public static final Block SMOOTH_END_STONE_WALL = new WallBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));
    public static final StorageNodeBlock STORAGE_NODE_BLOCK = new StorageNodeBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5f));
    public static final StorageCoreBlock STORAGE_CORE_BLOCK = new StorageCoreBlock(FabricBlockSettings.of(Material.STONE).hardness(3.5f));

    public static final BarbedWire BARBEDWIRE = new BarbedWire(FabricBlockSettings.of(Material.STONE).hardness(3.5f).noCollision().nonOpaque());

    public static final RedstoneRandomizerBlock REDSTONE_RANDOMIZER_BLOCK = new RedstoneRandomizerBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK).ticksRandomly().luminance((state) -> state.get(RedstoneRandomizerBlock.POWER)));
    public static BlockEntityType<RedstoneRandomizerBlockEntity> REDSTONE_RANDOMIZER_BLOCK_ENTITY;


    public static final HopperBlock SKIPPER_BLOCK = new HopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 2);
    public static final HopperBlock JUMPER_BLOCK = new HopperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 3);
    public static final UpperBlock UPPER_BLOCK = new UpperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 1);
    public static final UpperBlock SKUPPER_BLOCK = new UpperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 2);
    public static final UpperBlock JUPPER_BLOCK = new UpperBlock(FabricBlockSettings.copyOf(Blocks.HOPPER), 3);

    public static final CloakingDeviceBlock CLOAKING_DEVICE_BLOCK = new CloakingDeviceBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
    public static final PointOfInterestType CLOAKING_DEVICE = PointOfInterestType.register(
            "cloaking_device", PointOfInterestType.getAllStatesOf(CLOAKING_DEVICE_BLOCK), 0, 1
    );
    public static BlockEntityType<StarCoreEntity> STAR_CORE_ENTITY;
    public static BlockEntityType<CloakingDeviceBlockEntity> CLOAKING_DEVICE_BLOCK_ENTITY;
    public static BlockEntityType<StorageNodeBlockEntity> STORAGE_NODE_BLOCK_ENTITY;
    public static BlockEntityType<StorageCoreBlockEntity> STORAGE_CORE_BLOCK_ENTITY;
    public static BlockEntityType<HopperBlockEntity> HOPPER_BLOCK_ENTITY;
    public static BlockEntityType<UpperBlockEntity> UPPER_BLOCK_ENTITY;

    public static void registerBlocks() {
        if (FluxTechConfig2.get().enabled.enableEndurium) {
            Registry.register(Registry.FLUID, new Identifier(FluxTech.MOD_ID, "endurium_still"), ENDURIUM);
            Registry.register(Registry.FLUID, new Identifier(FluxTech.MOD_ID, "endurium_flowing"), ENDURIUM_FLOWING);
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "endurium_block"), ENDURIUM_BLOCK);
        }

        if (FluxTechConfig2.get().enabled.enableSmoothEndStone) {
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone"), SMOOTH_END_STONE);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone"), new BlockItem(SMOOTH_END_STONE, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_slab"), SMOOTH_END_STONE_SLAB);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_slab"), new BlockItem(SMOOTH_END_STONE_SLAB, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_stairs"), SMOOTH_END_STONE_STAIRS);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_stairs"), new BlockItem(SMOOTH_END_STONE_STAIRS, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_wall"), SMOOTH_END_STONE_WALL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_wall"), new BlockItem(SMOOTH_END_STONE_WALL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
        }

        CLOAKING_DEVICE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "cloaking_device_entity"), BlockEntityType.Builder.create(CloakingDeviceBlockEntity::new, CLOAKING_DEVICE_BLOCK).build(null));
        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "cloaking_device"), CLOAKING_DEVICE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "cloaking_device"), new BlockItem(CLOAKING_DEVICE_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));


        STORAGE_NODE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "locker_entity"), BlockEntityType.Builder.create(StorageNodeBlockEntity::new, STORAGE_NODE_BLOCK).build(null));
        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "node"), STORAGE_NODE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "node"), new BlockItem(STORAGE_NODE_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

        STORAGE_CORE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "core_entity"), BlockEntityType.Builder.create(StorageCoreBlockEntity::new, STORAGE_CORE_BLOCK).build(null));
        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "core"), STORAGE_CORE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "core"), new BlockItem(STORAGE_CORE_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

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

        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "barbed_wire"), BARBEDWIRE);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "barbed_wire"), new BlockItem(BARBEDWIRE, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

        REDSTONE_RANDOMIZER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "redstone_randomizer_entity"), BlockEntityType.Builder.create(RedstoneRandomizerBlockEntity::new, REDSTONE_RANDOMIZER_BLOCK).build(null));
        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "redstone_randomizer"), REDSTONE_RANDOMIZER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "redstone_randomizer"), new BlockItem(REDSTONE_RANDOMIZER_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

        if (FabricLoader.getInstance().isModLoaded("columns")) {
            FluxTechColumnBlocks.registerColumnBlocks();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM_FLOWING, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(FluxTechBlocks.BARBEDWIRE, RenderLayer.getTranslucent());
    }

    public static Tag<Fluid> fluidTagRegister(String id) {
        return TagRegistry.fluid(new Identifier("c", id));
    }

}
