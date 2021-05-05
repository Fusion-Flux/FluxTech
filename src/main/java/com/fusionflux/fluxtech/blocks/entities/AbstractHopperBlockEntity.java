package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.accessor.HopperInput;
import com.fusionflux.fluxtech.blocks.AbstractHopperBlock;
import com.fusionflux.fluxtech.blocks.HopperBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AbstractHopperBlockEntity  extends LootableContainerBlockEntity implements Hopper, HopperInput, Tickable {
    private DefaultedList<ItemStack> inventory;
    protected int transferCooldown;
    private int distance;
    private long lastTickTime;

    public AbstractHopperBlockEntity(BlockEntityType<?> type) {
        this(type, 1);
    }

    public AbstractHopperBlockEntity(BlockEntityType<?> type, int distance) {
        this(type, 5, distance);
    }

    public AbstractHopperBlockEntity(BlockEntityType<?> type, int invSize, int distance) {
        super(type);
        this.inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);
        this.transferCooldown = -1;
        this.distance = distance;
    }

    protected Direction getDirection() {
        return this.getCachedState().get(HopperBlock.FACING);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(tag)) {
            Inventories.fromTag(tag, this.inventory);
        }

        this.transferCooldown = tag.getInt("TransferCooldown");
        this.distance = tag.getInt("Distance");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (!this.serializeLootTable(tag)) {
            Inventories.toTag(tag, this.inventory);
        }

        tag.putInt("TransferCooldown", this.transferCooldown);
        tag.putInt("Distance", this.distance);
        return tag;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        this.checkLootInteraction(null);
        return Inventories.splitStack(this.getInvStackList(), slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.checkLootInteraction(null);
        this.getInvStackList().set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.hoppernt");
    }

    @Override
    public void tick() {
        if (this.world != null && !this.world.isClient) {
            --this.transferCooldown;
            this.lastTickTime = this.world.getTime();
            if (this.onCooldown()) {
                this.setCooldown(0);
                this.insertAndExtract(() -> extract(this));
            }

        }
    }

    private boolean insertAndExtract(Supplier<Boolean> extractMethod) {
        if (this.world != null && !this.world.isClient) {
            if (this.onCooldown() && this.getCachedState().get(AbstractHopperBlock.ENABLED)) {
                boolean bl = false;
                if (!this.isEmpty()) {
                    bl = this.insert();
                }

                if (!this.isFull()) {
                    bl |= extractMethod.get();
                }

                if (bl) {
                    this.setCooldown(8);
                    this.markDirty();
                    return true;
                }
            }

        }
        return false;
    }

    private boolean isFull() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while(!itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxCount());

        return false;
    }

    private boolean insert() {
        Inventory inventory = this.getOutputInventory();
        if (inventory != null) {
            Direction direction = this.getDirection().getOpposite();
            if (!this.isInventoryFull(inventory, direction)) {
                for (int i = 0; i < this.size(); ++i) {
                    if (!this.getStack(i).isEmpty()) {
                        ItemStack itemStack = this.getStack(i).copy();
                        ItemStack itemStack2 = transfer(this, inventory, this.removeStack(i, 1), direction);
                        if (itemStack2.isEmpty()) {
                            inventory.markDirty();
                            return true;
                        }

                        this.setStack(i, itemStack);
                    }
                }

            }
        }
        return false;
    }

    private static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        return inventory instanceof SidedInventory ?
                IntStream.of(((SidedInventory)inventory).getAvailableSlots(side)) :
                IntStream.range(0, inventory.size());
    }

    private boolean isInventoryFull(Inventory inv, Direction direction) {
        return getAvailableSlots(inv, direction).allMatch((i) -> {
            ItemStack itemStack = inv.getStack(i);
            return itemStack.getCount() >= itemStack.getMaxCount();
        });
    }

    private static boolean isInventoryEmpty(Inventory inv, Direction facing) {
        return getAvailableSlots(inv, facing).allMatch((i) -> inv.getStack(i).isEmpty());
    }

    public static boolean extract(Hopper hopper) {
        Inventory inventory = getInputInventory(hopper);
        if (inventory != null) {
            Direction direction = Direction.DOWN;
            return !isInventoryEmpty(inventory, direction) && getAvailableSlots(inventory, direction).anyMatch(
                    (i) -> extract(hopper, inventory, i, direction)
            );
        } else {
            Iterator<ItemEntity> var2 = getInputItemEntities(hopper).iterator();

            ItemEntity itemEntity;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                itemEntity = var2.next();
            } while(!extract(hopper, itemEntity));

            return true;
        }
    }

    private static boolean extract(Hopper hopper, Inventory inventory, int slot, Direction side) {
        ItemStack itemStack = inventory.getStack(slot);
        if (!itemStack.isEmpty() && canExtract(inventory, itemStack, slot, side)) {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = transfer(inventory, hopper, inventory.removeStack(slot, 1), null);
            if (itemStack3.isEmpty()) {
                inventory.markDirty();
                return true;
            }

            inventory.setStack(slot, itemStack2);
        }

        return false;
    }

    public static boolean extract(Inventory inventory, ItemEntity itemEntity) {
        boolean bl = false;
        ItemStack itemStack = itemEntity.getStack().copy();
        ItemStack itemStack2 = transfer(null, inventory, itemStack, null);
        if (itemStack2.isEmpty()) {
            bl = true;
            itemEntity.remove();
        } else {
            itemEntity.setStack(itemStack2);
        }

        return bl;
    }

    public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction side) {
        if (to instanceof SidedInventory && side != null) {
            SidedInventory sidedInventory = (SidedInventory)to;
            int[] availableSlots = sidedInventory.getAvailableSlots(side);

            for(int i = 0; i < availableSlots.length && !stack.isEmpty(); ++i) {
                stack = transfer(from, to, stack, availableSlots[i], side);
            }
        } else {
            int j = to.size();

            for(int k = 0; k < j && !stack.isEmpty(); ++k) {
                stack = transfer(from, to, stack, k, side);
            }
        }

        return stack;
    }

    private static boolean canInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
        if (!inventory.isValid(slot, stack)) {
            return false;
        } else {
            return !(inventory instanceof SidedInventory) || ((SidedInventory) inventory).canInsert(slot, stack, side);
        }
    }

    private static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
        return !(inv instanceof SidedInventory) || ((SidedInventory)inv).canExtract(slot, stack, facing);
    }

    private static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, int slot, @Nullable Direction direction) {
        ItemStack itemStack = to.getStack(slot);
        if (canInsert(to, stack, slot, direction)) {
            boolean bl = false;
            boolean bl2 = to.isEmpty();
            if (itemStack.isEmpty()) {
                to.setStack(slot, stack);
                stack = ItemStack.EMPTY;
                bl = true;
            } else if (canMergeItems(itemStack, stack)) {
                int i = stack.getMaxCount() - itemStack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.decrement(j);
                itemStack.increment(j);
                bl = j > 0;
            }

            if (bl) {
                if (bl2 && to instanceof AbstractHopperBlockEntity) {
                    AbstractHopperBlockEntity toHopper = (AbstractHopperBlockEntity)to;
                    if (!toHopper.isDisabled()) {
                        int k = 0;
                        if (from instanceof AbstractHopperBlockEntity) {
                            AbstractHopperBlockEntity fromHopper = (AbstractHopperBlockEntity)from;
                            if (toHopper.lastTickTime >= fromHopper.lastTickTime) {
                                k = 1;
                            }
                        }

                        toHopper.setCooldown(8 - k);
                    }
                }

                to.markDirty();
            }
        }

        return stack;
    }

    @Nullable
    private Inventory getOutputInventory() {
        Direction direction = this.getDirection();
        return getInventoryAt(this.getWorld(), this.pos.offset(direction, this.distance));
    }

    @Nullable
    public static Inventory getInputInventory(Hopper hopper) {
        return getInventoryAt(hopper.getWorld(), hopper.getHopperX(), hopper.getHopperY() + ((HopperInput) hopper).getInputInventoryY(), hopper.getHopperZ());
    }

    public static List<ItemEntity> getInputItemEntities(Hopper hopper) {
        return hopper.getInputAreaShape().getBoundingBoxes().stream().flatMap(
                (box) -> hopper
                        .getWorld()
                        .getEntitiesByClass(ItemEntity.class,
                                box.offset(hopper.getHopperX() - 0.5D,
                                        hopper.getHopperY() - 0.5D,
                                        hopper.getHopperZ() - 0.5D),
                                EntityPredicates.VALID_ENTITY).stream()).collect(Collectors.toList());
    }

    @Nullable
    public static Inventory getInventoryAt(World world, BlockPos blockPos) {
        return getInventoryAt(world,
                (double)blockPos.getX() + 0.5D,
                (double)blockPos.getY() + 0.5D,
                (double)blockPos.getZ() + 0.5D);
    }

    @Nullable
    public static Inventory getInventoryAt(World world, double x, double y, double z) {
        Inventory inventory = null;
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider)block).getInventory(blockState, world, blockPos);
        } else if (block.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof Inventory) {
                inventory = (Inventory)blockEntity;
                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
                }
            }
        }

        if (inventory == null) {
            List<Entity> list = world.getOtherEntities(null,
                    new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
                    EntityPredicates.VALID_INVENTORIES);
            if (!list.isEmpty()) {
                inventory = (Inventory)list.get(world.random.nextInt(list.size()));
            }
        }

        return inventory;
    }

    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        if (first.getItem() != second.getItem()) {
            return false;
        } else if (first.getDamage() != second.getDamage()) {
            return false;
        } else if (first.getCount() > first.getMaxCount()) {
            return false;
        } else {
            return ItemStack.areTagsEqual(first, second);
        }
    }

    @Override
    public double getHopperX() {
        return (double)this.pos.getX() + 0.5D;
    }

    @Override
    public double getHopperY() {
        return (double)this.pos.getY() + 0.5D;
    }

    @Override
    public double getHopperZ() {
        return (double)this.pos.getZ() + 0.5D;
    }

    private void setCooldown(int cooldown) {
        this.transferCooldown = cooldown;
    }

    private boolean onCooldown() {
        return this.transferCooldown <= 0;
    }

    private boolean isDisabled() {
        return this.transferCooldown > 8;
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    public void onEntityCollided(Entity entity) {
        if (entity instanceof ItemEntity) {
            BlockPos blockPos = this.getPos();
            if (VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset(-blockPos.getX(),
                                                                                              -blockPos.getY(),
                                                                                              -blockPos.getZ())),
                    this.getInputAreaShape(),
                    BooleanBiFunction.AND)) {
                this.insertAndExtract(() -> extract(this, (ItemEntity)entity));
            }
        }

    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new HopperScreenHandler(syncId, playerInventory, this);
    }
}
