package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.items.FluxTechItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);
    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);
    @Shadow
    @Final
    public PlayerAbilities abilities;

    @Shadow public abstract void increaseTravelMotionStats(double dx, double dy, double dz);


    @Shadow protected abstract boolean clipAtLedge();

    @Shadow protected abstract boolean method_30263();

    @Unique
    private boolean fluxtech$doGroundPound;
    @Unique
    private double fluxtech$fallSpeedMax;
    @Unique
    private boolean fluxtech$doCrunch;

    public boolean fallPls = false;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError("FluxTech: Called constructor for PlayerEntityMixin!");
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack feetStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (damageSource == DamageSource.FALL
                && (feetStack.getItem() == FluxTechItems.GRAVITRONS
                    || feetStack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)) {
            cir.setReturnValue(true);
        }
    }
/*
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d movementInput, CallbackInfo ci) {
        ItemStack legStack = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feetStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && legStack.getItem() == FluxTechItems.AEROARMOR) {
            this.flyingSpeed = this.abilities.getFlySpeed() * (float) FluxTechConfig.get().numbers.aeroarmorFlightBoost;
        }

        if (!this.isOnGround() && this.getVelocity().y < -1 && feetStack.getItem() == FluxTechItems.SLIDERS) {
            super.travel(movementInput);
        }

        if (!this.isOnGround() && this.getVelocity().y < -2.5 && feetStack.getItem() == FluxTechItems.SLIDERS) {
            super.travel(movementInput);
            super.travel(movementInput);
        }
        
        if (feetStack.getItem() == FluxTechItems.SLIDERS) {
            this.onGround = false;
            this.updateVelocity(this.getSlipSpeed(1.1f), movementInput);

        }

    }
*/

