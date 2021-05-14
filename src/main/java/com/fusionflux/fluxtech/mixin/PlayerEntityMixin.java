package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.accessor.PlayerEntityExtensions;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.items.FluxTechItems;
import com.fusionflux.fluxtech.util.FluxTechTags;
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
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
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
    private boolean groundpound = false;
    @Unique
    private double fallSpeedMax = 0;
    @Unique
    private boolean DoCrunch = false;


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError("FluxTech: Called constructor for PlayerEntityMixin!");
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (damageSource == DamageSource.FALL && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || (itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d movementInput, CallbackInfo ci) {
        ItemStack itemStack3 = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack itemStackChest = this.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && (itemStack3.getItem().equals(FluxTechItems.AEROARMOR))) {
            this.flyingSpeed = this.abilities.getFlySpeed() * (float) FluxTechConfig2.get().numbers.aeroarmorFlightBoost;
        }
        if (!this.isOnGround() && this.getVelocity().y < -1 && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/)) {
            super.travel(movementInput);
        }
        if (!this.isOnGround() && this.getVelocity().y < -2.5 && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/)) {
            super.travel(movementInput);
            super.travel(movementInput);
        }

        if ((itemStack5.getItem().equals(FluxTechItems.SLIDERS))) {
            this.onGround = false;
            this.updateVelocity(this.getSlipSpeed(1.1f), movementInput);

        }

    }

    private float getSlipSpeed(float slipperiness) {
        return this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness));
    }

    @Inject(method = "getFallSound", at = @At("HEAD"), cancellable = true)
    protected void getFallSound(int distance, CallbackInfoReturnable<SoundEvent> cir) {
        ItemStack itemFeet = this.getEquippedStack(EquipmentSlot.FEET);
        if (itemFeet.getItem().equals(FluxTechItems.GRAVITRONS)) {
            cir.setReturnValue(SoundEvents.BLOCK_NETHERITE_BLOCK_FALL);
        }
        if (itemFeet.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)) {
            cir.setReturnValue(SoundEvents.BLOCK_SLIME_BLOCK_PLACE);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {

        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || (itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/))) {

            if (this.getVelocity().y < fallSpeedMax && this.getVelocity().y < 0 /*&& ((!this.isSneaking() && itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) || itemStack5.getItem().equals(FluxTechItems.GRAVITRONS)))*/) {
                fallSpeedMax = Math.abs(this.getVelocity().y);
            }
            if (this.getVelocity().y < -1) {
                groundpound = true;
            }
        }
        Vec3d vec3d = this.getVelocity();
        if (!this.isTouchingWater() && !this.isSneaking() && fallSpeedMax > .3 && this.isOnGround() && itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)) {
            if (vec3d.y < 0.0D) {
                //double d = this instanceof LivingEntity ? 1.0D : 1.8D;
                this.setVelocity(vec3d.x, fallSpeedMax * 0.8D, vec3d.z);
                this.playSound(SoundEvents.BLOCK_SLIME_BLOCK_PLACE, 1, 1);
                if (this.isOnGround() || this.isFallFlying() || this.isSneaking()) {
                    //groundpound = false;
                    fallSpeedMax = 0;
                }
            }
        } else {
            if ((this.isSneaking() || this.isTouchingWater()) && itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)) {
                fallSpeedMax = 0;
            }
        }

        if (groundpound && itemStack5.getItem().equals(FluxTechItems.GRAVITRONS)) {
            List<LivingEntity> listhurt = this.world.getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), null);
            listhurt.remove(this);
            for (LivingEntity entity : listhurt) {
                DoCrunch = true;
                if (!this.world.isClient) {
                    entity.damage(DamageSource.GENERIC, FluxTechConfig2.get().numbers.gravitronCrushDamage);
                    world.playSound(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.NEUTRAL, 1, 1);
                    world.playSound(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.NEUTRAL, 2, 1);
                    break;
                }
            }
            if (DoCrunch) {
                this.setVelocity(this.getVelocity().x, fallSpeedMax / FluxTechConfig2.get().numbers.crushBounceMultiplier, this.getVelocity().z);
                groundpound = false;
                DoCrunch = false;
                fallSpeedMax = 0;
            }
            if ((this.isOnGround() || this.isFallFlying()) && !DoCrunch) {
                groundpound = false;
                fallSpeedMax = 0;
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





