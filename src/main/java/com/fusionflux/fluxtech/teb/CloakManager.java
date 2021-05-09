package com.fusionflux.fluxtech.teb;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.blocks.entities.CloakingDeviceBlockEntity;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.teb.objects.CloakedArea;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CloakManager {
    private final ArrayList<BlockPos> checked = new ArrayList<>();
    private ArrayList<CloakedArea> cloakedAreas = new ArrayList<>();
    private final ServerPlayerEntity player;
    private final FluxTechConfig2 config;

    public CloakManager(ServerPlayerEntity player, FluxTechConfig2 config) {
        this.player = player;
        this.config = config;
    }

    public void update() {
        ServerWorld world = ((CloakingInterface) player).getUncloakedWorld();

        Stream<PointOfInterest> POIStream = getCloakingDevicesInChunkRadius(world.getPointOfInterestStorage(), player.getBlockPos(), config.cloakingNumbers.cloakingRenderRadius);
        PointOfInterest[] cloakingDevices = POIStream.toArray(PointOfInterest[]::new);

        checked.clear();
        ArrayList<CloakedArea> newCloakedAreas = new ArrayList<>();

        for (PointOfInterest cloakingDevice : cloakingDevices) {
            try {
                BlockPos cloakingDevicePos = cloakingDevice.getPos();
                if (checked.contains(cloakingDevicePos)) {
                    continue;
                }

                CloakingDeviceBlockEntity be = (CloakingDeviceBlockEntity) world.getWorldChunk(cloakingDevicePos).getBlockEntity(cloakingDevicePos, WorldChunk.CreationType.IMMEDIATE);

                if (be != null) {
                    CloakedArea cloakedArea = new CloakedArea(cloakingDevicePos, be.getRadius());
                    newCloakedAreas.add(cloakedArea);
                }

                checked.add(cloakingDevicePos.toImmutable());
            } catch (IllegalArgumentException ignored) { }
        }
        this.cloakedAreas = newCloakedAreas;
    }

    private void garbageCollect(ServerPlayerEntity player) {
        cloakedAreas.removeIf(cloakedArea ->
                cloakedArea.getDistance(player.getBlockPos()) > config.cloakingNumbers.cloakingRenderRadius * 16
                || cloakedArea.contains(player.getBlockPos())
        );
    }

    public ArrayList<CloakedArea> getCloakedAreas() {
        return cloakedAreas;
    }

    private static Stream<PointOfInterest> getCloakingDevicesInChunkRadius(PointOfInterestStorage storage, BlockPos pos, int radius) {
        return ChunkPos.stream(new ChunkPos(pos), radius).flatMap(chunkPos
                -> storage.getInChunk(poi
                        -> poi == FluxTechBlocks.CLOAKING_DEVICE, chunkPos, PointOfInterestStorage.OccupationStatus.ANY)
        );
    }
}