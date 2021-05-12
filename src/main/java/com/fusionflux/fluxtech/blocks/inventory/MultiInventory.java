package com.fusionflux.fluxtech.blocks.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MultiInventory implements Inventory {
    private final List<Inventory> inventories;
    private final int size;

    public MultiInventory(Inventory... inventories) {
        this(Arrays.asList(inventories));
    }

    public MultiInventory(List<Inventory> inventories) {
        this.inventories = inventories;
        if (inventories.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("An inventory was null");
        }
        if (inventories.stream().anyMatch((i) -> i.size() != 27)) {
            throw new IllegalArgumentException("An inventory did not have a size of 27");
        }

        size = inventories.size() * 27;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Inventory inventory : inventories) {
            if (!inventory.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventories.get(slot / 27).getStack(slot % 27);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventories.get(slot / 27).removeStack(slot % 27, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventories.get(slot / 27).removeStack(slot % 27);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventories.get(slot / 27).setStack(slot % 27, stack);
    }

    @Override
    public void markDirty() {
        inventories.forEach(Inventory::markDirty);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        for (Inventory inventory : inventories) {
            if (!inventory.canPlayerUse(player)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        inventories.forEach((inv) -> inv.onOpen(player));
    }

    @Override
    public void onClose(PlayerEntity player) {
        inventories.forEach((inv) -> inv.onClose(player));
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return inventories.get(slot / 27).isValid(slot % 27, stack);
    }

    @Override
    public int count(Item item) {
        int total = 0;
        for (Inventory inventory : inventories) {
            total += inventory.count(item);
        }
        return total;
    }

    @Override
    public boolean containsAny(Set<Item> items) {
        for (Inventory inventory : inventories) {
            if (inventory.containsAny(items)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        inventories.forEach((inv) -> inv.clear());
    }
}

