package com.fusionflux.flyingfluxery.sound;

import com.fusionflux.flyingfluxery.FlyingFluxery;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechSounds {
    public static final Identifier HPD_BLAST = new Identifier(FlyingFluxery.MOD_ID, "hpdblast");
    public static SoundEvent BLAST_EVENT = new SoundEvent(HPD_BLAST);

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, HPD_BLAST, BLAST_EVENT);
    }
}
