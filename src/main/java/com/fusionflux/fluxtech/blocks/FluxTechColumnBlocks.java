package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.config.FluxTechConfig;

import io.github.haykam821.columns.block.ColumnBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechColumnBlocks {

    public static Block SMOOTH_END_STONE_COLUMN;

    public static void registerColumnBlocks() {
        if (FluxTechConfig.get().enabled.enableSmoothEndStone) {
            SMOOTH_END_STONE_COLUMN = new ColumnBlock(FabricBlockSettings.copy(FluxTechBlocks.SMOOTH_END_STONE));
            Registry.register(Registry.BLOCK, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_column"), SMOOTH_END_STONE_COLUMN);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "smooth_end_stone_column"), new BlockItem(SMOOTH_END_STONE_COLUMN, new Item.Settings().group(FluxTech.FLUXTECH_GROUP)));
        }
    }

}
