package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin {
    @Shadow
    protected abstract void playExtinguishSound(WorldAccess world, BlockPos pos);
    @Shadow
    @Final
    protected FlowableFluid fluid;

    /**
     * @reason Gives Fluid Functionality
     */
    @Inject(method = "receiveNeighborFluids", at = @At("RETURN"), cancellable = true)
    public void receiveNeighborFluids(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (this.fluid.isIn(FluidTags.LAVA)) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN) {
                    BlockPos blockPos = pos.offset(direction);
                    if (world.getFluidState(blockPos).isIn(FluxTechBlocks.ENDURIUM_TAG)) {
                        Block block = world.getFluidState(pos).isStill() ? Blocks.OBSIDIAN : Blocks.END_STONE;
                        world.setBlockState(pos, block.getDefaultState());
                        this.playExtinguishSound(world, pos);
                        cir.setReturnValue(false);
                    }
                }
            }
        }
        if (this.fluid.isIn(FluidTags.WATER)) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN) {
                    BlockPos blockPos = pos.offset(direction);
                    if (world.getFluidState(blockPos).isIn(FluxTechBlocks.ENDURIUM_TAG)) {
                        Block block = world.getFluidState(pos).isStill() ? Blocks.PACKED_ICE : Blocks.ICE;
                        world.setBlockState(pos, block.getDefaultState());
                        // this.playExtinguishSound(world, pos);
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}
