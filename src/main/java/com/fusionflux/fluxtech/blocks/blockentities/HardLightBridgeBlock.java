package com.fusionflux.fluxtech.blocks.blockentities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

// TODO replace with your own reference as appropriate

public class HardLightBridgeBlock extends BlockWithEntity {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public HardLightBridgeBlock( Settings settings ) {
        super( settings );
    }

    @Override
    public BlockEntity createBlockEntity( BlockView world ) {
        return new HardLightBridgeBlockEntity();
    }

    @Override
    protected void appendProperties( StateManager.Builder<Block, BlockState> builder) {
        builder.add( Properties.FACING );
    }

@Override
    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }
    @Override
    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }
    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }



    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(Properties.FACING, rotation.rotate(( Direction )state.get(Properties.FACING)));
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

@Override
    @Environment(EnvType.CLIENT)
public boolean isSideInvisible( BlockState state, BlockState stateFrom, Direction direction ) {
    if( stateFrom.isOf( FluxTechBlocks.HLB_EMITTER_BLOCK )) {
        return stateFrom.get( Properties.POWERED );
    }
    else return stateFrom.isOf( FluxTechBlocks.HLB_BLOCK );
}
}