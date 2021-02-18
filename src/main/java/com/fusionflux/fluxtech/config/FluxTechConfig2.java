package com.fusionflux.fluxtech.config;

import com.fusionflux.fluxtech.FluxTech;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

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
        public boolean enableGels = true;
        public boolean enableHPD = true;
        public boolean enablePortal2Blocks = true;
        public boolean enableHLB = true;
        public boolean enableEndurium = true;
        public boolean enableSmoothEndStone = true;
        public boolean enableGravitrons = true;
        public boolean enableAeroArmor = true;
        public boolean enableSCNB = true;
        public boolean enableHLBLaunchPreventer = true;
    }
    public static class Numbers {
        public int maxBridgeLength = 127;
        public int hPDCooldown = 10;
        public double hPDLaunchPower = 2;
        public double aeroArmorFlightBoost = 2;
        public double crushBounceMultiplier = 1.5;
        public int enduriumTpRange = 64;
        public int gravitronCrushDamage = 30;

    }
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("enabled")
    public Enabled enabled = new Enabled();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("numbers")
    public Numbers numbers = new Numbers();
}

