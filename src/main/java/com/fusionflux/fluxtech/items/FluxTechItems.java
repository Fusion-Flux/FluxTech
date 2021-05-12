package com.fusionflux.fluxtech.items;

import com.fusionflux.fluxtech.FluxTech;
import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.material.LapisMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluxTechItems {

    public static final LapisMaterial LAPIS_MATERIAL = new LapisMaterial();


    public static final ArmorMaterial FluxTechArmor = new FluxTechArmor();
    public static final ArmorMaterial FluxTechArmor2 = new FluxTechArmor2();
    public static final HandheldPropulsionDevice HANDHELD_PROPULSION_DEVICE = new HandheldPropulsionDevice(new FabricItemSettings().group(FluxTech.FLUXTECH_GROUP).maxCount(1).fireproof());
    public static final Item GRAVITRONS = new ArmorItem(FluxTechArmor, EquipmentSlot.FEET, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).fireproof());
    public static final Item AEROARMOR = new ArmorItem(FluxTechArmor, EquipmentSlot.LEGS, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).fireproof());
    public static final Item SLIME_COATED_NETHERITE_BOOTS = new ArmorItem(FluxTechArmor2, EquipmentSlot.FEET, new Item.Settings().group(FluxTech.FLUXTECH_GROUP).fireproof());

    public static final Item MELTEDPEARL = new Item(new FabricItemSettings().group(FluxTech.FLUXTECH_GROUP).maxCount(16));


    public static final Item ENDURIUM_BUCKET = new BucketItem(FluxTechBlocks.ENDURIUM, new Item.Settings()
            .recipeRemainder(Items.BUCKET).maxCount(1).group(FluxTech.FLUXTECH_GROUP));

    public static void registerItems() {
        if (FluxTechConfig2.get().enabled.enableHPD)
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "handheld_propulsion_device"), HANDHELD_PROPULSION_DEVICE);
        if (FluxTechConfig2.get().enabled.enableGravitrons)
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "gravitrons"), GRAVITRONS);
        if (FluxTechConfig2.get().enabled.enableAeroArmor)
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "aeroarmor"), AEROARMOR);
        if (FluxTechConfig2.get().enabled.enableSCNB)
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "slime_coated_netherite_boots"), SLIME_COATED_NETHERITE_BOOTS);
        if (FluxTechConfig2.get().enabled.enableEndurium) {
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "melted_ender_pearl"), MELTEDPEARL);
            Registry.register(Registry.ITEM, new Identifier(FluxTech.MOD_ID, "endurium_bucket"), ENDURIUM_BUCKET);
        }

    }


}
