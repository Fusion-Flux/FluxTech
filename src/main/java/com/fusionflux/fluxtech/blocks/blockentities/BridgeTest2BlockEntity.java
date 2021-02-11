package com.fusionflux.fluxtech.blocks.blockentities;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.BridgeTest2Block;
import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
        Direction facing = (Direction)state.get(Properties.FACING);
        Block pumpkinBlock = (Block) (world.isReceivingRedstonePower(getPos()) ? FluxTechBlocks.BRIDGE : Blocks.AIR) ;
        if( extensionTicks < EXTENSION_TIME) {
            BlockPos.Mutable extendPos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
            extendPos.move( facing, extensionTicks );
            for(int i = 0; i < BLOCKS_PER_TICK; i++ ) {
                if( world.isAir( extendPos ) || world.getBlockState(extendPos) == FluxTechBlocks.BRIDGE.getDefaultState()) {
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
    }


    @Override
    public void tick() {
        assert world != null;
        if (!world.isClient) {

                if (world.isReceivingRedstonePower(getPos()) && !alreadyPowered) {
                    alreadyPowered = true;
                    bridgeComplete = false;
                    shouldExtend = true;
                    world.getBlockTickScheduler().schedule(getPos(), getCachedState().getBlock(), 1);
                }
                if (!world.isReceivingRedstonePower(getPos())&&alreadyPowered) {
                    alreadyPowered = false;
                    bridgeComplete = false;
                    shouldExtend = true;
                    world.getBlockTickScheduler().schedule(getPos(), getCachedState().getBlock(), 1);
                    //world.getBlockTickScheduler().schedule(pos, this, 1);
                }


            BlockPos.Mutable bridgeStart = new BlockPos.Mutable(getPos().getX(), getPos().getY(), getPos().getZ());
            bridgeStart.move((Direction)getCachedState().get(Properties.FACING));
            if (world.getBlockState(bridgeStart) == FluxTechBlocks.BRIDGE.getDefaultState() && !world.isReceivingRedstonePower(getPos())) {
                shouldExtend = true;
                bridgeComplete = false;
            }
            if (world.isAir(bridgeStart) && world.isReceivingRedstonePower(getPos())) {
                //world.getBlockTickScheduler().schedule( pos, this, 1);
                shouldExtend = true;
                bridgeComplete = false;
            }
            updateBridge(getCachedState(), (ServerWorld) world, getPos());
            if (bridgeComplete && !world.isReceivingRedstonePower(getPos())) {

            }
          //  if (world.isReceivingRedstonePower(getPos())) {
                world.getBlockTickScheduler().schedule(getPos(), getCachedState().getBlock(), 1);
           // }
            //world.getBlockTickScheduler().schedule( pos, this, 1);
        }
    }



}
