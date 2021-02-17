package com.fusionflux.fluxtech.mixin;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity{

    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected abstract boolean willHitBlockAt(BlockPos pos);

    @Shadow public @Nullable abstract Vec3d snapPositionToRail(double x, double y, double z);

    @Shadow
    protected abstract void applySlowdown();

    @Shadow
    protected abstract double getMaxOffRailSpeed();


    @Shadow
    private static Pair<Vec3i, Vec3i> getAdjacentRailPositionsByShape(RailShape shape) {
        return null;
    }

    @Inject(method = "moveOnRail", at = @At("RETURN"), cancellable = true)
    public void moveOnRail(BlockPos pos, BlockState state, CallbackInfo ci){
        AbstractRailBlock abstractRailBlock = (AbstractRailBlock)state.getBlock();
        if (abstractRailBlock == FluxTechBlocks.RAILTEST) {
            this.setVelocity(this.getVelocity().multiply(2.0D, 0.0D, 2.0D));
        }
    }


    /*
    public void moveOnRail3(BlockPos pos, BlockState state) {
        this.fallDistance = 0.0F;
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        Vec3d vec3d = this.snapPositionToRail(d, e, f);
        e = (double)pos.getY();
        boolean bl = false;
        boolean bl2 = false;
        AbstractRailBlock abstractRailBlock = (AbstractRailBlock)state.getBlock();
        if (abstractRailBlock == Blocks.POWERED_RAIL) {
            bl = (Boolean)state.get(PoweredRailBlock.POWERED);
            bl2 = !bl;
        }

        double g = 0.0078125D;
        Vec3d vec3d2 = this.getVelocity();
        RailShape railShape = (RailShape)state.get(abstractRailBlock.getShapeProperty());
        switch(railShape) {
            case ASCENDING_EAST:
                this.setVelocity(vec3d2.add(-0.0078125D, 0.0D, 0.0D));
                ++e;
                break;
            case ASCENDING_WEST:
                this.setVelocity(vec3d2.add(0.0078125D, 0.0D, 0.0D));
                ++e;
                break;
            case ASCENDING_NORTH:
                this.setVelocity(vec3d2.add(0.0D, 0.0D, 0.0078125D));
                ++e;
                break;
            case ASCENDING_SOUTH:
                this.setVelocity(vec3d2.add(0.0D, 0.0D, -0.0078125D));
                ++e;
        }

        vec3d2 = this.getVelocity();
        Pair<Vec3i, Vec3i> pair = getAdjacentRailPositionsByShape(railShape);
        Vec3i vec3i = (Vec3i)pair.getFirst();
        Vec3i vec3i2 = (Vec3i)pair.getSecond();
        double h = (double)(vec3i2.getX() - vec3i.getX());
        double i = (double)(vec3i2.getZ() - vec3i.getZ());
        double j = Math.sqrt(h * h + i * i);
        double k = vec3d2.x * h + vec3d2.z * i;
        if (k < 0.0D) {
            h = -h;
            i = -i;
        }

        double l = Math.min(2.0D, Math.sqrt(squaredHorizontalLength(vec3d2)));
        vec3d2 = new Vec3d(l * h / j, vec3d2.y, l * i / j);
        this.setVelocity(vec3d2);
        Entity entity = this.getPassengerList().isEmpty() ? null : (Entity)this.getPassengerList().get(0);
        if (entity instanceof PlayerEntity) {
            Vec3d vec3d3 = entity.getVelocity();
            double m = squaredHorizontalLength(vec3d3);
            double n = squaredHorizontalLength(this.getVelocity());
            if (m > 1.0E-4D && n < 0.01D) {
                this.setVelocity(this.getVelocity().add(vec3d3.x * 0.1D, 0.0D, vec3d3.z * 0.1D));
                bl2 = false;
            }
        }

        double p;
        if (bl2) {
            p = Math.sqrt(squaredHorizontalLength(this.getVelocity()));
            if (p < 0.03D) {
                this.setVelocity(Vec3d.ZERO);
            } else {
                this.setVelocity(this.getVelocity().multiply(0.5D, 0.0D, 0.5D));
            }
        }

        p = (double)pos.getX() + 0.5D + (double)vec3i.getX() * 0.5D;
        double q = (double)pos.getZ() + 0.5D + (double)vec3i.getZ() * 0.5D;
        double r = (double)pos.getX() + 0.5D + (double)vec3i2.getX() * 0.5D;
        double s = (double)pos.getZ() + 0.5D + (double)vec3i2.getZ() * 0.5D;
        h = r - p;
        i = s - q;
        double x;
        double v;
        double w;
        if (h == 0.0D) {
            x = f - (double)pos.getZ();
        } else if (i == 0.0D) {
            x = d - (double)pos.getX();
        } else {
            v = d - p;
            w = f - q;
            x = (v * h + w * i) * 2.0D;
        }

        d = p + h * x;
        f = q + i * x;
        this.updatePosition(d, e, f);
        v = this.hasPassengers() ? 0.75D : 1.0D;
        w = this.getMaxOffRailSpeed();
        vec3d2 = this.getVelocity();
        this.move(MovementType.SELF, new Vec3d(MathHelper.clamp(v * vec3d2.x, -w, w), 0.0D, MathHelper.clamp(v * vec3d2.z, -w, w)));
        if (vec3i.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == vec3i.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == vec3i.getZ()) {
            this.updatePosition(this.getX(), this.getY() + (double)vec3i.getY(), this.getZ());
        } else if (vec3i2.getY() != 0 && MathHelper.floor(this.getX()) - pos.getX() == vec3i2.getX() && MathHelper.floor(this.getZ()) - pos.getZ() == vec3i2.getZ()) {
            this.updatePosition(this.getX(), this.getY() + (double)vec3i2.getY(), this.getZ());
        }

        this.applySlowdown();
        Vec3d vec3d4 = this.snapPositionToRail(this.getX(), this.getY(), this.getZ());
        Vec3d vec3d7;
        double af;
        if (vec3d4 != null && vec3d != null) {
            double aa = (vec3d.y - vec3d4.y) * 0.05D;
            vec3d7 = this.getVelocity();
            af = Math.sqrt(squaredHorizontalLength(vec3d7));
            if (af > 0.0D) {
                this.setVelocity(vec3d7.multiply((af + aa) / af, 1.0D, (af + aa) / af));
            }

            this.updatePosition(this.getX(), vec3d4.y, this.getZ());
        }

        int ac = MathHelper.floor(this.getX());
        int ad = MathHelper.floor(this.getZ());
        if (ac != pos.getX() || ad != pos.getZ()) {
            vec3d7 = this.getVelocity();
            af = Math.sqrt(squaredHorizontalLength(vec3d7));
            this.setVelocity(af * (double)(ac - pos.getX()), vec3d7.y, af * (double)(ad - pos.getZ()));
        }

        if (bl) {
            vec3d7 = this.getVelocity();
            af = Math.sqrt(squaredHorizontalLength(vec3d7));
            if (af > 0.01D) {
                double ag = 0.06D;
                this.setVelocity(vec3d7.add(vec3d7.x / af * 0.06D, 0.0D, vec3d7.z / af * 0.06D));
            } else {
                Vec3d vec3d8 = this.getVelocity();
                double ah = vec3d8.x;
                double ai = vec3d8.z;
                if (railShape == RailShape.EAST_WEST) {
                    if (this.willHitBlockAt(pos.west())) {
                        ah = 0.02D;
                    } else if (this.willHitBlockAt(pos.east())) {
                        ah = -0.02D;
                    }
                } else {
                    if (railShape != RailShape.NORTH_SOUTH) {
                        return;
                    }

                    if (this.willHitBlockAt(pos.north())) {
                        ai = 0.02D;
                    } else if (this.willHitBlockAt(pos.south())) {
                        ai = -0.02D;
                    }
                }

                this.setVelocity(ah, vec3d8.y, ai);
            }
        }

    }
*/

}
