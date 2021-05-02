package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.blocks.StorageNodeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StorageCoreBlockEntity extends BlockEntity implements Nameable {

    public final List<BlockPos> connectedNodes = new ArrayList<>();

    public StorageCoreBlockEntity() {
        super(FluxTechBlocks.STORAGE_CORE_BLOCK_ENTITY);
    }

    public void addNewNodes(BlockPos nodeBlockPos){
        if(this.world!=null) {
            if (!this.world.isClient) {
                connectedNodes.add(nodeBlockPos);
            }
        }
    }

    public void onDelete(BlockPos deletedLocker) {
        for (BlockPos locker : connectedNodes) {
            if (locker == deletedLocker) {
                connectedNodes.remove(deletedLocker);
                break;
            }
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        int size = tag.getInt("size");
        for (int i = 0; i < size; i++) {
            connectedNodes.add(new BlockPos(
                    tag.getInt(i + "nodex"),
                    tag.getInt(i + "nodey"),
                    tag.getInt(i + "nodez")
            ));
        }

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        tag.putInt("size", connectedNodes.size());
        for (int i = 0; i < connectedNodes.size(); i++) {
            tag.putInt(i + "nodex", connectedNodes.get(i).getX());
            tag.putInt(i + "nodey", connectedNodes.get(i).getY());
            tag.putInt(i + "nodez", connectedNodes.get(i).getZ());
        }

        return tag;
    }

    @Override
    public Text getName() {
        return new TranslatableText("container.core");
    }
}
