package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.FluxTech;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechItems {

    public static final ArmorMaterial FluxTechArmor = new FluxTechArmor();
    public static final CopperCreeper COPPER_CREEPER = new CopperCreeper(new FabricItemSettings().group(FluxTech.FLUXTECH_GROUP).maxCount(1).fireproof());
    public static final Item GRAVITRONS = new ArmorItem(FluxTechArmor, EquipmentSlot.FEET, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).fireproof());
    public static final MarketGardner MARKETGARNDER = new MarketGardner(new FabricItemSettings().group(FluxTech.FLUXTECH_GROUP).maxCount(1).fireproof());
    public static void registerItems() {
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "copper_creeper"), COPPER_CREEPER);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "mantreads"), GRAVITRONS);
        Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "marketgardner"), MARKETGARNDER);

    }
}
