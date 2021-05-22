package com.fusionflux.fluxtech.accessor;

public interface HopperInput {
    default double fluxtech$getInputInventoryY() {
        return 1.0D;
    }
}
