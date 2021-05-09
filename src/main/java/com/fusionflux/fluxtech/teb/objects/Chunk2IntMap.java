package com.fusionflux.fluxtech.teb.objects;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;

import static com.fusionflux.fluxtech.teb.objects.BlockCache.CHUNK_SIZE;
import static com.fusionflux.fluxtech.teb.objects.BlockCache.DEFAULT_MAP_SIZE;

public class Chunk2IntMap {
    private final Int2ObjectMap<Int2IntMap> map = new Int2ObjectOpenHashMap<>(DEFAULT_MAP_SIZE);
    private int total = 0;

    public Int2IntMap getSlice(int chunkX) {
        return map.get(chunkX);
    }

    public void increment(BlockPos p) {
        Int2IntMap chunkSlice = map.get(p.getX() >> CHUNK_SIZE);
        if (chunkSlice == null) {
            chunkSlice = new Int2IntOpenHashMap(DEFAULT_MAP_SIZE);
            chunkSlice.defaultReturnValue(0);
            map.put(p.getX() >> CHUNK_SIZE, chunkSlice);
        }

        chunkSlice.put(p.getZ() >> CHUNK_SIZE, chunkSlice.get(p.getZ() >> CHUNK_SIZE) + 1);
        total++;
    }

    public int getTotal() {
        return total;
    }
}
