package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.accessor.EnduriumToucher;
import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.entity.EntityAttachments;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAttachments, EnduriumToucher {

    private static final TrackedData<Boolean> IS_ROLLING = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Direction> DIRECTION = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.FACING);

    @Shadow
    @Final
    protected DataTracker dataTracker;

    public double maxFallSpeed = 0;

    @Override
    public double getMaxFallSpeed() {
        return maxFallSpeed;
    }

    @Override
    public void setMaxFallSpeed(double maxFallSpeed) {
        this.maxFallSpeed = maxFallSpeed;
    }

    @Shadow
    private float standingEyeHeight;

    @Shadow
    protected abstract float getEyeHeight(EntityPose pose, EntityDimensions dimensions);

    @Shadow
    public abstract EntityPose getPose();

    @Shadow
    public abstract EntityDimensions getDimensions(EntityPose pose);

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public abstract boolean isOnGround();

    @Shadow public World world;

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow public abstract Vec3d getPos();

    private boolean touchingEndurium;

    @Shadow public abstract boolean updateMovementInFluid(Tag<Fluid> tag, double d);

    @Shadow public @Nullable
    abstract Entity getVehicle();

    @Shadow protected abstract void onSwimmingStart();

    @Shadow public float fallDistance;

    @Shadow public abstract void extinguish();

    @Shadow protected boolean firstUpdate;

    @Shadow protected Object2DoubleMap<Tag<Fluid>> fluidHeight;

    @Override
    public boolean isRolling() {
        return dataTracker.get(IS_ROLLING);
    }

    @Override
    public void setRolling(boolean rolling) {
        dataTracker.set(IS_ROLLING, rolling);
    }

    @Override
    public Direction getDirection() {
        return dataTracker.get(DIRECTION);
    }

    @Override
    public void setDirection(Direction direction) {
        dataTracker.set(DIRECTION, direction);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;initDataTracker()V"))
    public void Entity(EntityType<?> type, World world, CallbackInfo callbackInfo) {
        this.dataTracker.startTracking(IS_ROLLING, false);
        this.dataTracker.startTracking(DIRECTION, Direction.UP);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {

            if(((EnduriumToucher)this).getTouchingEndurium()){
                if (((Entity) (Object) this) instanceof LivingEntity) {
                    LivingEntity user = (LivingEntity) ((Entity) (Object) this);
                    if (!world.isClient) {
                        double d = user.getX();
                        double e = user.getY();
                        double f = user.getZ();

                        for(int i = 0; i < 16; ++i) {
                            double g = user.getX() + (user.getRandom().nextDouble() - 0.5D) * FluxTechConfig2.get().numbers.enduriumTpRange;
                            double h = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(FluxTechConfig2.get().numbers.enduriumTpRange) - 16), 0.0D, (double)(world.getDimensionHeight() - 1));
                            double j = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * FluxTechConfig2.get().numbers.enduriumTpRange;
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

                }
            }


            if(maxFallSpeed == 10 && world.getBlockState(this.getBlockPos()).getBlock() == FluxTechBlocks.PROPULSION_GEL){
                maxFallSpeed = 10;
            }else{
                if(maxFallSpeed>0){
                    maxFallSpeed=maxFallSpeed-1;
                }
            }

    }

    @Inject(method = "isTouchingWater()Z", at = @At("TAIL"))
    public void onisTouchingWater(CallbackInfoReturnable<Boolean> cir) { isTouchingEndurium(); }

    @Unique
    public boolean isTouchingEndurium(){
        return this.touchingEndurium;
    }

    public void setTouchingEndurium(boolean touchingEndurium) { this.touchingEndurium = touchingEndurium; }
    public boolean getTouchingEndurium() { return this.touchingEndurium; }

    @Inject(method = "isInLava()Z", at = @At("TAIL"))
    public void onisInLava(CallbackInfoReturnable<Boolean> cir) { isInEndurium(); }

    public boolean isInEndurium(){
        return !this.firstUpdate && this.fluidHeight.getDouble(FluxTechBlocks.ENDURIUM_TAG) > 0.0D;
    }

    @Inject(method="updateWaterState()Z", at=@At("TAIL"))
    public void onUpdateWaterState(CallbackInfoReturnable<Boolean> cir) {
        checkEnduriumState();
    }

    void checkEnduriumState() {
        if (this.getVehicle() instanceof BoatEntity) {
            this.touchingEndurium = false;
        } else if (this.updateMovementInFluid(FluxTechBlocks.ENDURIUM_TAG, -0.014D)) {
            if (!this.touchingEndurium && !this.firstUpdate) {
                this.onSwimmingStart();
            }

            this.fallDistance = 0.0F;
            this.touchingEndurium = true;
            this.extinguish();
        } else {
            this.touchingEndurium = false;
        }
    }




    /*@Inject(method = "calculateDimensions", at = @At("TAIL"))
public void calculateDimensions(CallbackInfo ci){
        EntityPose entityPose2 = this.getPose();
        EntityDimensions entityDimensions3 = this.getDimensions(entityPose2);
        this.standingEyeHeight = this.getEyeHeight(entityPose2, entityDimensions3)-1;
    }*/
}
