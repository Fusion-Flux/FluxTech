package com.fusionflux.fluxtech.config;

import com.fusionflux.fluxtech.FluxTech;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = FluxTech.MOD_ID)
public class FluxTechConfig2 implements ConfigData {
    public static void register() {
        AutoConfig.register(FluxTechConfig2.class, JanksonConfigSerializer::new);
    }

    public static FluxTechConfig2 get() {
        return AutoConfig.getConfigHolder(FluxTechConfig2.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(FluxTechConfig2.class).save();
    }

    public static class Enabled {
        public boolean enableHPD = true;
        public boolean enableAeroArmor = true;
        public boolean enableGravitrons = true;
        public boolean enableSCNB = true;
        public boolean enableEndurium = true;
        public boolean enableSmoothEndStone = true;
    }
    public static class Numbers {
        public double hPDLaunchPower = 2;
        public int hPDCooldown = 10;
        public boolean hPDReturnsProjectiles = false;
        public boolean enableHPDLaunchPreventer = true;
        public double aeroarmorFlightBoost = 2;
        public int gravitronCrushDamage = 30;
        public double crushBounceMultiplier = 0.75;
        public double slimeBounceMultiplier = 0.75;
    }
    public static class NumbersBlock {
        public int enduriumTpRange = 64;
    }
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("enabled")
    public Enabled enabled = new Enabled();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("numbers")
    public Numbers numbers = new Numbers();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("numbersblock")
    public NumbersBlock numbersblock = new NumbersBlock();
}

