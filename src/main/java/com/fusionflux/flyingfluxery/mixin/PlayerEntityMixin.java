package com.fusionflux.flyingfluxery.mixin;

import com.fusionflux.flyingfluxery.accessor.LaunchAccessors;
import com.fusionflux.flyingfluxery.config.FluxTechConfig;
import com.fusionflux.flyingfluxery.items.FluxTechItems;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements LaunchAccessors {
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);
    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);
    @Shadow
    @Final
    public PlayerAbilities abilities;

    @Unique
    private boolean fluxtech$doGroundPound;
    @Unique
    private double fluxtech$fallSpeedMax;
    @Unique
    private boolean fluxtech$doCrunch;


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError("FluxTech: Called constructor for PlayerEntityMixin!");
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack feetStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (damageSource == DamageSource.FALL
                && (feetStack.getItem() == FluxTechItems.GRAVITRONS)) {
            cir.setReturnValue(true);
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3d uhhhhhhh(Vec3d travelVectorOriginal) {

        if (!this.hasNoGravity()) {
            ItemStack itemFeet = this.getEquippedStack(EquipmentSlot.FEET);
            if (!this.isOnGround() && !this.abilities.flying && !this.isFallFlying() && itemFeet.getItem().equals(FluxTechItems.GRAVITRONS)) {
                Vec3d maxSpeed = ((LaunchAccessors) this).getLaunchVelocity();
                if (maxSpeed == null) {
                    maxSpeed = Vec3d.ZERO;
                }
                if (maxSpeed != Vec3d.ZERO) {
                    //the maximum velocity a player can have while in midair

                    maxSpeed = new Vec3d(maxSpeed.x, 0, maxSpeed.z);

                    //the speed generated from the player input
                    Vec3d addVelocity = calcMovementInputToVelocity(travelVectorOriginal, .1f, this.getYaw());

                    //the input speed added to the players current velocity
                    Vec3d wishVelocity = this.getVelocity().add(addVelocity);

                    //the current players velocity
                    Vec3d currentVelocity = this.getVelocity().multiply(new Vec3d(1, 0, 1));

                    wishVelocity = currentVelocity.add(addVelocity);
                    // System.out.println("inital creation" + wishVelocity.length());


                    if (wishVelocity.length() < maxSpeed.length()) {
                        wishVelocity = wishVelocity.normalize().multiply(maxSpeed);
                        //System.out.println("before subtract" + wishVelocity.length());

                        wishVelocity.subtract(currentVelocity);

                        //System.out.println("after subtract" + wishVelocity.length());
                        this.airStrafingSpeed = .1f * (float) wishVelocity.length();
                    } else {
                        double mathval = 1;
                        double horizontalvelocity = Math.abs(this.getVelocity().x) + Math.abs(this.getVelocity().z);
                        if (horizontalvelocity / 0.01783440120041885 > 1) {
                            mathval = horizontalvelocity / 0.01783440120041885;
                        }

                        travelVectorOriginal = new Vec3d(travelVectorOriginal.x / mathval, travelVectorOriginal.y, travelVectorOriginal.z / mathval);
                    }


                } else {
                    double mathval = 1;
                    double horizontalvelocity = Math.abs(this.getVelocity().x) + Math.abs(this.getVelocity().z);
                    if (horizontalvelocity / 0.01783440120041885 > 1) {
                        mathval = horizontalvelocity / 0.01783440120041885;
                    }

                    travelVectorOriginal = new Vec3d(travelVectorOriginal.x / mathval, travelVectorOriginal.y, travelVectorOriginal.z / mathval);
                }

            }
        }
        return travelVectorOriginal;
    }


    private static Vec3d calcMovementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        } else {
            Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply((double)speed);
            float f = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
            float g = MathHelper.cos(yaw * (float) (Math.PI / 180.0));
            return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
        }
    }
