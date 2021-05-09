package com.fusionflux.fluxtech.blocks;

import com.fusionflux.fluxtech.blocks.entities.StorageCoreBlockEntity;
import com.fusionflux.fluxtech.client.rendering.CoreGui;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StorageCoreBlock extends BlockWithEntity implements BlockEntityProvider {

    public StorageCoreBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        PlayerEntity entity = player;
        if (!world.isClient && entity != null) {
            StorageCoreBlockEntity blockEntity = (StorageCoreBlockEntity) world.getBlockEntity(pos);
            if (blockEntity != null) {
                CoreGui.open((ServerPlayerEntity) entity, blockEntity);
            }
        }
        return ActionResult.SUCCESS;
    }

}
