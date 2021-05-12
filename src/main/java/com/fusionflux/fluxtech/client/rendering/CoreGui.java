package com.fusionflux.fluxtech.client.rendering;

import com.fusionflux.fluxtech.blocks.entities.StorageCoreBlockEntity;
import com.fusionflux.fluxtech.blocks.entities.StorageNodeBlockEntity;
import io.github.astrarre.gui.v0.api.RootContainer;
import io.github.astrarre.gui.v0.api.base.panel.ACenteringPanel;
import io.github.astrarre.gui.v0.api.base.panel.APanel;
import io.github.astrarre.gui.v0.api.base.statik.ABeveledRectangle;
import io.github.astrarre.gui.v0.api.base.statik.ADarkenedBackground;
import io.github.astrarre.gui.v0.api.base.widgets.AButton;
import io.github.astrarre.gui.v0.api.base.widgets.ATextFieldWidget;
import io.github.astrarre.gui.v0.fabric.adapter.slot.ABlockEntityInventorySlot;
import io.github.astrarre.gui.v0.fabric.adapter.slot.APlayerSlot;
import io.github.astrarre.gui.v0.fabric.adapter.slot.ASlot;
import io.github.astrarre.networking.v0.api.network.NetworkMember;
import io.github.astrarre.rendering.v0.api.Transformation;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class CoreGui {
    private static List<ASlot> nodeSlots;
    private static int currentInv;

    public static void open(ServerPlayerEntity entity, StorageCoreBlockEntity blockEntity) {
        RootContainer.openC((NetworkMember) entity, container -> open(entity, container, blockEntity));
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



        // Add a button to get the next node
        AButton nextButton = new AButton(AButton.ARROW_RIGHT);
        nextButton.setTransformation(Transformation.translate(163, 4, 0));
        nextButton.onPress(() -> {
            ++currentInv;
            if (currentInv < 0 || currentInv >= blockEntity.connectedNodes.size()) {
                currentInv = 0;
            }
            updateSlots(center, blockEntity);
        });
        center.add(nextButton);
        // Add a button to get the previous node
        AButton prevButton = new AButton(AButton.ARROW_LEFT);
        prevButton.setTransformation(Transformation.translate(156, 4, 0));
        prevButton.onPress(() -> {
            --currentInv;
            if (currentInv < 0 || currentInv >= blockEntity.connectedNodes.size()) {
                currentInv = blockEntity.connectedNodes.size() - 1;
            }
            updateSlots(center, blockEntity);
        });
        center.add(prevButton);

        List<ASlot> hotbar = new ArrayList<>();
        nodeSlots = new ArrayList<>();
        updateSlots(center, blockEntity);

        for (int inventoryRow = 0; inventoryRow < 3; ++inventoryRow) {
            for (int inventoryColumn = 0; inventoryColumn < 9; ++inventoryColumn) {
                ASlot slot = new APlayerSlot(entity.inventory, inventoryColumn + inventoryRow * 9 + 9);
                slot.setTransformation(Transformation.translate(6 + inventoryColumn * 18, 82 + inventoryRow * 18, 0));
                center.add(slot);
                hotbar.add(slot);
            }
        }

        for (int hotbarIndex = 0; hotbarIndex < 9; ++hotbarIndex) {
            ASlot slot = new APlayerSlot(entity.inventory, hotbarIndex);
            slot.setTransformation(Transformation.translate(6 + hotbarIndex * 18, 140, 0));
            center.add(slot);
            slot.linkAll(container, hotbar);
            for (ASlot hotbarSlot : hotbar) {
                hotbarSlot.link(container, slot);
            }
        }
    }

    private static void updateSlots(ACenteringPanel center, StorageCoreBlockEntity core) {
        nodeSlots.forEach(center::remove);
        nodeSlots.forEach(center::removeClient);
        nodeSlots.clear();
        StorageNodeBlockEntity be = core.getNode(currentInv * 27);

        if (be != null) {
            for (int inventoryRow = 0; inventoryRow < be.size() / 9; ++inventoryRow) {
                for (int inventoryColumn = 0; inventoryColumn < 9; ++inventoryColumn) {
                    ASlot slot = new ABlockEntityInventorySlot<>(be, inventoryColumn + inventoryRow * 9);
                    slot.setTransformation(Transformation.translate(6 + inventoryColumn * 18, 15 + inventoryRow * 18, 0));
                    center.add(slot);
                    nodeSlots.add(slot);
                }
            }
        }
    }
}
