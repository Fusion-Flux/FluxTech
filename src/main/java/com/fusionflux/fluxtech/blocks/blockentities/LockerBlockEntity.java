package com.fusionflux.fluxtech.blocks.blockentities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LockerBlockEntity extends BlockEntity {
    public LockerBlockEntity() {
        super(FluxTechBlocks.LOCKER_BLOCK_ENTITY);
    }


}
