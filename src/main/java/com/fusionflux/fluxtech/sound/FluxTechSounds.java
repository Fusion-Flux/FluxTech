package com.fusionflux.fluxtech.sound;

import com.fusionflux.fluxtech.FluxTech;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechSounds {
    public static final Identifier HPD_BLAST = new Identifier(FluxTech.MOD_ID, "hpdblast");
    public static SoundEvent BLAST_EVENT = new SoundEvent(HPD_BLAST);

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, HPD_BLAST, BLAST_EVENT);
    }
}
