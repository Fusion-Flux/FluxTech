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
    private ItemStack item = ItemStack.EMPTY;
    //private LinkedList<StorageNodeBlockEntity> connectedNodes = new LinkedList<StorageNodeBlockEntity>();
    public final List<BlockPos> connectedNodes = new ArrayList<>();

    public StorageCoreBlockEntity() {
        super(FluxTechBlocks.STORAGE_CORE_BLOCK_ENTITY);
    }

    public void addNewNodes(BlockPos nodeBlockPos){
        connectedNodes.add(nodeBlockPos);
        System.out.println(connectedNodes);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        int size = tag.getInt("size");
        for (int i = 0; i < size; i++) {
            connectedNodes.add(new BlockPos(
                    tag.getInt(i + "x"),
                    tag.getInt(i + "y"),
                    tag.getInt(i + "z")
            ));
        }

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        tag.putInt("size", connectedNodes.size());
        for (int i = 0; i < connectedNodes.size(); i++) {
            tag.putInt(i + "x", connectedNodes.get(i).getX());
            tag.putInt(i + "y", connectedNodes.get(i).getY());
            tag.putInt(i + "z", connectedNodes.get(i).getZ());
        }

        return tag;
    }

    @Override
    public Text getName() {
        return new TranslatableText("container.core");
    }
}
