package com.fusionflux.fluxtech.fluids;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.items.FluxTechItems;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static net.minecraft.state.property.Properties.LEVEL_1_8;


public abstract class Endurium extends FlowableFluid {
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.NeighborGroup>> field_15901;

    static {
        field_15901 = ThreadLocal.withInitial(() -> {
            Object2ByteLinkedOpenHashMap<Block.NeighborGroup> object2ByteLinkedOpenHashMap = new Object2ByteLinkedOpenHashMap<Block.NeighborGroup>(200) {
                protected void rehash(int i) {
                }
            };
            object2ByteLinkedOpenHashMap.defaultReturnValue((byte) 127);
            return object2ByteLinkedOpenHashMap;
        });
    }

    @Override
    public Fluid getFlowing() {
        return FluxTechBlocks.ENDURIUM_FLOWING;
    }

    @Override
    public Fluid getStill() {
        return FluxTechBlocks.ENDURIUM;
    }

    @Override
    public Item getBucketItem() {
        return FluxTechItems.ENDURIUM_BUCKET;
    }

    @Override
    protected void tryFlow(WorldAccess world, BlockPos fluidPos, FluidState state) {
        if (!state.isEmpty()) {
            BlockState blockState = world.getBlockState(fluidPos);
            BlockPos blockPos = fluidPos.down();
            BlockState blockState2 = world.getBlockState(blockPos);
            FluidState fluidState = this.getUpdatedState(world, blockPos, blockState2);
            if (this.canFlow(world, fluidPos, blockState, Direction.DOWN, blockPos, blockState2, world.getFluidState(blockPos), fluidState.getFluid())) {
                this.flow(world, blockPos, blockState2, Direction.DOWN, fluidState);
                if (this.method_15740(world, fluidPos) >= 3) {
                    this.method_15744(world, fluidPos, state, blockState);
                }
            } else if (state.isStill() || !this.method_15736(world, fluidState.getFluid(), fluidPos, blockState, blockPos, blockState2)) {
                this.method_15744(world, fluidPos, state, blockState);
            }

        }
    }

    private boolean receivesFlow(Direction face, BlockView world, BlockPos pos, BlockState state, BlockPos fromPos, BlockState fromState) {
        Object2ByteLinkedOpenHashMap object2ByteLinkedOpenHashMap2;
        if (!state.getBlock().hasDynamicBounds() && !fromState.getBlock().hasDynamicBounds()) {
            object2ByteLinkedOpenHashMap2 = field_15901.get();
        } else {
            object2ByteLinkedOpenHashMap2 = null;
        }

        Block.NeighborGroup neighborGroup2;
        if (object2ByteLinkedOpenHashMap2 != null) {
            neighborGroup2 = new Block.NeighborGroup(state, fromState, face);
            byte b = object2ByteLinkedOpenHashMap2.getAndMoveToFirst(neighborGroup2);
            if (b != 127) {
                return b != 0;
            }
        } else {
            neighborGroup2 = null;
        }

        VoxelShape voxelShape = state.getCollisionShape(world, pos);
        VoxelShape voxelShape2 = fromState.getCollisionShape(world, fromPos);
        boolean bl = !VoxelShapes.adjacentSidesCoverSquare(voxelShape, voxelShape2, face);
        if (object2ByteLinkedOpenHashMap2 != null) {
            if (object2ByteLinkedOpenHashMap2.size() == 200) {
                object2ByteLinkedOpenHashMap2.removeLastByte();
            }

            object2ByteLinkedOpenHashMap2.putAndMoveToFirst(neighborGroup2, (byte) (bl ? 1 : 0));
        }

        return bl;
    }

    private boolean method_15736(BlockView world, Fluid fluid, BlockPos pos, BlockState state, BlockPos fromPos, BlockState fromState) {
        if (!this.receivesFlow(Direction.DOWN, world, pos, state, fromPos, fromState)) {
            return false;
        } else {
            return fromState.getFluidState().getFluid().matchesType(this) || this.canFill(world, fromPos, fromState, fluid);
        }
    }

    private boolean canFill(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        Block block = state.getBlock();
        if (block instanceof FluidFillable) {
            return ((FluidFillable) block).canFillWithFluid(world, pos, state, fluid);
        } else if (!(block instanceof DoorBlock) && !block.isIn(BlockTags.SIGNS) && block != Blocks.LADDER && block != Blocks.SUGAR_CANE && block != Blocks.BUBBLE_COLUMN) {
            Material material = state.getMaterial();
            if (material != Material.PORTAL && material != Material.STRUCTURE_VOID && material != Material.UNDERWATER_PLANT && material != Material.REPLACEABLE_UNDERWATER_PLANT) {
                return !material.blocksMovement();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isMatchingAndStill(FluidState state) {
        return state.getFluid().matchesType(this) && state.isStill();
    }

    private int method_15740(WorldView world, BlockPos pos) {
        int i = 0;
        Iterator var4 = Direction.Type.HORIZONTAL.iterator();

        while (var4.hasNext()) {
            Direction direction = (Direction) var4.next();
            BlockPos blockPos = pos.offset(direction);
            FluidState fluidState = world.getFluidState(blockPos);
            if (this.isMatchingAndStill(fluidState)) {
                ++i;
            }
        }

        return i;
    }

    private void method_15744(WorldAccess world, BlockPos pos, FluidState fluidState, BlockState blockState) {
        int i = fluidState.getLevel() - this.getLevelDecreasePerBlock(world);
        if (fluidState.get(FALLING)) {
            i = 7;
        }

        if (i > 0) {
            Map<Direction, FluidState> map = this.getSpread(world, pos, blockState);
            Iterator var7 = map.entrySet().iterator();

            while (var7.hasNext()) {
                Map.Entry<Direction, FluidState> entry = (Map.Entry) var7.next();
                Direction direction = entry.getKey();
                FluidState fluidState2 = entry.getValue();
                BlockPos blockPos = pos.offset(direction);
                BlockState blockState2 = world.getBlockState(blockPos);
                if (this.canFlow(world, pos, blockState, direction, blockPos, blockState2, world.getFluidState(blockPos), fluidState2.getFluid())) {
                    this.flow(world, blockPos, blockState2, direction, fluidState2);
                }
            }

        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                worldIn.playSound(
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D,
                        SoundEvents.BLOCK_WATER_AMBIENT,
                        SoundCategory.BLOCKS,
                        random.nextFloat() * 0.25F + 0.75F,
                        random.nextFloat() * 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            worldIn.addParticle(ParticleTypes.UNDERWATER,
                    (double) pos.getX() + (double) random.nextFloat(),
                    (double) pos.getY() + (double) random.nextFloat(),
                    (double) pos.getZ() + (double) random.nextFloat(),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public ParticleEffect getParticle() {
        return ParticleTypes.DRIPPING_OBSIDIAN_TEAR;
    }

    @Override
    protected boolean hasRandomTicks() {
        return true;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 7;
    }

    @Override
    protected float getBlastResistance() {
        return 100.0F;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    public int getFlowSpeed(WorldView world) {
        return 4;
    }

    @Override
    public int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid.isIn(FluxTechBlocks.ENDURIUM_TAG);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluxTechBlocks.ENDURIUM_TAG);
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return FluxTechBlocks.ENDURIUM_BLOCK.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
    }

    public static class Flowing extends Endurium {
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }

        @Override
        protected boolean isInfinite() {
            return true;
        }
    }

    public static class Source extends Endurium {

        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }

        @Override
        protected boolean isInfinite() {
            return false;
        }
    }
}

