package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.blockentities.StarCoreEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechBlocks {

    public static final Gel PROPULSION_GEL = new Gel(FabricBlockSettings.of(Material.ICE).hardness(3.5f).slipperiness(1).nonOpaque());
    public static final Gel2 REPULSION_GEL = new Gel2(FabricBlockSettings.of(Material.ICE).hardness(3.5f).slipperiness(1).nonOpaque());
    public static final StarCore CORE = new StarCore(FabricBlockSettings.of(Material.METAL).hardness(3.5f));
    public static BlockEntityType<StarCoreEntity> STAR_CORE_ENTITY;

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "propulsion_gel"), PROPULSION_GEL);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "propulsion_gel"), new BlockItem(PROPULSION_GEL, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "repulsion_gel"), REPULSION_GEL);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "repulsion_gel"), new BlockItem(REPULSION_GEL, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        STAR_CORE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(FluxTech.MOD_ID, "star_core_entity"), BlockEntityType.Builder.create(StarCoreEntity::new, CORE).build(null));
        Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "core"), CORE);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "core"), new BlockItem(CORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
    }


}
