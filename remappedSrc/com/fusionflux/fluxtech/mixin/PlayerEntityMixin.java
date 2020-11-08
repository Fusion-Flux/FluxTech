package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.delay.DelayedForLoop;
import com.fusionflux.fluxtech.delay.DelayedForLoopManager;
import com.fusionflux.fluxtech.items.FluxTechItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
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
    private Entity targetEntity;
    private ItemEntity targetEntity2;
    private LivingEntity livingEntity;
    private ItemEntity itemEntity;
    private BlockEntity targetEntity3;
    private int groundpound = 0;
    private double experimental = 0;
    private double launchd = .7;
    private boolean wasOnGround = ((PlayerEntity) (Object) this).isOnGround();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void sendMessage(Text message, boolean actionBar);

    @Shadow
    protected abstract void spawnParticles(ParticleEffect parameters);

    /*@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = this.getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = this.getStackInHand(Hand.OFF_HAND);
        if (damageSource == DamageSource.FALL && (itemStack.getItem().equals(FluxTechItems.FABRIC_ITEM)||itemStack2.getItem().equals(FluxTechItems.FABRIC_ITEM))){
            cir.setReturnValue(true);
        }
    }*/

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (damageSource == DamageSource.FALL && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d movementInput, CallbackInfo ci) {
        ItemStack itemStack3 = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack itemStack4 = this.getStackInHand(Hand.OFF_HAND);
        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && (itemStack3.getItem().equals(FluxTechItems.CUSTOM_MATERIAL_LEGGINGS))) {
            this.flyingSpeed = this.abilities.getFlySpeed() * (float) (this.isSprinting() ? 1.5 : 1);
        }
        if (!this.isOnGround() && this.getVelocity().y < -1 && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS))) {
            super.travel(movementInput);
        }
        if (!this.isOnGround() && this.getVelocity().y < -2.5 && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS))) {
            super.travel(movementInput);
            super.travel(movementInput);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {


        ItemStack itemStack5 = this.getEquippedStack(EquipmentSlot.FEET);
        if (!this.isOnGround() && this.getVelocity().y < -1 && (itemStack5.getItem().equals(FluxTechItems.GRAVITRONS) || itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS))) {
            groundpound = 1;
            if (this.getVelocity().y < experimental) {
                experimental = (this.getVelocity().y) * -1;
            }
        }

        boolean isOnGround = ((PlayerEntity) (Object) this).isOnGround();
        //if(wasOnGround != isOnGround && isOnGround) {
        if (this.isOnGround() && groundpound == 1) {
            groundpound = 0;
            //double launchd = .7;
            double test = experimental * 4;
            Box shockwaveBox = this.getBoundingBox();
            List<Entity> listc = this.world.getEntitiesByClass(Entity.class, this.getBoundingBox(), null);
            listc.remove(this);
            for (Entity entity : listc) {
                this.targetEntity = entity;
                this.targetEntity.damage(DamageSource.ANVIL, 20);
            }

            BlockPos playerPos = ((PlayerEntity) (Object) this).getBlockPos();
            if (!itemStack5.getItem().equals(FluxTechItems.GRAVITRONS)) {
                DelayedForLoopManager.add(new DelayedForLoop((a) -> {
                    if (a % 2 == 0) {
                        int t = a / 2;
                        List<Entity> list = this.world.getEntitiesByClass(Entity.class, shockwaveBox.expand(t, 0, t), null);
                        List<Entity> listb = this.world.getEntitiesByClass(Entity.class, shockwaveBox.expand(t - 1, 0, t - 1), null);
                        list.remove(this);
                        for (Entity entity : listb) {
                            list.remove(entity);
                        }
                        for (Entity entity : list) {
                            this.targetEntity = entity;
                            if (entity.isOnGround()) {
                                this.targetEntity.setVelocity(0, (((test + 1 - t) / (test + 1)) * 1.8), 0);
                            }
                        }
                        if (a >= (test + 1) * 4) {
                            experimental = 0;
                        }
                        if (itemStack5.getItem().equals(FluxTechItems.UNSTABLE_GRAVITRONS)) {
                            Box expandedShockwaveBox = shockwaveBox.expand(t, -1, t);
                            BlockPos.Mutable pos = new BlockPos.Mutable();
                            for (double x = expandedShockwaveBox.getMin(Direction.Axis.X); x <= expandedShockwaveBox.getMax(Direction.Axis.X); x++) {
                                for (double y = expandedShockwaveBox.getMin(Direction.Axis.Y); y <= expandedShockwaveBox.getMax(Direction.Axis.Y); y++) {
                                    for (double z = expandedShockwaveBox.getMin(Direction.Axis.Z); z <= expandedShockwaveBox.getMax(Direction.Axis.Z); z++) {
                                        double dx = playerPos.getX() - x;
                                        double dz = playerPos.getZ() - z;
                                        pos.set(x, y - 1, z);
                                        //double horizDist = Math.sqrt(dx * dx + dz * dz);
                                    /*if (horizDist > t-1 && horizDist < t){
                                        FallingBlockEntity f = new FallingBlockEntity(world, pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D, world.getBlockState(pos));
                                        f.timeFalling = 1;
                                        f.setFallingBlockPos(pos.toImmutable());
                                        f.setVelocity(0, (((test+1-t) / (test+1)) *.3 ), 0);
                                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                                        world.spawnEntity(f);
                                    }*/
                                        //double distance = (x)+(z); &&((dx+dz)<t || (dx-dz)<t || (-dx-dz)<t ||(-dx+dz)<t)
                                        BlockState h = world.getBlockState(pos);
                                        if ((h != Blocks.AIR.getDefaultState()) && ((dx + dz) > t - 1 || (dx - dz) > t - 1 || (-dx - dz) > t - 1 || (-dx + dz) > t - 1) && t > 1) {
                                            FallingBlockEntity f = new FallingBlockEntity(world, pos.getX() + .5, pos.getY(), pos.getZ() + 0.5D, world.getBlockState(pos));
                                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                                            f.timeFalling = 1;
                                            //f.noClip=true;

                                            //f.setFallingBlockPos(pos.toImmutable());
                                            f.setVelocity(0, (((test + 1 - t) / (test + 1)) * .3), 0);

                                            //world.setBlockState(pos, h);

                                            world.spawnEntity(f);
                                            //world.setBlockState(pos, Blocks.AIR.getDefaultState());
                                            // world.setBlockState(pos, h);

                                        }
                                        //do something to the blockstate at "pos"

                                    }
                                }
                            }
                        }
                    }
                }, 1, (test + 1) * 2));
            }
            launchd = .7;
            //experimental = 0;
        }
        //  }
        // wasOnGround = isOnGround;
    }

}




