package com.fusionflux.fluxtech.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;


public class DragonHealing extends StatusEffect {
    protected DragonHealing(StatusEffectCategory type, int color) {
        super(StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
                0); // color in RGB
    }
}

