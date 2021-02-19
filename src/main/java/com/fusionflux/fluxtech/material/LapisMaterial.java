package com.fusionflux.fluxtech.material;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;

public class LapisMaterial implements ToolMaterial {
    @Override
    public int getDurability() {
        return 200;
    }
    @Override
    public float getMiningSpeedMultiplier() {
        return 1.5F;
    }
    @Override
    public float getAttackDamage() {
        return 3.0F;
    }
    @Override
    public int getMiningLevel() {
        return 2;
    }
    @Override
    public int getEnchantability() {
        return 23;
    }
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.LAPIS_LAZULI);
    }
}
