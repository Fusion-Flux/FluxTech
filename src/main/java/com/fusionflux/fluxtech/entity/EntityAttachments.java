package com.fusionflux.fluxtech.entity;

import net.minecraft.util.math.Direction;

public interface EntityAttachments {
    boolean fluxtech$isRolling();

    void fluxtech$setRolling(boolean rolling);

    Direction fluxtech$getDirection();

    void fluxtech$setDirection(Direction direction);

    double fluxtech$getMaxFallSpeed();

    void fluxtech$setMaxFallSpeed(double maxFallSpeed);
}
