package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.FluxTech;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechItems {
    public static final ArmorMaterial StableArmor = new StableArmor();
    public static final ArmorMaterial UnstableArmor = new UnstableArmor();
    public static final FabricItem HANDHELD_PROPULSION_DEVICE = new FabricItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item GRAVITRONS = new ArmorItem(StableArmor, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.MISC));
    public static final Item UNSTABLE_GRAVITRONS = new ArmorItem(StableArmor, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.MISC).fireproof());
    public static final Item CUSTOM_MATERIAL_LEGGINGS = new ArmorItem(StableArmor, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.MISC).fireproof());

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "handheld_propulsion_device"), HANDHELD_PROPULSION_DEVICE);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "gravitrons"), GRAVITRONS);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "unstable_gravitrons"), UNSTABLE_GRAVITRONS);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "custom_material_leggings"), CUSTOM_MATERIAL_LEGGINGS);
    }


}
