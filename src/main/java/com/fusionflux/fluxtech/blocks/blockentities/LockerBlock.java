package com.fusionflux.fluxtech.blocks.blockentities;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.*;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LockerBlock extends Block implements BlockEntityProvider {


    public LockerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new LockerBlockEntity();
    }


}
