package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.entity.IsRollingAccessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Camera camera;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V"))
    private void preWorldRender(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo callbackInfo) {
        IsRollingAccessor accessor = (IsRollingAccessor) camera.getFocusedEntity();

        if (accessor.isRolling()) {
            Quaternion quaternion = accessor.getDirection().getRotationQuaternion();
            quaternion.conjugate();
            matrix.multiply(quaternion);
        }
    }
}
