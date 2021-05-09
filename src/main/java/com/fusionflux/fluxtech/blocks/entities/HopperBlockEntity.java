package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;

public class HopperBlockEntity extends AbstractHopperBlockEntity {
    public HopperBlockEntity() {
        this(1);
    }

    public HopperBlockEntity(int distance) {
        super(FluxTechBlocks.HOPPER_BLOCK_ENTITY, distance);
    }
}
