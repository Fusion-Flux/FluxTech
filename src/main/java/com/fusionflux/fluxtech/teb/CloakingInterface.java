package com.fusionflux.fluxtech.teb;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public interface CloakingInterface {
    void setCloseToCloakingDevice(boolean v);
    boolean getCloseToCloakingDevice();

    static boolean isCloseToCloakingDevice(ServerPlayerEntity player) {
        return ((CloakingInterface) player).getCloseToCloakingDevice();
    }

    void cloakWorld(World world);
    void uncloakWorld();
    ServerWorld getUncloakedWorld();

    void setCloakingViewEnabled(boolean v);
    boolean getCloakingViewEnabled();
}
