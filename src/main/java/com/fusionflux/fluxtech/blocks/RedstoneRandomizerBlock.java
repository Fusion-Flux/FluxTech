package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.blocks.entities.RedstoneRandomizerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class RedstoneRandomizerBlock extends BlockWithEntity {
    public static final IntProperty POWER;

    static {
        POWER = Properties.POWER;
    }

    public RedstoneRandomizerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        //world.setBlockState(pos, state.with(POWER, random.nextInt(16)));
        super.randomTick(state, world, pos, random);
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWER);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new RedstoneRandomizerBlockEntity();
    }
}
