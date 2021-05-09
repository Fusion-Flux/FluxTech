package com.fusionflux.fluxtech.teb;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;

public class CloakingEvents {
    public static Thread cloakingThread;
    public static CloakingServer cloakingServer;

    public static void registerCloakingEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            cloakingThread = new Thread(() -> {
                cloakingServer = new CloakingServer(minecraftServer);
                cloakingServer.start();
            });
            cloakingThread.start();
            cloakingThread.setName("FluxTech Cloaking Thread");
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(minecraftServer -> {
            cloakingServer.stop();
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, b) -> dispatcher.register(CommandManager.literal("cloakview")
                .then(CommandManager.literal("toggle").executes((context) -> {
                    CloakingInterface pi = (CloakingInterface) context.getSource().getPlayer();
                    pi.setCloakingViewEnabled(!pi.getCloakingViewEnabled());
                    context.getSource().sendFeedback(new LiteralText("You have now " + (pi.getCloakingViewEnabled() ? "enabled" : "disabled") + " cloaking view!"), false);
                    if (!pi.getCloakingViewEnabled()) {
                        cloakingServer.getManager(context.getSource().getPlayer()).purgeCache();
                    }
                    return Command.SINGLE_SUCCESS;
                }))));
    }
}
