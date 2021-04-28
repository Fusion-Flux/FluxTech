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

import java.util.LinkedList;

public class StorageCoreBlockEntity extends BlockEntity implements Nameable {
    private ItemStack item = ItemStack.EMPTY;
    private LinkedList<StorageNodeBlockEntity> connectedNodes = new LinkedList<StorageNodeBlockEntity>();

    public StorageCoreBlockEntity() {
        super(FluxTechBlocks.STORAGE_CORE_BLOCK_ENTITY);
    }

    public void addNewNodes(StorageNodeBlockEntity node){
        connectedNodes.add(node);
        System.out.println(connectedNodes.toString());
    }


    @Override
    public Text getName() {
        return new TranslatableText("container.core");
    }
}
