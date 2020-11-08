package com.fusionflux.fluxtech.blocks.blockentities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.entity.IsRollingAccessor;
import com.fusionflux.fluxtech.mixin.EntityAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.List;

public class StarCoreEntity extends BlockEntity implements Tickable {

    public StarCoreEntity() {
        super(FluxTechBlocks.STAR_CORE_ENTITY);
    }

    @Override
    public void tick() {
        this.applyPlayerEffects();
    }


    private void applyPlayerEffects() {
        if (this.world != null && this.pos != null) {
            Box gravityBox = (new Box(this.pos)).expand(20);
            Box gravityBox2 = (new Box(this.pos)).expand(20+5);
            List<Entity> list = this.world.getEntitiesByClass(Entity.class, gravityBox, e -> !(e instanceof PlayerEntity && ((PlayerEntity) e).abilities.flying));
            List<Entity> listb = this.world.getEntitiesByClass(Entity.class, gravityBox2, null);

            for (Entity entity : list) {
                IsRollingAccessor accessor = ((IsRollingAccessor) entity);
                EntityAccessor dimention =((EntityAccessor) entity) ;

                Box boundingBox = entity.getBoundingBox();
                listb.remove(entity);
                entity.setNoGravity(true);

                double testy = this.pos.getY() - entity.getPos().getY();

                if (testy < 0) {
                    testy = testy * -1;
                }

                double testx = this.pos.getX() - entity.getPos().getX();

                if (testx < 0) {
                    testx = testx * -1;
                }

                double testz = this.pos.getZ() - entity.getPos().getZ();

                if (testz < 0) {
                    testz = testz * -1;
                }

                if (testy >= testx && testy >= testz) {
                    double decresey = testy;

                    if (decresey < 0) {
                        decresey = 1;
                    }

                    if (this.pos.getY() < entity.getPos().getY()) {
                        if(decresey>(20*.6666)) {
                           decresey=decresey-((20*.6666)-1);
                        entity.setVelocity(entity.getVelocity().add(0, -.07 / decresey, 0));
                        }else{
                            entity.setVelocity(entity.getVelocity().add(0, -0.07, 0));
                        }
                        double height = entity.getHeight();
                        double width = entity.getWidth();

                        if (height >= width) {
                            height = entity.getHeight();
                            width = entity.getWidth();
                        } else if (height <= width) {
                            height = entity.getWidth();
                            width = entity.getHeight();
                        }

                        Box oldBB = boundingBox.shrink(boundingBox.getXLength(), boundingBox.getYLength(), boundingBox.getZLength());
                        Box newBB = oldBB.expand(width / 2, height / 2, width / 2).offset(width / 2, height / 2, width / 2);
                        if (boundingBox != newBB) {
                            entity.setBoundingBox(newBB);
                        }


                        accessor.setRolling(true);
                        accessor.setDirection(Direction.UP);
                    }

                    if (this.pos.getY() > entity.getPos().getY()) {
                        if(decresey>(20*.6666)) {
                            decresey=decresey-((20*.6666)-1);
                            entity.setVelocity(entity.getVelocity().add(0, 0.07 / decresey, 0));
                        }else{
                            entity.setVelocity(entity.getVelocity().add(0, 0.07, 0));
                        }
                        if(entity.verticalCollision)
                        {
                            entity.setOnGround(true);
                        }
                        double height = entity.getHeight();
                        double width = entity.getWidth();

                        if (height >= width) {
                            height = entity.getHeight();
                            width = entity.getWidth();
                        } else if (height <= width) {
                            height = entity.getWidth();
                            width = entity.getHeight();
                        }

                        Box oldBB = boundingBox.shrink(boundingBox.getXLength(), boundingBox.getYLength(), boundingBox.getZLength());
                        Box newBB = oldBB.expand(width / 2, -height / 2, width / 2).offset(width / 2, height / 2, width / 2);

                        if (boundingBox != newBB) {
                            entity.setBoundingBox(newBB);
                        }

                        accessor.setRolling(true);
                        accessor.setDirection(Direction.DOWN);
                    }
                }

                if (testz >= testx && testz >= testy) {
                    double decresez = testz;

                    if (decresez < 0) {
                        decresez = 1;
                    }

                    if (this.pos.getZ() < entity.getPos().getZ()) {
                        if(decresez>(20*.6666)) {
                            decresez=decresez-((20*.6666)-1);
                            entity.setVelocity(entity.getVelocity().add(0, 0, -.07 / decresez));
                        }else{
                            entity.setVelocity(entity.getVelocity().add(0, 0, -0.07));
                        }
                        if(entity.horizontalCollision)
                        {
                            entity.setOnGround(true);
                        }
                        double height = entity.getHeight();
                        double width = entity.getWidth();
                        EntityDimensions oldDimensions = entity.getDimensions(EntityPose.STANDING);
                        EntityDimensions newDimensions = new EntityDimensions(oldDimensions.height, oldDimensions.width, false);
                        if (height >= width) {
                            height = entity.getHeight();
                            width = entity.getWidth();
                        } else if (height <= width) {
                            height = entity.getWidth();
                            width = entity.getHeight();
                        }

                        Box oldBB = boundingBox.shrink(boundingBox.getXLength(), boundingBox.getYLength(), boundingBox.getZLength());
                        Box newBB = oldBB.expand(-width / 2, -width / 2, -height / 2).offset(width / 2, width / 2, height / 2);


                        if (boundingBox != newBB) {
                            entity.setBoundingBox(newBB);
                            ((EntityAccessor) entity).setDimensions(newDimensions);
                            float newEyeHeight = ((EntityAccessor)entity).callGetEyeHeight(EntityPose.STANDING, newDimensions);
                            ((EntityAccessor)entity).setStandingEyeHeight(newEyeHeight);
                        }

                        accessor.setRolling(true);
                        accessor.setDirection(Direction.SOUTH);
                    }

                    if (this.pos.getZ() > entity.getPos().getZ()) {
                        if(decresez>(20*.6666)) {
                            decresez=decresez-((20*.6666)-1);
                            entity.setVelocity(entity.getVelocity().add(0, 0, 0.07 / decresez));
                        }else{
                            entity.setVelocity(entity.getVelocity().add(0, 0, 0.07));
                        }
                        if(entity.horizontalCollision)
                        {
                            entity.setOnGround(true);
                        }
                        double height = entity.getHeight();
                        double width = entity.getWidth();

                        if (height >= width) {
                            height = entity.getHeight();
                            width = entity.getWidth();
                        } else if (height <= width) {
                            height = entity.getWidth();
                            width = entity.getHeight();
                        }

                        Box oldBB = boundingBox.shrink(boundingBox.getXLength(), boundingBox.getYLength(), boundingBox.getZLength());
                        Box newBB = oldBB.expand(width / 2, width / 2, height / 2).offset(width / 2, width / 2, height / 2);

                        if (boundingBox != newBB) {
                            entity.setBoundingBox(newBB);
                        }

                        accessor.setRolling(true);
                        accessor.setDirection(Direction.NORTH);
                    }
                }

                if (testx >= testy && testx >= testz) {
                    double decresex = testx;

                    if (decresex < 0) {
                        decresex = 1;
                    }

                    if (this.pos.getX() < entity.getPos().getX()) {
                        if(decresex>(20*.6666)) {
                            decresex=decresex-((20*.6666)-1);
                            entity.setVelocity(entity.getVelocity().add(-0.07 / decresex, 0, 0));
                        }else{
                            entity.setVelocity(entity.getVelocity().add(-0.07, 0, 0));
                        }
                        if(entity.horizontalCollision)
                        {
                            entity.setOnGround(true);
                        }
                        double height = entity.getHeight();
                        double width = entity.getWidth();

                        if (height >= width) {
                            height = entity.getHeight();
                            width = entity.getWidth();
                        } else if (height <= width) {
                            height = entity.getWidth();
                            width = entity.getHeight();
                        }

                        Box oldBB = boundingBox.shrink(boundingBox.getXLength(), boundingBox.getYLength(), boundingBox.getZLength());
                        Box newBB = oldBB.expand(height / 2, width / 2, width / 2).offset(height / 2, width / 2, width / 2);

                        if (boundingBox != newBB) {
                            entity.setBoundingBox(newBB);
                        }

                        accessor.setRolling(true);
                        accessor.setDirection(Direction.EAST);
                    }

                    if (this.pos.getX() > entity.getPos().getX()) {
                        if(decresex>(20*.6666)) {
                            decresex=decresex-((20*.6666)-1);
                            entity.setVelocity(entity.getVelocity().add(0.07 / decresex, 0, 0));
                        }else{
                            entity.setVelocity(entity.getVelocity().add(0.07, 0, 0));
                        }
                        if(entity.horizontalCollision)
                        {
                            entity.setOnGround(true);
                        }
                        double height = entity.getHeight();
                        double width = entity.getWidth();

                        if (height >= width) {
                            height = entity.getHeight();
                            width = entity.getWidth();
                        } else if (height <= width) {
                            height = entity.getWidth();
                            width = entity.getHeight();
                        }

                        Box oldBB = boundingBox.shrink(boundingBox.getXLength(), boundingBox.getYLength(), boundingBox.getZLength());
                        Box newBB = oldBB.expand(height / 2, width / 2, width / 2).offset(height / 2, width / 2, width / 2);

                        if (boundingBox != newBB) {
                            entity.setBoundingBox(newBB);
                        }

                        accessor.setRolling(true);
                        accessor.setDirection(Direction.WEST);
                    }
                }
            }

            for (Entity entity : listb) {
                entity.setNoGravity(false);
                double height = entity.getHeight();
                double width = entity.getWidth();

                if (height >= width) {
                    height = entity.getHeight();
                    width = entity.getWidth();
                } else if (height <= width) {
                    height = entity.getWidth();
                    width = entity.getHeight();
                }

                Box oldBB = entity.getBoundingBox().shrink(entity.getBoundingBox().getXLength(), entity.getBoundingBox().getYLength(), entity.getBoundingBox().getZLength());
                Box newBB = oldBB.expand(width / 2, height / 2, width / 2).offset(width / 2, height / 2, width / 2);

                if (entity.getBoundingBox() != newBB) {
                    entity.setBoundingBox(newBB);
                }

                ((IsRollingAccessor) entity).setRolling(false);
            }
        }
    }
}
