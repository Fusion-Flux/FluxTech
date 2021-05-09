package com.fusionflux.fluxtech.teb;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public interface CloakingInterface {
    static boolean isCloseToCloakingDevice(ServerPlayerEntity player) {
        return ((CloakingInterface) player).getCloseToCloakingDevice();
    }

    boolean getCloseToCloakingDevice();

    void setCloseToCloakingDevice(boolean v);

    void cloakWorld(World world);

    void uncloakWorld();

    ServerWorld getUncloakedWorld();

    boolean getCloakingViewEnabled();

    void setCloakingViewEnabled(boolean v);
}
