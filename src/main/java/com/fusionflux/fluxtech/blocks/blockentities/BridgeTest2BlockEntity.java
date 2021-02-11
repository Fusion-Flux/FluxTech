package com.fusionflux.fluxtech.blocks.blockentities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.*;


public class BridgeTest2BlockEntity extends BlockEntity implements Tickable {

    public final int MAX_RANGE = 27;
    public final int BLOCKS_PER_TICK = 1;
    public final int EXTENSION_TIME = MAX_RANGE / BLOCKS_PER_TICK;

    public int extensionTicks = 0;

    public boolean bridgeComplete = false;
    public boolean alreadyPowered = false;
    public boolean shouldExtend = false;

    public BridgeTest2BlockEntity() {
        super(FluxTechBlocks.EMITTER_TEST_ENTITY);
    }



    public void updateBridge(BlockState state, ServerWorld world, BlockPos pos) {
        if( !bridgeComplete && shouldExtend ) {
            extendBridge(state, world, pos);
        }
        else {
            bridgeComplete = true;
            shouldExtend = false;
            extensionTicks = 0;
        }
    }

    private void extendBridge(BlockState state, ServerWorld world, BlockPos pos) {
        Direction facing = state.get(Properties.FACING);
        Block pumpkinBlock =  (world.isReceivingRedstonePower(getPos()) ? FluxTechBlocks.BRIDGE : Blocks.AIR) ;
            BlockPos.Mutable extendPos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
            extendPos.move( facing, extensionTicks );
            for(int i = 0; i < BLOCKS_PER_TICK; i++ ) {
                if( (world.isAir( extendPos ) || world.getBlockState(extendPos) == FluxTechBlocks.BRIDGE.getDefaultState())&&(extensionTicks < EXTENSION_TIME )) {
                    world.setBlockState( extendPos,pumpkinBlock.getDefaultState(), 3 );
                    System.out.println(extendPos);
                }
                else {
                    System.out.println("we broke it");
                    bridgeComplete = true;
                    shouldExtend = false;
                    break;
                }
                extendPos.move( facing );
            }
            ++extensionTicks;
    }


    @Override
    public void tick() {
        assert world != null;
        if (!world.isClient) {

                if (world.isReceivingRedstonePower(getPos()) && !alreadyPowered) {
                    alreadyPowered = true;
                    bridgeComplete = false;
                    shouldExtend = true;
                    extensionTicks = 0;
                }
                if (!world.isReceivingRedstonePower(getPos())&&alreadyPowered) {
                    alreadyPowered = false;
                    bridgeComplete = false;
                    shouldExtend = true;
                    extensionTicks = 0;
                }

            BlockPos.Mutable bridgeStart = new BlockPos.Mutable(getPos().getX(), getPos().getY(), getPos().getZ());
            bridgeStart.move(getCachedState().get(Properties.FACING));
            if (world.getBlockState(bridgeStart) == FluxTechBlocks.BRIDGE.getDefaultState() && !world.isReceivingRedstonePower(getPos())) {
                shouldExtend = true;
                bridgeComplete = false;
            }
            if (world.isAir(bridgeStart) && world.isReceivingRedstonePower(getPos())) {
                shouldExtend = true;
                bridgeComplete = false;
            }
            updateBridge(getCachedState(), (ServerWorld) world, getPos());
        }

    }



}
