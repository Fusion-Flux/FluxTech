package com.fusionflux.fluxtech.teb;

import com.fusionflux.fluxtech.config.FluxTechConfig2;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloakingServer {
    private final MinecraftServer server;
    private volatile boolean isServerActive = true;
    private long nextTick;
    private int tickCount;
    private final Map<ServerPlayerEntity, PlayerManager> playerManagers = new HashMap<>();
    private final FluxTechConfig2 config = AutoConfig.getConfigHolder(FluxTechConfig2.class).getConfig();

    public CloakingServer(MinecraftServer server) {
        this.server = server;
    }

    public void start() {
        System.out.println("Starting FluxTech Cloaking thread");
        while (isServerActive) {
            long currentTime = System.currentTimeMillis();
            if (currentTime < nextTick) {
                try {
                    Thread.sleep(nextTick-currentTime);
                } catch (InterruptedException ignored) { }
                continue;
            }
            try {
                tick();
                tickCount++;
            } catch (Exception e) {
                System.out.println("Exception occurred whilst ticking the FluxTech Cloaking thread");
                e.printStackTrace();
            }
            nextTick = System.currentTimeMillis() + 50;
        }
    }

    public void stop() {
        System.out.println("Stopping FluxTech Cloaking thread");
        isServerActive = false;
    }

    public void tick() {
        //Sync player managers
        List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();

        playerManagers.entrySet().removeIf(i -> !playerList.contains(i.getKey()));
        for (ServerPlayerEntity player : playerList) {
            if (!playerManagers.containsKey(player)) {
                playerManagers.put(player, new PlayerManager(player, config));
            }
        }

        //Tick player managers
        playerManagers.forEach((player, manager) -> manager.tick(tickCount));
    }

    public PlayerManager getManager(ServerPlayerEntity player) {
        return playerManagers.get(player);
    }
}
