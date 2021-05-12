package com.fusionflux.fluxtech.effects;

import com.fusionflux.fluxtech.FluxTech;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomEffects {

    public static final StatusEffect DRAGONHEALING = new DragonHealing(StatusEffectType.BENEFICIAL, 0x98D982);

    public static void registerEffects() {

        Registry.register(Registry.STATUS_EFFECT, new Identifier(FluxTech.MOD_ID, "dragon_healing"), DRAGONHEALING);
    }
}
