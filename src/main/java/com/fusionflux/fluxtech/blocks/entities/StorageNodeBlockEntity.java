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
import org.lwjgl.system.CallbackI;

import java.util.Objects;

public class StorageNodeBlockEntity extends BlockEntity implements Inventory, Nameable {
    private ItemStack item = ItemStack.EMPTY;
    private StorageCoreBlockEntity savedCore;
    private BlockPos connectedCore;
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
                if(connectedCore==null) {
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
                                if (node.connectedCore != null) {
                                    this.connectedCore = node.connectedCore;
                                    core = (StorageCoreBlockEntity) this.world.getBlockEntity(node.connectedCore);
                                    core.addNewNodes(this.pos);
                                    System.out.println("node");
                                }
                                break;
                            }
                            if (this.world.getBlockState(this.getPos().offset(offsetdir)).getBlock().equals(FluxTechBlocks.STORAGE_CORE_BLOCK)) {
                                core = (StorageCoreBlockEntity) this.world.getBlockEntity(new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()).offset(offsetdir));
                                connectedCore = core.getPos();
                                core.addNewNodes(this.pos);
                                System.out.println("core");
                                break;
                            }
                            if (i >= 6) {
                                connectedCore=new BlockPos(0,-5,0);
                                break;
                            }
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
        connectedCore = (new BlockPos(
                tag.getInt("x"),
                tag.getInt("y"),
                tag.getInt("z")
        ));

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        CompoundTag itemTag = new CompoundTag();
        this.item.toTag(itemTag);
        tag.put("item", itemTag);
        tag.putInt("itemCount", item.getCount());

        tag.putInt("x", connectedCore.getX());
        tag.putInt("y", connectedCore.getY());
        tag.putInt("z", connectedCore.getZ());

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
