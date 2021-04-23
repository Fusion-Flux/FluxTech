package com.fusionflux.fluxtech.accessor;

public interface HopperInput {
    default double getInputInventoryY() {
        return 1.0D;
    }
}
