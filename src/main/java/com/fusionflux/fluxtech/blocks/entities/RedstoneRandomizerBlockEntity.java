package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;

import java.util.Random;

public class RedstoneRandomizerBlockEntity extends BlockEntity implements Tickable {
    private int age;
    public RedstoneRandomizerBlockEntity() {
        super(FluxTechBlocks.REDSTONE_RANDOMIZER_BLOCK_ENTITY);
    }

    @Override
    public void tick() {
        Random random = new Random();
        if (world != null) {
            if(this.age % 100 == 0)
            world.setBlockState(pos, world.getBlockState(pos).with(Properties.POWER, random.nextInt(16)));
        }
        age++;
    }


}
