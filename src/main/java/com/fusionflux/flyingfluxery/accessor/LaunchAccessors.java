package com.fusionflux.flyingfluxery.accessor;

import net.minecraft.util.math.Vec3d;

public interface LaunchAccessors {


    void setLaunchVelocity(Vec3d launchVelocity);

    Vec3d getLaunchVelocity();

    void setBlastJumping(boolean isBlastJumping);

    boolean getBlastJumping();
}
