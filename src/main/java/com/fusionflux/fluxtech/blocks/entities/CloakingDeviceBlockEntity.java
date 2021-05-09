package com.fusionflux.fluxtech.blocks.entities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

public class CloakingDeviceBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private int radius = 16;

    public CloakingDeviceBlockEntity() {
        super(FluxTechBlocks.CLOAKING_DEVICE_BLOCK_ENTITY);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int i) {
        this.radius = i;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.radius = tag.getInt("radius");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("radius", this.radius);
        return super.toTag(tag);
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(null, compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }
}
