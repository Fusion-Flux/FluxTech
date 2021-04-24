package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.accessor.PlayerEntityExtensions;
import com.fusionflux.fluxtech.util.UtilFields;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        EndCrystalEntity connectedCrystal = ((PlayerEntityExtensions) abstractClientPlayerEntity).fluxTech$getConnectedCrystal();
        Vec3d playerPos = abstractClientPlayerEntity.getPos();
        if (connectedCrystal != null && playerPos != null) {
            matrixStack.push();
            float l = (float) (connectedCrystal.getX() - MathHelper.lerp(g, abstractClientPlayerEntity.prevX, abstractClientPlayerEntity.getX()));
            float m = (float) (connectedCrystal.getY() - MathHelper.lerp(g, abstractClientPlayerEntity.prevY, abstractClientPlayerEntity.getY()) + 2.0F - UtilFields.beamRenderingYTranslation);
            float t = (float) (connectedCrystal.getZ() - MathHelper.lerp(g, abstractClientPlayerEntity.prevZ, abstractClientPlayerEntity.getZ()));
            UtilFields.beamRenderingOverride = true;
            if (abstractClientPlayerEntity.isInSwimmingPose() || abstractClientPlayerEntity.isFallFlying()) {
                UtilFields.beamRenderingYTranslation = 0.3D;
            } else if (abstractClientPlayerEntity.isInSneakingPose()) {
                UtilFields.beamRenderingYTranslation = 0.8D;
            } else{
                UtilFields.beamRenderingYTranslation = 1.0D;
        }
            EnderDragonEntityRenderer.renderCrystalBeam(l, m + EndCrystalEntityRenderer.getYOffset(connectedCrystal, g), t, g, abstractClientPlayerEntity.age, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();
        }
    }
}
