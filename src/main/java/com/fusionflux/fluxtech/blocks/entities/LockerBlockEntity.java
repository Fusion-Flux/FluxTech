package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;

public class LockerBlockEntity extends BlockEntity implements Inventory, Nameable {
    private ItemStack item = ItemStack.EMPTY;

    public LockerBlockEntity() {
        super(FluxTechBlocks.LOCKER_BLOCK_ENTITY);
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
