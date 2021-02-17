package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin extends FlowableFluid {



    /*@Shadow
    protected abstract void playExtinguishEvent(WorldAccess world, BlockPos pos);
    @Inject(method = "flow", at = @At("RETURN"), cancellable = true)
    public void flow(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo ci) {
        if (direction == Direction.DOWN) {
            FluidState fluidState2 = world.getFluidState(pos);
            if (this.isIn(FluidTags.LAVA) && fluidState2.isIn(FluxTechBlocks.ENDURIUM_TAG)) {
                if (state.getBlock() instanceof FluidBlock) {
                    world.setBlockState(pos, FluxTechBlocks.SMOOTH_END_STONE.getDefaultState(), 3);
                }
                this.playExtinguishEvent(world, pos);
                ci.cancel();
            }
        }

    }*/

}
