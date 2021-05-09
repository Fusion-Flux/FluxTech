package com.fusionflux.fluxtech.mixin.client;

import com.fusionflux.fluxtech.accessor.Hideable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method = "renderEntity", cancellable = true)
    private void hideEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
                            MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        Hideable hideable = (Hideable) entity;
        if (hideable.fluxtech$isHidden() && !MinecraftClient.getInstance().player.getPos().isInRange(entity.getPos(), 16)) {
            info.cancel();
        }
    }
}
