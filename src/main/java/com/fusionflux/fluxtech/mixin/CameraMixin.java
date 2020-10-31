package com.fusionflux.fluxtech.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {


    @Mutable
    @Shadow
    @Final
    private Vector3f horizontalPlane;
    @Mutable
    @Shadow
    @Final
    private Quaternion rotation;
    @Shadow
    private float lastCameraY;
    @Shadow
    private float cameraY;
    @Mutable
    @Shadow
    @Final
    private Vector3f verticalPlane;
    @Mutable
    @Shadow
    @Final
    private Vector3f diagonalPlane;
    @Shadow
    private Entity focusedEntity;
    @Shadow
    private Vec3d pos;
    @Shadow
    @Final
    private BlockPos.Mutable blockPos;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Shadow
    protected abstract void moveBy(double x, double y, double z);

    @Shadow
    protected abstract void setPos(Vec3d pos);

    @Inject(method = "updateEyeHeight", at = @At("TAIL"))
    private void mixin(CallbackInfo ci) {

    }


}
