
package com.fusionflux.fluxtech.compatability;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.fusionflux.fluxtech.config.FluxTechConfig2;


import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(FluxTechConfig2.class, parent).get();
    }
}