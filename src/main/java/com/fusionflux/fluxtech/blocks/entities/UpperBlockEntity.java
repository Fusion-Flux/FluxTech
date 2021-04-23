package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.blocks.UpperBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class UpperBlockEntity extends AbstractHopperBlockEntity {
    VoxelShape INSIDE_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D);
    VoxelShape BELOW_SHAPE = Block.createCuboidShape(0.0D, -16.0D, 0.0D, 16.0D, 0.0D, 16.0D);
    VoxelShape INPUT_AREA_SHAPE = VoxelShapes.union(INSIDE_SHAPE, BELOW_SHAPE);

    public UpperBlockEntity() {
        this(1);
    }

    public UpperBlockEntity(int distance) {
        super(FluxTechBlocks.UPPER_BLOCK_ENTITY, distance);
    }

    @Override
    public VoxelShape getInputAreaShape() {
        return INPUT_AREA_SHAPE;
    }

    @Override
    protected Direction getDirection() {
        return this.getCachedState().get(UpperBlock.FACING);
    }

    @Override
    public double getInputInventoryY() {
        return -1.0D;
    }
}
