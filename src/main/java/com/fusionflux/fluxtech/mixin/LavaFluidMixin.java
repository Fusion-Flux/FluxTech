package com.fusionflux.fluxtech.mixin;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;

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
