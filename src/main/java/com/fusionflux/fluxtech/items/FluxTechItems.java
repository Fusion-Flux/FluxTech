package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.config.FluxTechConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechItems {
    public static final ArmorMaterial FluxTechArmor = new FluxTechArmor();
    public static final ArmorMaterial ExperimentalArmor = new ExperimentalArmor();
    public static final FabricItem HANDHELD_PROPULSION_DEVICE = new FabricItem(new FabricItemSettings().group(FluxTech.FLUXTECH_GROUP).maxCount(1).fireproof());
    public static final Item GRAVITRONS = new ArmorItem(FluxTechArmor, EquipmentSlot.FEET, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).fireproof());
    public static final Item UNSTABLE_GRAVITRONS = new ArmorItem(FluxTechArmor, EquipmentSlot.FEET, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).fireproof());
    public static final Item AEROARMOR = new ArmorItem(FluxTechArmor, EquipmentSlot.LEGS, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).fireproof());

    public static void registerItems() {
        if (FluxTechConfig.ENABLED.ENABLED_HPD.getValue())
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "handheld_propulsion_device"), HANDHELD_PROPULSION_DEVICE);
        if (FluxTechConfig.ENABLED.ENABLED_GRAVITRONS.getValue())
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "gravitrons"), GRAVITRONS);
        if (FluxTechConfig.ENABLED.ENABLED_AEROARMOR.getValue())
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "aeroarmor"), AEROARMOR);
    }


}
