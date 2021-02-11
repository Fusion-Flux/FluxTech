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



    public BridgeTest2BlockEntity() {
        super(FluxTechBlocks.EMITTER_TEST_ENTITY);
    }



    public static void updateBridge(BlockState state, ServerWorld world, BlockPos pos) {
        if( !FluxTechBlocks.EMITTER_TEST.bridgeComplete && FluxTechBlocks.EMITTER_TEST.shouldExtend ) {
            extendBridge(state, world, pos);
        }
        else {
            FluxTechBlocks.EMITTER_TEST.bridgeComplete = true;
            FluxTechBlocks.EMITTER_TEST.shouldExtend = false;
            FluxTechBlocks.EMITTER_TEST.extensionTicks = 0;
        }
    }

    private static void extendBridge(BlockState state, ServerWorld world, BlockPos pos) {
        Direction facing = (Direction)state.get(BridgeTest2Block.FACING);
        Block pumpkinBlock = (Block) FluxTechBlocks.BRIDGE;
        if( FluxTechBlocks.EMITTER_TEST.extensionTicks < BridgeTest2Block.EXTENSION_TIME) {
            BlockPos.Mutable extendPos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
            extendPos.move( facing, FluxTechBlocks.EMITTER_TEST.extensionTicks );
            for(int i = 0; i < BridgeTest2Block.BLOCKS_PER_TICK; i++ ) {
                if( world.isAir( extendPos )) {
                    world.setBlockState( extendPos, pumpkinBlock.getDefaultState(), 3 );
                    System.out.println(extendPos);
                }
                else {
                    System.out.println("we broke it");
                    FluxTechBlocks.EMITTER_TEST.bridgeComplete = true;
                    FluxTechBlocks.EMITTER_TEST.shouldExtend = false;
                    break;
                }
                extendPos.move( facing );
            }
            ++FluxTechBlocks.EMITTER_TEST.extensionTicks;
        }
    }


    @Override
    public void tick() {
        assert world != null;
        if (!world.isClient) {

                if (world.isReceivingRedstonePower(pos) && !FluxTechBlocks.EMITTER_TEST.alreadyPowered) {
                    FluxTechBlocks.EMITTER_TEST.alreadyPowered = true;
                    FluxTechBlocks.EMITTER_TEST.bridgeComplete = false;
                    FluxTechBlocks.EMITTER_TEST.shouldExtend = true;
                    world.getBlockTickScheduler().schedule(pos, FluxTechBlocks.EMITTER_TEST, 1);
                }
                if (!world.isReceivingRedstonePower(pos)) {
                    FluxTechBlocks.EMITTER_TEST.alreadyPowered = false;
                    FluxTechBlocks.EMITTER_TEST.bridgeComplete = true;
                    FluxTechBlocks.EMITTER_TEST.shouldExtend = false;

                    //world.getBlockTickScheduler().schedule(pos, this, 1);
                }


            BlockPos.Mutable bridgeStart = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
            bridgeStart.move((Direction) FluxTechBlocks.EMITTER_TEST.getDefaultState().get(BridgeTest2Block.FACING));
            if (!world.isReceivingRedstonePower(pos)) {
                FluxTechBlocks.EMITTER_TEST.bridgeComplete = true;
                FluxTechBlocks.EMITTER_TEST.shouldExtend = false;
            }
            if (world.isAir(bridgeStart)) {
                //world.getBlockTickScheduler().schedule( pos, this, 1);
                FluxTechBlocks.EMITTER_TEST.shouldExtend = true;
                FluxTechBlocks.EMITTER_TEST.bridgeComplete = false;
            }
            updateBridge(FluxTechBlocks.EMITTER_TEST.getDefaultState(), (ServerWorld) world, this.getPos());
            if (FluxTechBlocks.EMITTER_TEST.bridgeComplete && !world.isReceivingRedstonePower(pos)) {

            }
            if (world.isReceivingRedstonePower(pos)) {
                world.getBlockTickScheduler().schedule(pos, FluxTechBlocks.EMITTER_TEST, 1);
            }
            //world.getBlockTickScheduler().schedule( pos, this, 1);
        }
    }



}
