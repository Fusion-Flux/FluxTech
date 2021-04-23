package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.blocks.blockentities.LockerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class LockerBlock extends Block implements BlockEntityProvider {

    public LockerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new LockerBlockEntity();
    }
}
