package com.fusionflux.fluxtech.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;


public class EnduriumBlock extends FluidBlock {

    public static final IntProperty LEVEL;
    public static final VoxelShape COLLISION_SHAPE;
    public EnduriumBlock(FlowableFluid baseFluid) {
        super(baseFluid, FabricBlockSettings.of(Material.WATER).noCollision().strength(100.0F, 100.0F).dropsNothing().velocityMultiplier(0.95F));
    }



    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.receiveNeighborFluids(world, pos, state)) {
            world.getFluidTickScheduler().schedule(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
        }
    }

    protected boolean FirstUpdate;





    private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
        boolean flag = false;

        for (Direction direction : Direction.values()) {
            if (direction != Direction.DOWN && world.getFluidState(pos.offset(direction)).isIn(FluidTags.LAVA)) {
                flag = true;
                break;
            }

        }
       if (flag) {
            FluidState ifluidstate = world.getFluidState(pos);
            //if (ifluidstate.isStill()) {
                world.setBlockState(pos, FluxTechBlocks.SMOOTH_END_STONE.getDefaultState());
                this.triggerMixEffects(world, pos);
                return false;
           // }

            /*if (ifluidstate.getHeight(world, pos) >= 0.44444445F) {
                world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
                this.triggerMixEffects(world, pos);
                return false;
            }*/

        }


        return true;
    }
   static {
        LEVEL = Properties.LEVEL_15;
        COLLISION_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    }

    @Deprecated
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {
        /*if (entity instanceof LivingEntity) {
            LivingEntity user = (LivingEntity) entity;
            if (!world.isClient) {
                double d = user.getX();
                double e = user.getY();
                double f = user.getZ();

                for(int i = 0; i < 16; ++i) {
                    double g = user.getX() + (user.getRandom().nextDouble() - 0.5D) * 32.0D;
                    double h = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(32) - 16), 0.0D, (double)(world.getDimensionHeight() - 1));
                    double j = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * 32.0D;
                    if (user.hasVehicle()) {
                        user.stopRiding();
                    }

                    if (user.teleport(g, h, j, true)) {
                        SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ENTITY_ENDERMAN_TELEPORT;
                        world.playSound((PlayerEntity)null, g, h, j, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        user.playSound(soundEvent, 1.0F, 1.0F);
                        break;
                    }
                }

            }

        }*/

        super.onEntityCollision(state, world, position, entity);
    }

    private void triggerMixEffects(World world, BlockPos pos) {
        world.syncWorldEvent(1501, pos, 0);
    }

   /* @Deprecated
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {

        if (entity instanceof LivingEntity) {
            LivingEntity playerEntity = ((LivingEntity) entity);
            double g = playerEntity.getX() + (playerEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
            double h = MathHelper.clamp(playerEntity.getY() + (double)(playerEntity.getRandom().nextInt(16) - 8), 0.0D, (double)(world.getDimensionHeight() - 1));
            double j = playerEntity.getZ() + (playerEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
            playerEntity.teleport(g,h,j);
        }

        super.onEntityCollision(state, world, position, entity);
    }

    private void triggerMixEffects(World world, BlockPos pos) {
        world.syncWorldEvent(1501, pos, 0);
    }*/
}

