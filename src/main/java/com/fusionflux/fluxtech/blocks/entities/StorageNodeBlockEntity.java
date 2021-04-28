package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.BlockEntityProvider;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.World;

import java.util.Objects;

public class StorageNodeBlockEntity extends BlockEntity implements Inventory, Nameable {
    private ItemStack item = ItemStack.EMPTY;
    private StorageCoreBlockEntity savedCore;
    public StorageNodeBlockEntity() {
        super(FluxTechBlocks.STORAGE_NODE_BLOCK_ENTITY);
    }

    @Override
    public void setLocation(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos.toImmutable();
    }

    @Override
    public void cancelRemoval() {
        this.removed = false;
        StorageCoreBlockEntity core;
        StorageNodeBlockEntity node;
        if (this.world != null) {
            if (!this.world.isClient) {
                for (int i = 1; i < 7; i++) {
                    Direction offsetdir = Direction.NORTH;
                    switch (i) {
                        case 1:
                            offsetdir = Direction.NORTH;
                            break;
                        case 2:
                            offsetdir = Direction.EAST;
                            break;
                        case 3:
                            offsetdir = Direction.SOUTH;
                            break;
                        case 4:
                            offsetdir = Direction.WEST;
                            break;
                        case 5:
                            offsetdir = Direction.UP;
                            break;
                        case 6:
                            offsetdir = Direction.DOWN;
                            break;
                    }

                    if (this.world.getBlockState(this.getPos().offset(offsetdir)).getBlock().equals(FluxTechBlocks.STORAGE_NODE_BLOCK)) {
                        node = (StorageNodeBlockEntity) this.world.getBlockEntity(new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()).offset(offsetdir));
                        if (node.savedCore != null) {
                            core = node.savedCore;
                            assert core != null;
                            savedCore = core;
                            core.addNewNodes(this);
                            System.out.println("node");
                        }
                        break;
                    }
                    if (this.world.getBlockState(this.getPos().offset(offsetdir)).getBlock().equals(FluxTechBlocks.STORAGE_CORE_BLOCK)) {
                        core = (StorageCoreBlockEntity) this.world.getBlockEntity(new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()).offset(offsetdir));
                        savedCore = core;
                        assert core != null;
                        core.addNewNodes(this);
                        System.out.println("core");
                        break;
                    }
                    if (i >= 6) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        // TODO
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        // TODO
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        // TODO
        return null;
    }

    @Override
    public ItemStack removeStack(int slot) {
        // TODO
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        // TODO
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        // TODO
        return false;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.item = ItemStack.fromTag(tag.getCompound("item"));
        this.item.setCount(tag.getInt("itemCount"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        CompoundTag itemTag = new CompoundTag();
        this.item.toTag(itemTag);
        tag.put("item", itemTag);
        tag.putInt("itemCount", item.getCount());
        return tag;
    }

    @Override
    public void clear() {
        // TODO
    }

    @Override
    public Text getName() {
        return new TranslatableText("container.locker");
    }
}
