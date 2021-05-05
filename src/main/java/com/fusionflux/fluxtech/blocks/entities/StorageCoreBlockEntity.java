package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.blocks.StorageNodeBlock;
import com.fusionflux.fluxtech.blocks.inventory.ImplementedInventory;
import com.fusionflux.fluxtech.blocks.inventory.MultiInventory;
import com.fusionflux.fluxtech.client.rendering.BoxScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StorageCoreBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory, Nameable {

    public final List<BlockPos> connectedNodes = new ArrayList<>();
    //private DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);

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

        if (this.world != null) {
            if (!this.world.isClient) {
                StorageNodeBlockEntity node;
                for (BlockPos locker : connectedNodes) {
                    node = (StorageNodeBlockEntity) this.world.getBlockEntity(locker);
                    if(node!=null) {
                        node.setConnectedCore();
                    }
                }
                connectedNodes.clear();
                updateNearbyNodes();
            }
        }
    }

public void updateNearbyNodes(){
    StorageNodeBlockEntity node;
    for (Direction offsetdir : Direction.values()) {
        if (this.world.getBlockState(this.getPos().offset(offsetdir)).getBlock().equals(FluxTechBlocks.STORAGE_NODE_BLOCK)) {
            node = (StorageNodeBlockEntity) this.world.getBlockEntity(new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()).offset(offsetdir));
            if(node!=null) {
                node.checkConnections();
            }

        }
    }
}

    @Override
    public void markRemoved() {
        if (this.world != null) {
            if (!this.world.isClient) {
                if (!this.removed) {
                    if (!connectedNodes.isEmpty()) {
                        for (BlockPos nodes : this.connectedNodes) {
                            StorageNodeBlockEntity node;
                            node = (StorageNodeBlockEntity) this.world.getBlockEntity(nodes);
                            if (node != null) {
                                node.removeStoredBlockPos(this.getPos());
                            }
                        }
                    }
                }
            }
        }
        this.removed = true;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        //Inventories.fromTag(tag, items);
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
        //Inventories.toTag(tag,items);
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

    private @Nullable Inventory getInventory(int slot) {
        if (slot < this.connectedNodes.size() * 27) {
            BlockEntity blockEntity = this.world.getBlockEntity(this.connectedNodes.get(slot / 27));

            if (blockEntity instanceof Inventory) {
                return (Inventory) blockEntity;
            }
        }

        return null;
    }

    @Override
    public int size() {
        return connectedNodes.size()*27;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.connectedNodes.size();) {
            Inventory child = this.getInventory(i);
            if (child != null && !child.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        Inventory child = this.getInventory(slot);
        return child == null ? ItemStack.EMPTY:child.getStack(slot%27);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        Inventory child = this.getInventory(slot);
        return child == null ? ItemStack.EMPTY:child.removeStack(slot%27,amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        Inventory child = this.getInventory(slot);
        return child == null ? ItemStack.EMPTY:child.removeStack(slot%27);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        Inventory child = this.getInventory(slot);
        if(child!=null){
            child.setStack(slot%27,stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.connectedNodes.size();) {
            Inventory child = this.getInventory(i);
            if (child != null) {
                child.clear();
            }
        }
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BoxScreenHandler(syncId, playerInventory, this);
    }
}
