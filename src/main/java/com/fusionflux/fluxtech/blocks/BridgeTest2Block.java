package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.blocks.blockentities.BridgeTest2BlockEntity;
import com.fusionflux.fluxtech.blocks.blockentities.StarCoreEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BridgeTest2Block extends BlockWithEntity {







    protected BridgeTest2Block(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BridgeTest2BlockEntity();
    }
    @Override
    protected void appendProperties( StateManager.Builder<Block, BlockState> builder) {
        builder.add( Properties.FACING, Properties.DELAY );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)FluxTechBlocks.EMITTER_TEST.getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if ( world.isReceivingRedstonePower( pos )) {
            world.getBlockTickScheduler().schedule(pos, FluxTechBlocks.EMITTER_TEST, 1);
        }
        //world.getBlockTickScheduler().schedule(pos, this, 1);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(Properties.FACING, rotation.rotate((Direction)state.get(Properties.FACING)));
    }

/*@Override
    public void neighborUpdate( BlockState blockState, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify ) {
        if( !world.isClient ) {
            if( world.isReceivingRedstonePower( pos ) && !FluxTechBlocks.EMITTER_TEST.alreadyPowered ) {
                FluxTechBlocks.EMITTER_TEST.alreadyPowered = true;
                FluxTechBlocks.EMITTER_TEST.bridgeComplete = false;
                FluxTechBlocks.EMITTER_TEST.shouldExtend = true;
                world.getBlockTickScheduler().schedule(pos, FluxTechBlocks.EMITTER_TEST, 1);
            }
            if(!world.isReceivingRedstonePower( pos )){
                FluxTechBlocks.EMITTER_TEST.alreadyPowered = false;
                FluxTechBlocks.EMITTER_TEST.bridgeComplete = true;
                FluxTechBlocks.EMITTER_TEST.shouldExtend = false;

                //world.getBlockTickScheduler().schedule(pos, this, 1);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos.Mutable bridgeStart = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
        bridgeStart.move( (Direction)state.get(FACING) );
        if( !world.isReceivingRedstonePower( pos ) ) {
            FluxTechBlocks.EMITTER_TEST.bridgeComplete = true;
            FluxTechBlocks.EMITTER_TEST.shouldExtend = false;
        }
        if( world.isAir( bridgeStart )) {
            //world.getBlockTickScheduler().schedule( pos, this, 1);
            FluxTechBlocks.EMITTER_TEST.shouldExtend = true;
            FluxTechBlocks.EMITTER_TEST.bridgeComplete = false;
        }
        BridgeTest2BlockEntity.updateBridge(state, world, pos);
        if( FluxTechBlocks.EMITTER_TEST.bridgeComplete && !world.isReceivingRedstonePower( pos )) {

        }
        if( world.isReceivingRedstonePower( pos ) ) {
            world.getBlockTickScheduler().schedule( pos, FluxTechBlocks.EMITTER_TEST, 1);
        }
        //world.getBlockTickScheduler().schedule( pos, this, 1);
    }*/



}