private boolean enableNoDrag = false;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {

        ItemStack feetStack = this.getEquippedStack(EquipmentSlot.FEET);
        if((!this.isOnGround() && !this.abilities.flying && !this.isFallFlying() && feetStack.getItem().equals(FluxTechItems.GRAVITRONS))){
            if(!enableNoDrag) {
                enableNoDrag = true;
            }
                this.setNoDrag(true);
        }else if (enableNoDrag){
            enableNoDrag = false;
            this.setNoDrag(false);
        }
        //((LaunchAccessors) this).getLaunchVelocity();

        //if(initalflyspeed == Vec3d.ZERO && !this.isOnGround() && !this.abilities.flying && !this.isFallFlying() && feetStack.getItem().equals(FluxTechItems.GRAVITRONS)){
          //  initalflyspeed = ((LaunchAccessors) this).getLaunchVelocity();
        //}

        if(this.isOnGround() || this.abilities.flying || this.isFallFlying() || !feetStack.getItem().equals(FluxTechItems.GRAVITRONS)){
           // initalflyspeed = Vec3d.ZERO;
            ((LaunchAccessors) this).setLaunchVelocity(Vec3d.ZERO);
        }

        if(this.isOnGround() || this.abilities.flying || this.isFallFlying()){
            ((LaunchAccessors) this).setBlastJumping(false);
        }



        if (!this.isOnGround() && (feetStack.getItem() == FluxTechItems.GRAVITRONS)) {
            if (this.getVelocity().y < fluxtech$fallSpeedMax && this.getVelocity().y < 0) {
                fluxtech$fallSpeedMax = Math.abs(this.getVelocity().y);
            }

            if (this.getVelocity().y < fluxtech$fallSpeedMax && this.getVelocity().y < 0) {
                fluxtech$fallSpeedMax = Math.abs(this.getVelocity().y);
            }

            if (this.getVelocity().y < -.4) {
                fluxtech$doGroundPound = true;
            }
        }


        if (fluxtech$doGroundPound && feetStack.getItem() == FluxTechItems.GRAVITRONS && FluxTechConfig.get().numbers.doCrunch) {
            List<LivingEntity> stompableEntities = this.world.getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), e -> true);
            stompableEntities.remove(this);
            for (LivingEntity entity : stompableEntities) {
                fluxtech$doCrunch = true;
                if (!this.world.isClient) {
                    ((LaunchAccessors) this).setBlastJumping(false);
                    entity.damage(DamageSource.GENERIC, (float)fluxtech$fallSpeedMax*5);
                    world.playSound(null,this.getPos().getX(),this.getPos().getY(),this.getPos().getZ(),SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.NEUTRAL, 1, 1);
                    world.playSound(null,this.getPos().getX(),this.getPos().getY(),this.getPos().getZ(),SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.NEUTRAL, 2, 1);
                    break;
                }
            }
            if (fluxtech$doCrunch) {
                this.setVelocity(this.getVelocity().x, 0, this.getVelocity().z);
                fluxtech$doCrunch = false;
                fluxtech$doGroundPound = false;
            }
            /*if (fluxtech$doCrunch) {
                this.setVelocity(this.getVelocity().x, fluxtech$fallSpeedMax / FluxTechConfig.get().numbers.crushBounceMultiplier, this.getVelocity().z);
                fluxtech$doGroundPound = false;
                fluxtech$doCrunch = false;
                fluxtech$fallSpeedMax = 0;
            }*/
            if ((this.isOnGround() || this.isFallFlying()) && !fluxtech$doCrunch) {
                fluxtech$doGroundPound = false;
                fluxtech$fallSpeedMax = 0;
            }
        }

    }

    Vec3d storedLaunchVelocity = Vec3d.ZERO;

    @Override
    public void setLaunchVelocity(Vec3d launchVelocity){
        storedLaunchVelocity = launchVelocity;
    }

    @Override
    public Vec3d getLaunchVelocity(){
        return storedLaunchVelocity;
    }

    boolean isBlastJumping = false;

    @Override
    public void setBlastJumping(boolean isBlastJumping){
        this.isBlastJumping = isBlastJumping;
    }

    @Override
    public boolean getBlastJumping(){
        return isBlastJumping;
    }


}





