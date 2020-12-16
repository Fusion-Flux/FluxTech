package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.items.FluxTechItems;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    @Final
    public PlayerAbilities abilities;
    private int groundpound = 0;
    private double fallSpeedMax = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (damageSource == DamageSource.FALL && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d movementInput, CallbackInfo ci) {
        ItemStack itemStack3 = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && (itemStack3.getItem().equals(FluxTechItems.AEROARMOR))) {
            this.flyingSpeed = this.abilities.getFlySpeed() * (float) (this.isSprinting() ? FluxTechConfig.VALUES.AEROARMOR_FLIGHT_BOOST.getValue() : 1);
        }
        if (!this.isOnGround() && this.getVelocity().y < -1 && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/)) {
            super.travel(movementInput);
        }
        if (!this.isOnGround() && this.getVelocity().y < -2.5 && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/)) {
            super.travel(movementInput);
            super.travel(movementInput);
        }
    }

    @Inject(method="getFallSound", at = @At("HEAD"), cancellable = true)
    protected void getFallSound(int distance, CallbackInfoReturnable<SoundEvent> cir){
        ItemStack itemFeet = this.getEquippedStack(EquipmentSlot.FEET);
        if(itemFeet.getItem().equals(FluxTechItems.GRAVITRONS)){
            cir.setReturnValue(SoundEvents.BLOCK_NETHERITE_BLOCK_FALL);
        }
        if(itemFeet.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)){
            cir.setReturnValue(SoundEvents.BLOCK_SLIME_BLOCK_FALL);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {


        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || (itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) /*|| itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)*/))) {

            if (this.getVelocity().y < fallSpeedMax /*&& ((!this.isSneaking() && itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS) || itemStack5.getItem().equals(FluxTechItems.GRAVITRONS)))*/) {
                fallSpeedMax = (this.getVelocity().y) * -1;
            }
            if(this.getVelocity().y < -1)
            {
                groundpound = 1;
            }
        }
        Vec3d vec3d = this.getVelocity();
        if(!this.isTouchingWater()&&!this.isSneaking()&&fallSpeedMax>.3 && this.isOnGround() && itemStack5.getItem().equals(FluxTechItems.SLIME_COATED_NETHERITE_BOOTS)) {
            if (vec3d.y < 0.0D) {
                //double d = this instanceof LivingEntity ? 1.0D : 1.8D;
                this.setVelocity(vec3d.x, fallSpeedMax * .8, vec3d.z);
                if (this.isOnGround() || this.isFallFlying() || this.isSneaking()) {
                    groundpound = 0;
                    fallSpeedMax = 0;
                }
            }
        }else{
            if(this.isSneaking()|| this.isTouchingWater()) {
                fallSpeedMax = 0;
            }
        }
        if (groundpound == 1 && itemStack5.getItem().equals(FluxTechItems.GRAVITRONS)) {
            List<LivingEntity> listhurt = this.world.getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), null);
            listhurt.remove(this);
            for (LivingEntity entity : listhurt) {
                entity.damage(DamageSource.GENERIC, FluxTechConfig.INTEGER_VALUES.GRAVITRON_CRUSH_DAMAGE.getValue());
                if(entity.getHealth()<=0)
                {
                    this.playSound(SoundEvents.ENTITY_TURTLE_EGG_CRACK, 1, 1);
                    this.playSound(SoundEvents.BLOCK_HONEY_BLOCK_STEP, 2, 1);
                }
                this.setVelocity(this.getVelocity().x, fallSpeedMax/FluxTechConfig.VALUES.CRUSH_BOUNCE_MULTIPLIER.getValue(), this.getVelocity().z);
                groundpound = 0;
                fallSpeedMax = 0;
                break;
            }
            if(this.isOnGround()||this.isFallFlying())
            {
                groundpound = 0;
                fallSpeedMax = 0;
            }
        }
    }

}




