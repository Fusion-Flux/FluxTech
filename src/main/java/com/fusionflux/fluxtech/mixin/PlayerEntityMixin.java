package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.accessor.PlayerEntityExtensions;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.effects.CustomEffects;
import com.fusionflux.fluxtech.items.FluxTechItems;
import com.fusionflux.fluxtech.util.FluxTechTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityExtensions {

    @Shadow
    @Final
    public PlayerAbilities abilities;
    @Unique
    private boolean fluxTech$groundPound;
    @Unique
    private double fluxTech$vertSpeedMax;
    @Unique
    private double fluxTech$topSpeedThisFlight;
    @Unique
    private EndCrystalEntity fluxTech$connectedCrystal;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError("FluxTech: Called constructor for PlayerEntityMixin!");
    }

    @Override
    public EndCrystalEntity fluxTech$getConnectedCrystal() {
        return fluxTech$connectedCrystal;
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void fluxTechNegateFallDamage(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (damageSource == DamageSource.FALL && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || (itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS))) ){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d movementInput, CallbackInfo ci) {
        ItemStack legsArmorStack = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feetArmorStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && (legsArmorStack.getItem().equals(FluxTechItems.AEROARMOR))) {
            this.flyingSpeed = this.abilities.getFlySpeed() * (float) (this.isSprinting() ? FluxTechConfig2.get().numbers.aeroarmorFlightBoost : 1);
        }
        if (!this.isOnGround() && this.getVelocity().y < -1 && (feetArmorStack.getItem().equals(FluxTechItems.GRAVITRONS))) {
            super.travel(movementInput);
        }
        if (!this.isOnGround() && this.getVelocity().y < -2.5 && (feetArmorStack.getItem().equals(FluxTechItems.GRAVITRONS))) {
            super.travel(movementInput);
            super.travel(movementInput);
        }
    }

    @Inject(method="getFallSound", at = @At("HEAD"), cancellable = true)
    protected void getFallSound(int distance, CallbackInfoReturnable<SoundEvent> cir) {
        ItemStack feetArmorStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (feetArmorStack.getItem().equals(FluxTechItems.GRAVITRONS)) {
            cir.setReturnValue(SoundEvents.BLOCK_NETHERITE_BLOCK_FALL);
        } else if (feetArmorStack.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)) {
            cir.setReturnValue(SoundEvents.BLOCK_SLIME_BLOCK_PLACE);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void checkConnectedCrystal(CallbackInfo ci) {
        if (this.isAlive()) {

            // If we have a connected crystal, we get dragon healing
            if (this.fluxTech$connectedCrystal != null) {
                this.applyStatusEffect(new StatusEffectInstance(CustomEffects.DRAGONHEALING, 1205, 0));
                if (!this.fluxTech$connectedCrystal.isAlive()) {
                    this.fluxTech$connectedCrystal = null;
                } else if (this.age % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                    this.setHealth(this.getHealth() + 1.0F);
                }
            } else {
                this.removeStatusEffectInternal(CustomEffects.DRAGONHEALING);
            }

            // Update connected crystal to the current closest one.
            List<EndCrystalEntity> list = this.world.getNonSpectatingEntities(EndCrystalEntity.class, this.getBoundingBox().expand(32.0D));
            EndCrystalEntity endCrystalEntity = null;
            double bestDistance = 1000;
            for (EndCrystalEntity nearbyCrystal : list) {
                double currDistance = nearbyCrystal.squaredDistanceTo(this);

                if (currDistance < bestDistance) {
                    bestDistance = currDistance;
                    endCrystalEntity = nearbyCrystal;
                }
            }

            this.fluxTech$connectedCrystal = endCrystalEntity;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void fluxTechBootLogic(CallbackInfo ci) {
        ItemStack feetArmorStack = this.getEquippedStack(EquipmentSlot.FEET);
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && (feetArmorStack.getItem().equals(FluxTechItems.GRAVITRONS))) {
            if (vec3d.y < -fluxTech$vertSpeedMax) {
                fluxTech$vertSpeedMax = (this.getVelocity().y) * -1;
            }

            if (vec3d.y < -1) {
                fluxTech$groundPound = true;
            }
        }
        if (fluxTech$groundPound && feetArmorStack.getItem().equals(FluxTechItems.GRAVITRONS)) {
            LivingEntity entity = this.world.getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT, ((PlayerEntity) (Object) this), 0, 0, 0, this.getBoundingBox().stretch(0.0D, this.getVelocity().y, 0.0D));
            if (entity != null) {
                entity.damage(DamageSource.GENERIC, FluxTechConfig2.get().numbers.gravitronCrushDamage);
                if (entity.getHealth() <= 0) {
                    this.playSound(SoundEvents.ENTITY_TURTLE_EGG_CRACK, 1, 1);
                    this.playSound(SoundEvents.BLOCK_HONEY_BLOCK_STEP, 2, 1);
                }
                this.setVelocity(this.getVelocity().x, fluxTech$vertSpeedMax * FluxTechConfig2.get().numbers.crushBounceMultiplier, this.getVelocity().z);
                fluxTech$groundPound = false;
                fluxTech$vertSpeedMax = 0;
            }

            if (this.isOnGround() || this.isFallFlying()) {
                fluxTech$groundPound = false;
                fluxTech$vertSpeedMax = 0;
            }
        }
    }

    @ModifyVariable(method = "getBlockBreakingSpeed", at = @At(value = "JUMP", opcode = Opcodes.IFLE, ordinal = 0))
    public float getBlockBreakingSpeed(float f) {
        ItemStack itemStack3 = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (this.isSubmergedIn(FluidTags.WATER) && (itemStack3.getItem().isIn(FluxTechTags.LAPIS_TOOLS))) {
            f += 10.0F;
        }
        return f;
    }
}





