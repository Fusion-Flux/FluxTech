package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.accessor.PlayerEntityExtensions;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    public Vec3d beamSource;
    public Vec3d blockPos;
    public int beamAge;


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        EndCrystalEntity connectedCrystal = ((PlayerEntityExtensions) abstractClientPlayerEntity).fluxtech_getConnectedCrystal();
        blockPos = abstractClientPlayerEntity.getPos();
        if (blockPos != null) {
            matrixStack.push();
            float m = (float)blockPos.getX() + 0.5F;
            float n = (float)blockPos.getY() + 0.5F;
            float o = (float)blockPos.getZ() + 0.5F;
            float p = (float)((double)m - connectedCrystal.getX());
            float q = (float)((double)n - connectedCrystal.getY());
            float r = (float)((double)o - connectedCrystal.getZ());
            matrixStack.translate((double)-p, (double)-q, (double)-r);
            EnderDragonEntityRenderer.renderCrystalBeam(p, q, r, g, connectedCrystal.endCrystalAge, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();
        }

       // super.render(endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

}
