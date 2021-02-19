package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.util.UtilFields;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EnderDragonEntityRenderer.class)
@Environment(EnvType.CLIENT)
public class EnderDragonEntityRendererMixin {
    @ModifyConstant(method = "renderCrystalBeam", constant = @Constant(doubleValue = 2.0F))
    private static double modifyYTranslation(double yOffset) {
        if (UtilFields.beamRenderingOverride) {
            UtilFields.beamRenderingOverride = false;
            yOffset = UtilFields.beamRenderingYTranslation;
        }
        return yOffset;
    }
}
