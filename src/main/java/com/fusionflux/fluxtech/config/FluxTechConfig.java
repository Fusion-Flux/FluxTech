package com.fusionflux.fluxtech.config;

import com.fusionflux.fluxtech.FluxTech;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = FluxTech.MOD_ID)
public class FluxTechConfig implements ConfigData {
    public static void register() {
        AutoConfig.register(FluxTechConfig.class, JanksonConfigSerializer::new);
    }

    public static FluxTechConfig get() {
        return AutoConfig.getConfigHolder(FluxTechConfig.class).getConfig();
    }

    public static class Numbers {
        public double hPDLaunchPower = 1.25;
        public int hPDCooldown = 10;
        public boolean doCrunch = false;
    }

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("numbers")
    public Numbers numbers = new Numbers();

}

