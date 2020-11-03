package com.fusionflux.fluxtech.entity;

import net.minecraft.util.math.Direction;

public interface IsRollingAccessor {

    boolean isRolling();

    void setRolling(boolean rolling);

    Direction getDirection();

    void setDirection(Direction direction);
}
