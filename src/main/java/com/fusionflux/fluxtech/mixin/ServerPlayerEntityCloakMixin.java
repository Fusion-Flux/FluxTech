package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.teb.CloakingInterface;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityCloakMixin extends PlayerEntity implements CloakingInterface {
    public ServerPlayerEntityCloakMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
        throw new AssertionError("FluxTech ServerPlayerEntityCloakMixin constructor was called!");
    }

    @Shadow
    public abstract ServerWorld getServerWorld();

    @Unique
    private volatile boolean isCloseToCloakingDevice;
    @Unique
    private World uncloakedWorld;
    @Unique
    private boolean cloakingViewEnabled;

    @Override
    public void setCloseToCloakingDevice(boolean v) {
        isCloseToCloakingDevice = v;
    }

    @Override
    public boolean getCloseToCloakingDevice() {
        return isCloseToCloakingDevice;
    }

    @Override
    public void cloakWorld(World world) {
        uncloakedWorld = this.world;
        this.world = world;
    }

    @Override
    public void uncloakWorld() {
        this.world = uncloakedWorld;
        uncloakedWorld = null;
    }

    @Override
    public ServerWorld getUncloakedWorld() {
        if (uncloakedWorld != null) {
            return (ServerWorld) uncloakedWorld;
        }
        return this.getServerWorld();
    }

    @Inject(method = "writeCustomDataToTag", at = @At("HEAD"))
    public void writeInject(CompoundTag tag, CallbackInfo ci) {
        if (!this.cloakingViewEnabled) {
            tag.putBoolean("cloaking_view_enabled", false);
        }
    }

    @Inject(method = "readCustomDataFromTag", at = @At("HEAD"))
    public void readInject(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("cloaking_view_enabled")) {
            this.cloakingViewEnabled = tag.getBoolean("cloaking_view_enabled");
        }
    }

    @Override
    public void setCloakingViewEnabled(boolean cloakingViewEnabled) {
        this.cloakingViewEnabled = cloakingViewEnabled;
    }

    @Override
    public boolean getCloakingViewEnabled() {
        return this.cloakingViewEnabled;
    }
}
