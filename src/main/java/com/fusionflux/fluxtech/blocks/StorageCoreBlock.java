package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.blocks.entities.StorageCoreBlockEntity;
import com.fusionflux.fluxtech.blocks.entities.StorageNodeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class StorageCoreBlock extends Block implements BlockEntityProvider {

    public StorageCoreBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new StorageCoreBlockEntity();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StorageCoreBlockEntity) {
            ((StorageCoreBlockEntity) blockEntity).updateNearbyNodes();
        }
    }



}
