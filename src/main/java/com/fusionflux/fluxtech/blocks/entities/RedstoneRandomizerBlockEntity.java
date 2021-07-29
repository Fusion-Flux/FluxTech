package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class RedstoneRandomizerBlockEntity extends BlockEntity {
    private int age = 1;

    public RedstoneRandomizerBlockEntity(BlockPos pos, BlockState state) {
        super(FluxTechBlocks.REDSTONE_RANDOMIZER_BLOCK_ENTITY,pos,state);
    }


    public static void tick(World world, BlockPos pos, BlockState state, RedstoneRandomizerBlockEntity blockEntity) {
        Random random = new Random();
        if (world != null) {
            if(blockEntity.age % 100 == 0)
            world.setBlockState(pos, world.getBlockState(pos).with(Properties.POWER, random.nextInt(16)));
        }
        blockEntity.age++;
    }


}
