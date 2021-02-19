package com.fusionflux.fluxtech.util;

import com.fusionflux.fluxtech.FluxTech;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class FluxTechTags {
    public static final Tag<Block> HPD_DENY_LAUNCH = TagRegistry.block(new Identifier(FluxTech.MOD_ID, "hpd_deny_launch"));
    public static final Tag<Item> LAPIS_TOOLS = TagRegistry.item(new Identifier(FluxTech.MOD_ID, "lapis_tools"));
}