@Override
    public Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type) {
        if (!this.abilities.flying && (type == MovementType.SELF || type == MovementType.PLAYER) && this.clipAtLedge() && this.method_30263()&& fallPls) {
            double d = movement.x;
            double e = movement.z;
            double var7 = 0.05D;

            while(true) {
                while(d != 0.0D && this.world.isSpaceEmpty(this, this.getBoundingBox().offset(d, (double)(-this.stepHeight), 0.0D))) {
                    if (d < 0.05D && d >= -0.05D) {
                        d = 0.0D;
                    } else if (d > 0.0D) {
                        d -= 0.05D;
                    } else {
                        d += 0.05D;
                    }
                }

                while(true) {
                    while(e != 0.0D && this.world.isSpaceEmpty(this, this.getBoundingBox().offset(0.0D, (double)(-this.stepHeight), e))) {
                        if (e < 0.05D && e >= -0.05D) {
                            e = 0.0D;
                        } else if (e > 0.0D) {
                            e -= 0.05D;
                        } else {
                            e += 0.05D;
                        }
                    }

                    while(true) {
                        while(d != 0.0D && e != 0.0D && this.world.isSpaceEmpty(this, this.getBoundingBox().offset(d, (double)(-this.stepHeight), e))) {
                            if (d < 0.05D && d >= -0.05D) {
                                d = 0.0D;
                            } else if (d > 0.0D) {
                                d -= 0.05D;
                            } else {
                                d += 0.05D;
                            }

                            if (e < 0.05D && e >= -0.05D) {
                                e = 0.0D;
                            } else if (e > 0.0D) {
                                e -= 0.05D;
                            } else {
                                e += 0.05D;
                            }
                        }

                        movement = new Vec3d(d, movement.y, e);
                        return movement;
                    }
                }
            }
        } else {
            return movement;
        }
    }


    /**
     * @author
     */
    @Overwrite
    public void travel(Vec3d movementInput) {
        if (!this.isOnGround() && !this.abilities.flying && !this.isFallFlying()) {

            double mathval = 1;
            double horizontalvelocity = Math.abs(this.getVelocity().x)+Math.abs(this.getVelocity().z);
            if(horizontalvelocity/0.01783440120041885 > 1){
                mathval= horizontalvelocity/0.01783440120041885;
            }

            movementInput = new Vec3d(movementInput.x/mathval,movementInput.y,movementInput.z/mathval);
            //movementInput=movementInput.multiply(1, 1, 0);
           /* float airSpeed = 0;
            float scalevalue;

            if(horizontalvelocity<1){
               scalevalue = 1.5f;
            }else{
                scalevalue =(float)horizontalvelocity*4;
            }

            if(this.isSneaking() && horizontalvelocity > .5){
                airSpeed =(.01f*(10f/3f))*scalevalue;

            }else{
                airSpeed =.01f*scalevalue;
            }
            this.airStrafingSpeed = airSpeed;*/
        }
       /* double horizontalvelocity = Math.abs(this.getVelocity().x)+Math.abs(this.getVelocity().z);
        if(this.isSneaking() && horizontalvelocity > 1){
            fallPls=false;
            this.stepHeight = 1.6f;
            this.horizontalCollision = false;
        }else{
            fallPls=true;
            this.stepHeight = .6f;
        }*/



        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        double g;
        if (this.isSwimming() && !this.hasVehicle()) {
            g = this.getRotationVector().y;
            double h = g < -0.2D ? 0.085D : 0.06D;
            if (g <= 0.0D || this.jumping || !this.world.getBlockState(new BlockPos(this.getX(), this.getY() + 1.0D - 0.1D, this.getZ())).getFluidState().isEmpty()) {
                Vec3d vec3d = this.getVelocity();
                this.setVelocity(vec3d.add(0.0D, (g - vec3d.y) * h, 0.0D));
            }
        }

        if (this.abilities.flying && !this.hasVehicle()) {
            g = this.getVelocity().y;
            float h = this.airStrafingSpeed;
            this.airStrafingSpeed = this.abilities.getFlySpeed() * (float)(this.isSprinting() ? 2 : 1);
            super.travel(movementInput);
            Vec3d vec3d2 = this.getVelocity();
            this.setVelocity(vec3d2.x, g * 0.6D, vec3d2.z);
            this.airStrafingSpeed = h;
            this.onLanding();
            this.setFlag(7, false);
        } else {
            super.travel(movementInput);
        }

        this.increaseTravelMotionStats(this.getX() - d, this.getY() - e, this.getZ() - f);
    }

    private float getSlipSpeed(float slipperiness) {
        return this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness));
    }

    /*@Inject(method="getFallSounds", at = @At("HEAD"), cancellable = true)
    protected void getFallSounds(CallbackInfoReturnable<FallSounds> cir){
        ItemStack feetStack = this.getEquippedStack(EquipmentSlot.FEET);
        if(feetStack.getItem() == FluxTechItems.GRAVITRONS){
            cir.setReturnValue(SoundEvents.BLOCK_NETHERITE_BLOCK_FALL);
        }

        if(feetStack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS){
            cir.setReturnValue(SoundEvents.BLOCK_SLIME_BLOCK_PLACE);
        }
    }*/

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {

        double horizontalvelocity2 = Math.abs(this.getVelocity().x)+Math.abs(this.getVelocity().z);
        //this.setNoDrag(!this.isOnGround() && !this.abilities.flying && !this.isFallFlying());
        this.setNoDrag((!this.isOnGround() && !this.abilities.flying && !this.isFallFlying()) || (this.isSneaking() && horizontalvelocity2 > 1.5));



        ItemStack feetStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && (feetStack.getItem() == FluxTechItems.SLIDERS) || feetStack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) {
            if (this.getVelocity().y < fluxtech$fallSpeedMax && this.getVelocity().y < 0) {
                fluxtech$fallSpeedMax = Math.abs(this.getVelocity().y);
            }

            if (this.getVelocity().y < fluxtech$fallSpeedMax && this.getVelocity().y < 0) {
                fluxtech$fallSpeedMax = Math.abs(this.getVelocity().y);
            }

            if (this.getVelocity().y < -1) {
                fluxtech$doGroundPound = true;
            }
        }
        Vec3d vec3d = this.getVelocity();
        if (!this.isTouchingWater() && !this.isSneaking() && fluxtech$fallSpeedMax > .3 && this.isOnGround() && feetStack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) {
            if (vec3d.y < 0.0D) {
                // double d = this instanceof LivingEntity ? 1.0D : 1.8D;
                this.setVelocity(vec3d.x, fluxtech$fallSpeedMax * 0.8D, vec3d.z);
                this.playSound(SoundEvents.BLOCK_SLIME_BLOCK_PLACE, 1, 1);
                if (this.isOnGround() || this.isFallFlying() || this.isSneaking()) {
                    // groundpound = false;
                    fluxtech$fallSpeedMax = 0;
                }
            }
        } else {
            if ((this.isSneaking() || this.isTouchingWater()) && feetStack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) {
                fluxtech$fallSpeedMax = 0;
            }
        }

        if (fluxtech$doGroundPound && feetStack.getItem() == FluxTechItems.SLIDERS) {
            List<LivingEntity> stompableEntities = this.world.getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), e -> true);
            stompableEntities.remove(this);
            for (LivingEntity entity : stompableEntities) {
                fluxtech$doCrunch = true;
                if (!this.world.isClient) {
                    entity.damage(DamageSource.GENERIC, FluxTechConfig.get().numbers.gravitronCrushDamage);
                    world.playSound(null,this.getPos().getX(),this.getPos().getY(),this.getPos().getZ(),SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.NEUTRAL, 1, 1);
                    world.playSound(null,this.getPos().getX(),this.getPos().getY(),this.getPos().getZ(),SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.NEUTRAL, 2, 1);
                    break;
                }
            }
            if (fluxtech$doCrunch) {
                this.setVelocity(this.getVelocity().x, fluxtech$fallSpeedMax / FluxTechConfig.get().numbers.crushBounceMultiplier, this.getVelocity().z);
                fluxtech$doGroundPound = false;
                fluxtech$doCrunch = false;
                fluxtech$fallSpeedMax = 0;
            }
            if ((this.isOnGround() || this.isFallFlying()) && !fluxtech$doCrunch) {
                fluxtech$doGroundPound = false;
                fluxtech$fallSpeedMax = 0;
            }
        }
    }
}





