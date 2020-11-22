
package com.fusionflux.fluxtech.compatability;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import com.oroarmor.config.ConfigItem;


import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;

public class ModMenuIntegration implements ModMenuApi {

    private ConfigCategory createCategory(ConfigBuilder builder, String categoryName) {
        return builder.getOrCreateCategory(new TranslatableText(categoryName));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(new TranslatableText("config.fluxtech"));

            builder.setSavingRunnable(FluxTech.CONFIG::saveConfigToFile);

            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory enabledFeatures = createCategory(builder, "config.fluxtech.enabled");
            ConfigCategory values = createCategory(builder, "config.fluxtech.values");
            ConfigCategory integer_values = createCategory(builder, "config.fluxtech.integer_values");

            FluxTechConfig.ENABLED.OPTIONS.forEach(ci -> setupBooleanConfigItem((ConfigItem<Boolean>) ci, enabledFeatures, entryBuilder));

            FluxTechConfig.INTEGER_VALUES.OPTIONS.forEach(ci -> setupIntegerConfigItem((ConfigItem<Integer>) ci, integer_values, entryBuilder));

            FluxTechConfig.VALUES.OPTIONS.forEach(ci -> setupDoubleConfigItem((ConfigItem<Double>) ci, values, entryBuilder));

            return builder.build();
        };
    }

    @Override
    public String getModId() {
        return "fluxtech";
    }

    private void setupBooleanConfigItem(ConfigItem<Boolean> ci, ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText(ci.getDetails()), ci.getValue()).setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
    }

    private void setupDoubleConfigItem(ConfigItem<Double> ci, ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        category.addEntry(entryBuilder.startDoubleField(new TranslatableText(ci.getDetails()), ci.getValue()).setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
    }

    private void setupIntegerConfigItem(ConfigItem<Integer> ci, ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        category.addEntry(entryBuilder.startIntField(new TranslatableText(ci.getDetails()), ci.getValue()).setSaveConsumer(ci::setValue).setDefaultValue(ci::getDefaultValue).build());
    }
}