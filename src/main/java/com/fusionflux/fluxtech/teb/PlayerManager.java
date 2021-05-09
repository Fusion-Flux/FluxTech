package com.fusionflux.fluxtech.teb;

import com.fusionflux.fluxtech.accessor.Hideable;
import com.fusionflux.fluxtech.config.FluxTechConfig2;
import com.fusionflux.fluxtech.dim.CloakDimension;
import com.fusionflux.fluxtech.teb.objects.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerManager {
    private final FluxTechConfig2 config;
    private final ServerPlayerEntity player;
    private final CloakManager cloakManager;
    private final List<UUID> hiddenEntities = new ArrayList<>();
    private BlockCache blockCache = new BlockCache();
    private ServerWorld previousWorld;

    public PlayerManager(ServerPlayerEntity player, FluxTechConfig2 config) {
        this.player = player;
        cloakManager = new CloakManager(player, config);
        this.config = config;
    }

    public void tick(int tickCount) {
        if (!((CloakingInterface) player).getCloakingViewEnabled()) {
            return;
        }

        ServerWorld sourceWorld = ((CloakingInterface) player).getUncloakedWorld();
        ServerWorld destinationWorld = sourceWorld.getServer().getWorld(CloakDimension.WORLD_KEY);
        AsyncWorldView sourceView = new AsyncWorldView(sourceWorld);
        AsyncWorldView destinationView = new AsyncWorldView(destinationWorld);

        boolean justWentThroughPortal = false;
        if (sourceWorld != previousWorld) {
            blockCache = new BlockCache();
            justWentThroughPortal = true;
        }

        if (tickCount % 30 == 0 || justWentThroughPortal) {
            cloakManager.update();
        }

        List<CloakedArea> sentCloakedAreas = new ArrayList<>(cloakManager.getCloakedAreas().size() * config.cloakingNumbers.cloakingDepth);
        Chunk2IntMap sentBlocks = new Chunk2IntMap();
        BlockUpdateMap toBeSent = new BlockUpdateMap();

        List<Entity> entities = this.getEntitiesInRange(sourceView);
        if (tickCount % 200 == 0) {
            removeNoLongerExistingEntities(entities);
        }

        ((CloakingInterface) player).setCloseToCloakingDevice(false);
        // iterate through all cloaked areas
        for (CloakedArea cloakedArea : cloakManager.getCloakedAreas()) {

            if (cloakedArea.getDistance(player.getBlockPos()) <= 16) {
                ((CloakingInterface) player).setCloseToCloakingDevice(true);
            }

            if (tickCount % 40 == 0 || justWentThroughPortal) {
                if (cloakedArea.getDistance(player.getBlockPos()) > 20) {
                    //replace the Cloaking Device with air
                    toBeSent.put(cloakedArea.getPos().toImmutable(), Blocks.AIR.getDefaultState());
                }
            }

            //iterate through all of the cloaked area
            sentCloakedAreas.add(cloakedArea);

            entities.removeIf((entity) -> {
                if (cloakedArea.contains(entity.getBlockPos())) {
                    for (UUID uuid : hiddenEntities) {
                        if (entity.getUuid().equals(uuid)) {
                            return true; // cancel if the uuid is already in hiddenEntities
                        }
                    }
                    ((Hideable) entity).fluxtech$setHidden(true);
                    hiddenEntities.add(entity.getUuid());
                    return true;
                }
                return false;
            });

            // go through all blocks in this cloaked area to get the correct block in the cloaked dimension. Then send it to the client
            cloakedArea.iterate((pos) -> {
                double dist = cloakedArea.getDistance(player.getBlockPos());
                BlockState ret;
                // If the player is too close just send the original blocks
                if (cloakedArea.getDistance(player.getBlockPos()) <= 20) {
                    ret = sourceView.getBlock(pos);
                } else {
                    ret = destinationView.getBlock(pos);
                }
                BlockPos imPos = pos.toImmutable();
                sentBlocks.increment(imPos);
                if (!(blockCache.get(imPos) == ret)) {
                    if (!ret.isAir() || !sourceView.getBlock(pos).isAir()) {
                        blockCache.put(imPos, ret);
                        toBeSent.put(imPos, ret);
                    }
                }
            });
        }

        //get all of the old blocks and remove them
        blockCache.purge(sentBlocks, sentCloakedAreas, (pos, cachedState) -> {
            BlockState originalBlock = sourceView.getBlock(pos);
            if (originalBlock != cachedState) {
                toBeSent.put(pos, originalBlock);
            }
        });

        entities.forEach(entity -> {
            for (UUID uuid : hiddenEntities) {
                if (entity.getUuid().equals(uuid)) {
                    hiddenEntities.remove(uuid);
                    ((Hideable) entity).fluxtech$setHidden(false);
                    return;
                }
            }
        });
        toBeSent.sendTo(this.player);
        previousWorld = sourceWorld;
    }

    public void purgeCache() {
        BlockUpdateMap blockUpdateMap = new BlockUpdateMap();
        ((CloakingInterface) player).setCloseToCloakingDevice(false);
        blockCache.purgeAll((pos, cachedState) -> {
            BlockState originalBlock = AsyncWorldView.getBlockAsync(player.getServerWorld(), pos);
            if (originalBlock != cachedState) {
                blockUpdateMap.put(pos.toImmutable(), originalBlock);
            }
        });
        for (CloakedArea cloakedArea : cloakManager.getCloakedAreas()) {
            BlockPos.iterate(cloakedArea.getLowerLeft(), cloakedArea.getUpperRight()).forEach(pos -> {
                blockUpdateMap.put(pos.toImmutable(), AsyncWorldView.getBlockAsync(player.getServerWorld(), pos));
            });
        }
        blockUpdateMap.sendTo(this.player);
    }

    private List<Entity> getEntitiesInRange(AsyncWorldView world) {
        return ChunkPos.stream(new ChunkPos(player.getBlockPos()), config.cloakingNumbers.cloakingRenderRadius).flatMap((chunkPos) -> {
            Chunk chunk = world.getChunk(chunkPos);
            if (chunk instanceof WorldChunk) {
                WorldChunk worldChunk = (WorldChunk) chunk;
                return Arrays.stream(worldChunk.getEntitySectionArray()).flatMap((Function<TypeFilterableList<Entity>, Stream<Entity>>) Collection::stream);
            } else {
                return Stream.empty();
            }
        }).collect(Collectors.toList());
    }

    private void removeNoLongerExistingEntities(List<Entity> existingEntities) {
        hiddenEntities.removeIf((uuid) ->
                existingEntities.stream().noneMatch(entity -> uuid.equals(entity.getUuid())));
    }
}
