package com.fusionflux.fluxtech.teb.objects;

import com.fusionflux.fluxtech.blocks.entities.CloakingDeviceBlockEntity;
import net.minecraft.util.math.*;

import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CloakedArea {
    //Right is defined as the most positive point in whatever axis this is
    private final BlockPos pos;
    private final int radius;

    public CloakedArea(BlockPos pos, int radius) {
        this.pos = pos.toImmutable();
        this.radius = radius;
    }

    public double getDistance(BlockPos pos) {
        return MathHelper.sqrt(this.pos.getSquaredDistance(pos));
    }

    public BlockPos getPos() {
        return this.pos;
    }

    /**
     * Returns true if this rectangle fully encloses b
     */
    public boolean contains(BlockPos blockPos) {
        return blockPos.isWithinDistance(this.pos, this.radius);
    }

    public BlockPos getUpperRight() {
        return this.pos.add(radius, radius, radius).toImmutable();
    }

    public BlockPos getLowerLeft() {
        return this.pos.add(-radius, -radius, -radius).toImmutable();
    }

    public void iterate(Consumer<BlockPos> consumer) {
        for (BlockPos pos : BlockPos.iterate(this.getLowerLeft(), this.getUpperRight())) {
            consumer.accept(pos);
        }
    }
}
