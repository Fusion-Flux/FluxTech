package com.fusionflux.fluxtech.teb.objects;

import com.fusionflux.fluxtech.mixin.ServerChunkManagerInvoker;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AsyncWorldView {
    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private final Map<ChunkPos, Chunk> chunkCache = new HashMap<>();
    private final ServerWorld world;

    public AsyncWorldView(ServerWorld world) {
        this.world = world;
    }

    public static Optional<Chunk> getChunkAsync(ServerWorld world, int x, int z) {
        ServerChunkManagerInvoker chunkManager = (ServerChunkManagerInvoker) world.getChunkManager();
        Either<Chunk, ChunkHolder.Unloaded> either = chunkManager.fluxtech$callGetChunkFuture(x, z, ChunkStatus.FULL, false).join();
        return either.left();
    }

    public static BlockState getBlockAsync(ServerWorld world, BlockPos pos) {
        Optional<Chunk> chunkOptional = getChunkAsync(world, pos.getX() >> 4, pos.getZ() >> 4);
        if (!chunkOptional.isPresent()) return AIR;
        return chunkOptional.get().getBlockState(pos);
    }

    public BlockState getBlock(BlockPos pos) {
        Chunk chunk = getChunk(pos);
        if (chunk == null) {
            return AIR;
        }

        return chunk.getBlockState(pos);
    }

    public Chunk getChunk(BlockPos p) {
        return getChunk(new ChunkPos(p));
    }

    public Chunk getChunk(ChunkPos chunkPos) {
        Chunk chunk = chunkCache.get(chunkPos);
        if (chunk == null) {
            Optional<Chunk> chunkO = getChunkAsync(world, chunkPos.x, chunkPos.z);
            if (chunkO.isPresent()) {
                chunk = chunkO.get();
                chunkCache.put(chunkPos, chunk);
            } else {
                return null;
            }
        }
        return chunk;
    }
}
