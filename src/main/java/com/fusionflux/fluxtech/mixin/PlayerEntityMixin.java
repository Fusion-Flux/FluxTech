package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.items.FluxTechItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
                && (feetStack.getItem() == FluxTechItems.GRAVITRONS
                    || feetStack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)) {
            cir.setReturnValue(true);
        }
    }

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

    private float getSlipSpeed(float slipperiness) {
        return this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness));
    }

    @Inject(method="getFallSound", at = @At("HEAD"), cancellable = true)
    protected void getFallSound(int distance, CallbackInfoReturnable<SoundEvent> cir){
        ItemStack feetStack = this.getEquippedStack(EquipmentSlot.FEET);
        if(feetStack.getItem() == FluxTechItems.GRAVITRONS){
            cir.setReturnValue(SoundEvents.BLOCK_NETHERITE_BLOCK_FALL);
        }

        if(feetStack.getItem() == FluxTechItems.SLIME_COATED_NETHERITE_BOOTS){
            cir.setReturnValue(SoundEvents.BLOCK_SLIME_BLOCK_PLACE);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
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





