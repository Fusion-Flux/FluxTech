package com.fusionflux.fluxtech.blocks.blockentities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.*;
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
import net.minecraft.world.World;

import java.util.Random;

public class HardLightBridgeEmitterBlock extends FacingBlock {
    public static final DirectionProperty FACING;

    private static final int MAX_RANGE = 27;
    private static final int BLOCKS_PER_TICK = 1;
    private static final int EXTENSION_TIME = MAX_RANGE / BLOCKS_PER_TICK;

    private int extensionTicks = 0;
    private boolean bridgeComplete = false;
    private boolean alreadyPowered = false;
    private boolean shouldExtend = false;

    public HardLightBridgeEmitterBlock( Settings settings ) {
        super( settings );
    }

    /*@Override
    public ActionResult onUse( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit ) {

    }*/

    @Override
    protected void appendProperties( StateManager.Builder<Block, BlockState> builder) {
        builder.add( Properties.FACING, Properties.DELAY );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }


    @Override
    public void onPlaced( World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if ( world.isReceivingRedstonePower( pos )) {
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }
        //world.getBlockTickScheduler().schedule(pos, this, 1);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    @Override
    public void neighborUpdate( BlockState blockState, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify ) {
        if( !world.isClient ) {
            if( world.isReceivingRedstonePower( pos ) && !this.alreadyPowered ) {
                this.alreadyPowered = true;
                this.shouldExtend = true;
                world.getBlockTickScheduler().schedule(pos, this, 1);
            }else{
                this.alreadyPowered = false;
                this.shouldExtend = false;
                this.bridgeComplete = true;
                //world.getBlockTickScheduler().schedule(pos, this, 1);
            }
        }
    }

    @Override
    public void scheduledTick( BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos.Mutable bridgeStart = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
        bridgeStart.move( (Direction)state.get(FACING) );
        if( !world.isReceivingRedstonePower( pos ) ) {
            this.bridgeComplete = true;

        }
        if( world.isAir( bridgeStart )) {
            //world.getBlockTickScheduler().schedule( pos, this, 1);
            this.shouldExtend = true;
            this.bridgeComplete = false;
        }
        this.updateBridge(state, world, pos);
        if( this.bridgeComplete && !world.isReceivingRedstonePower( pos )) {

        }
        world.getBlockTickScheduler().schedule( pos, this, 1);
    }

    private void updateBridge(BlockState state, ServerWorld world, BlockPos pos) {
        if( !bridgeComplete && this.shouldExtend ) {
            this.extendBridge(state, world, pos);
        }
        else {
            this.bridgeComplete = true;
            this.shouldExtend = false;
            extensionTicks = 0;
        }
    }

    private void extendBridge(BlockState state, ServerWorld world, BlockPos pos) {
        Direction facing = (Direction)state.get(FACING);
        Block pumpkinBlock = (Block) FluxTechBlocks.BRIDGE;
        if( extensionTicks < EXTENSION_TIME ) {
            BlockPos.Mutable extendPos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
            extendPos.move( facing, extensionTicks );
            for( int i = 0; i < BLOCKS_PER_TICK; i++ ) {
                if( world.isAir( extendPos )) {
                    world.setBlockState( extendPos, pumpkinBlock.getDefaultState(), 3 );
                    System.out.println(extendPos);
                }
                else {
                    System.out.println("we broke it");
                    this.bridgeComplete = true;
                    this.shouldExtend = false;
                    break;
                }
                extendPos.move( facing );
            }
            ++extensionTicks;
        }
    }

    static {
        FACING = FacingBlock.FACING;
    }

}


