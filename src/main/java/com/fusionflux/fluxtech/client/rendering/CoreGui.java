package com.fusionflux.fluxtech.client.rendering;

import com.fusionflux.fluxtech.blocks.entities.StorageCoreBlockEntity;
import io.github.astrarre.gui.v0.api.ADrawable;
import io.github.astrarre.gui.v0.api.RootContainer;
import io.github.astrarre.gui.v0.api.base.panel.ACenteringPanel;
import io.github.astrarre.gui.v0.api.base.panel.APanel;
import io.github.astrarre.gui.v0.api.base.statik.ABeveledRectangle;
import io.github.astrarre.gui.v0.api.base.statik.ADarkenedBackground;
import io.github.astrarre.gui.v0.api.base.widgets.AButton;
import io.github.astrarre.gui.v0.fabric.adapter.slot.ABlockEntityInventorySlot;
import io.github.astrarre.gui.v0.fabric.adapter.slot.APlayerSlot;
import io.github.astrarre.gui.v0.fabric.adapter.slot.ASlot;
import io.github.astrarre.networking.v0.api.network.NetworkMember;
import io.github.astrarre.rendering.v0.api.Transformation;
import io.github.astrarre.rendering.v0.api.util.Polygon;
import io.github.astrarre.util.v0.api.Val;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class CoreGui {

    public static void open(ServerPlayerEntity entity, StorageCoreBlockEntity blockEntity) {
        RootContainer.openC((NetworkMember) entity, container -> open(entity, container,blockEntity));
    }


    protected static void open(ServerPlayerEntity entity, RootContainer container, StorageCoreBlockEntity blockEntity) {
        // here's where our gui will be created
        APanel contentPanel = container.getContentPanel();
        // to emulate minecraft guis, we want to make the background go dark-ish
        contentPanel.add(new ADarkenedBackground());
        // we want our gui to be in the middle of the screen, so
        // This object will re-translate itself every time the screen resizes such that the center of the panel is aligned with the center of the screen
        ACenteringPanel center = new ACenteringPanel(175, 165);
        contentPanel.add(center);
        // here, we create a beveled rectangle. 'Bevel' is an outline, this component is basically just a grey rectangle with a special border
        // (this is the same one minecraft guis use)
        // we use the shortcut constructor to tell the beveled rectangle to fill up the entire centering panel
        center.add(new ABeveledRectangle(center));

        AScrollBar scrollBar = new AScrollBar(new AButton(AButton.MEDIUM), Val.ofFloat(0), 165);
        contentPanel.add(scrollBar);


        List<ASlot> hotbar = new ArrayList<>();
        List<ASlot> core = new ArrayList<>();


        for(int inventoryRow = 0; inventoryRow < 3; ++inventoryRow) {
            for(int inventoryColumn = 0; inventoryColumn < 9; ++inventoryColumn) {
                ASlot slot = new APlayerSlot(entity.inventory, inventoryColumn + inventoryRow * 9 + 9);
                slot.setTransformation(Transformation.translate(6 + inventoryColumn * 18, 82 + inventoryRow * 18, 0));
                center.add(slot);
                hotbar.add(slot);
            }
        }

        for(int hotbarIndex = 0; hotbarIndex < 9; ++hotbarIndex) {
            ASlot slot = new APlayerSlot(entity.inventory, hotbarIndex);
            slot.setTransformation(Transformation.translate(6 + hotbarIndex * 18, 140, 0));
            center.add(slot);
            slot.linkAll(container, hotbar);
            for (ASlot hotbarSlot : hotbar) {
                hotbarSlot.link(container, slot);
            }
        }

        for(int inventoryRow = 0; inventoryRow < blockEntity.size()/9; ++inventoryRow) {
            for(int inventoryColumn = 0; inventoryColumn < 9; ++inventoryColumn) {
                ASlot slot = new ABlockEntityInventorySlot<>(blockEntity, inventoryColumn + inventoryRow * 9);
                slot.setTransformation(Transformation.translate(6 + inventoryColumn * 18, 15 + inventoryRow * 18, 0));
                center.add(slot);
                slot.linkAll(container, hotbar);
                for (ASlot hotbarSlot : hotbar) {
                    hotbarSlot.link(container, slot);
                }
            }
        }


    }
}
