package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.blockentities.*;
import com.fusionflux.fluxtech.config.FluxTechConfig;
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
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechBlocks {

    public static final PropulsionGel PROPULSION_GEL = new PropulsionGel(FabricBlockSettings.of(Material.WATER).hardness(0f).slipperiness(1).nonOpaque().sounds(new BlockSoundGroup(1,-1, SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundEvents.BLOCK_HONEY_BLOCK_HIT, SoundEvents.BLOCK_HONEY_BLOCK_FALL)));
    public static final RepulsionGel REPULSION_GEL = new RepulsionGel(FabricBlockSettings.of(Material.WATER).hardness(0f).nonOpaque().sounds(new BlockSoundGroup(1,-1, SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundEvents.BLOCK_HONEY_BLOCK_HIT, SoundEvents.BLOCK_HONEY_BLOCK_FALL)));
    public static final Gel GEL = new Gel(FabricBlockSettings.of(Material.WATER).hardness(0f).nonOpaque().sounds(new BlockSoundGroup(1,-1, SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundEvents.BLOCK_HONEY_BLOCK_HIT, SoundEvents.BLOCK_HONEY_BLOCK_FALL)));
    //public static final StarCore CORE = new StarCore(FabricBlockSettings.of(Material.METAL).hardness(3.5f));

    public static final HardLightBridgeEmitterBlock HLB_EMITTER_BLOCK = new HardLightBridgeEmitterBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).nonOpaque().sounds(BlockSoundGroup.METAL));
    public static final HardLightBridgeBlock HLB_BLOCK = new HardLightBridgeBlock(FabricBlockSettings.of(Material.PLANT).hardness(999999f).nonOpaque().luminance(10).resistance(9999999999f).sounds(new BlockSoundGroup(1,1, SoundEvents.BLOCK_NETHERITE_BLOCK_BREAK, SoundEvents.BLOCK_NETHERITE_BLOCK_STEP, SoundEvents.BLOCK_NETHERITE_BLOCK_PLACE, SoundEvents.BLOCK_NETHERITE_BLOCK_HIT, SoundEvents.BLOCK_NETHERITE_BLOCK_FALL)));

    public static final HighSpeedRail RAILTEST = new HighSpeedRail(false,FabricBlockSettings.of(Material.SUPPORTED).noCollision().strength(0.7F));


    public static final FlowableFluid ENDURIUM = new Endurium.Source();
    public static final FlowableFluid ENDURIUM_FLOWING = new Endurium.Flowing();
    public static final Block ENDURIUM_BLOCK = new EnduriumBlock(ENDURIUM);

    public static final Block SMOOTH_END_STONE = new Block(FabricBlockSettings.of(Material.STONE).hardness(3.5f));
    public static final Block SMOOTH_END_STONE_SLAB = new SlabBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));
    public static final Block SMOOTH_END_STONE_STAIRS = new CustomStairs(FluxTechBlocks.SMOOTH_END_STONE);
    public static final Block SMOOTH_END_STONE_WALL = new WallBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));

    public static final Block SMOOTH_WHITE_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final Block CHISELED_SMOOTH_WHITE_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final PillarBlock TOP_SMOOTH_WHITE_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final PillarBlock BOTTOM_SMOOTH_WHITE_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final DirectionalBlock BOTTOM_2X2_SMOOTH_WHITE_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final DirectionalBlock TOP_2X2_SMOOTH_WHITE_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));

    public static final Block SMOOTH_GREY_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final Block CHISELED_SMOOTH_GREY_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final PillarBlock TOP_SMOOTH_GREY_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final PillarBlock BOTTOM_SMOOTH_GREY_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final DirectionalBlock BOTTOM_2X2_SMOOTH_GREY_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final DirectionalBlock TOP_2X2_SMOOTH_GREY_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));

    public static final Block PADDED_GREY_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final Block CHISELED_PADDED_GREY_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final PillarBlock TOP_PADDED_GREY_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final PillarBlock BOTTOM_PADDED_GREY_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final DirectionalBlock BOTTOM_2X2_PADDED_GREY_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));
    public static final DirectionalBlock TOP_2X2_PADDED_GREY_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f).sounds(BlockSoundGroup.NETHERITE));

    public static final Block GRITTY_WHITE_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final Block CHISELED_GRITTY_WHITE_PANEL = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final PillarBlock TOP_GRITTY_WHITE_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final PillarBlock BOTTOM_GRITTY_WHITE_PANEL = new PillarBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final DirectionalBlock BOTTOM_2X2_GRITTY_WHITE_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static final DirectionalBlock TOP_2X2_GRITTY_WHITE_PANEL = new DirectionalBlock(FabricBlockSettings.of(Material.METAL).hardness(3.5f));

    //public static Tag<Block> MY_TAG = TagRegistry.block(new Identifier("fluxtech", "hpd_deny_launch"));

    public static BlockEntityType<StarCoreEntity> STAR_CORE_ENTITY;
    public static BlockEntityType<HardLightBridgeEmitterBlockEntity> HLB_EMITTER_ENTITY;
    public static BlockEntityType<HardLightBridgeBlockEntity> HLB_BLOCK_ENTITY;


    public static void registerBlocks() {
        if (FluxTechConfig2.get().enabled.enableGels) {
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "propulsion_gel"), PROPULSION_GEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "propulsion_gel"), new GelBucket(PROPULSION_GEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).maxCount(1)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "repulsion_gel"), REPULSION_GEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "repulsion_gel"), new GelBucket(REPULSION_GEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).maxCount(1)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "gel"), GEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "gel"), new GelBucket(GEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).maxCount(1)));
        }

        if (FluxTechConfig2.get().enabled.enableHLB) {
            HLB_EMITTER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "emitter_test_entity"), BlockEntityType.Builder.create(HardLightBridgeEmitterBlockEntity::new, HLB_EMITTER_BLOCK).build(null));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "emitter"), HLB_EMITTER_BLOCK);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "emitter"), new BlockItem(HLB_EMITTER_BLOCK, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            HLB_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "bridge_test_entity"), BlockEntityType.Builder.create(HardLightBridgeBlockEntity::new, HLB_BLOCK).build(null));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bridge_test"), HLB_BLOCK);
        }

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

        //STAR_CORE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "star_core_entity"), BlockEntityType.Builder.create(StarCoreEntity::new, CORE).build(null));
        //Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "core"), CORE);
        if (FluxTechConfig2.get().enabled.enablePortal2Blocks) {
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_white_panel"), SMOOTH_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_white_panel"), new BlockItem(SMOOTH_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "chiseled_smooth_white_panel"), CHISELED_SMOOTH_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "chiseled_smooth_white_panel"), new BlockItem(CHISELED_SMOOTH_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_smooth_white_panel"), TOP_SMOOTH_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_smooth_white_panel"), new BlockItem(TOP_SMOOTH_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_smooth_white_panel"), BOTTOM_SMOOTH_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_smooth_white_panel"), new BlockItem(BOTTOM_SMOOTH_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_2x2_smooth_white_panel"), BOTTOM_2X2_SMOOTH_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_2x2_smooth_white_panel"), new BlockItem(BOTTOM_2X2_SMOOTH_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_2x2_smooth_white_panel"), TOP_2X2_SMOOTH_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_2x2_smooth_white_panel"), new BlockItem(TOP_2X2_SMOOTH_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_grey_panel"), SMOOTH_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_grey_panel"), new BlockItem(SMOOTH_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "chiseled_smooth_grey_panel"), CHISELED_SMOOTH_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "chiseled_smooth_grey_panel"), new BlockItem(CHISELED_SMOOTH_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_smooth_grey_panel"), TOP_SMOOTH_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_smooth_grey_panel"), new BlockItem(TOP_SMOOTH_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_smooth_grey_panel"), BOTTOM_SMOOTH_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_smooth_grey_panel"), new BlockItem(BOTTOM_SMOOTH_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_2x2_smooth_grey_panel"), BOTTOM_2X2_SMOOTH_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_2x2_smooth_grey_panel"), new BlockItem(BOTTOM_2X2_SMOOTH_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_2x2_smooth_grey_panel"), TOP_2X2_SMOOTH_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_2x2_smooth_grey_panel"), new BlockItem(TOP_2X2_SMOOTH_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "padded_grey_panel"), PADDED_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "padded_grey_panel"), new BlockItem(PADDED_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "chiseled_padded_grey_panel"), CHISELED_PADDED_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "chiseled_padded_grey_panel"), new BlockItem(CHISELED_PADDED_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_padded_grey_panel"), TOP_PADDED_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_padded_grey_panel"), new BlockItem(TOP_PADDED_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_padded_grey_panel"), BOTTOM_PADDED_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_padded_grey_panel"), new BlockItem(BOTTOM_PADDED_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_2x2_padded_grey_panel"), BOTTOM_2X2_PADDED_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_2x2_padded_grey_panel"), new BlockItem(BOTTOM_2X2_PADDED_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_2x2_padded_grey_panel"), TOP_2X2_PADDED_GREY_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_2x2_padded_grey_panel"), new BlockItem(TOP_2X2_PADDED_GREY_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "gritty_white_panel"), GRITTY_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "gritty_white_panel"), new BlockItem(GRITTY_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "chiseled_gritty_white_panel"), CHISELED_GRITTY_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "chiseled_gritty_white_panel"), new BlockItem(CHISELED_GRITTY_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_gritty_white_panel"), TOP_GRITTY_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_gritty_white_panel"), new BlockItem(TOP_GRITTY_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_gritty_white_panel"), BOTTOM_GRITTY_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_gritty_white_panel"), new BlockItem(BOTTOM_GRITTY_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "bottom_2x2_gritty_white_panel"), BOTTOM_2X2_GRITTY_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "bottom_2x2_gritty_white_panel"), new BlockItem(BOTTOM_2X2_GRITTY_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "top_2x2_gritty_white_panel"), TOP_2X2_GRITTY_WHITE_PANEL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "top_2x2_gritty_white_panel"), new BlockItem(TOP_2X2_GRITTY_WHITE_PANEL, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));

            // Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "core"), new BlockItem(CORE, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(FluxTechBlocks.HLB_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(FluxTechBlocks.HLB_EMITTER_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(FluxTechBlocks.ENDURIUM_FLOWING, RenderLayer.getTranslucent());
    }

    public static final Tag<Fluid> ENDURIUM_TAG = fluidTagRegister("endurium");

    public static Tag<Fluid> fluidTagRegister(String id) {
        return TagRegistry.fluid(new Identifier("c", id));

    }

}
