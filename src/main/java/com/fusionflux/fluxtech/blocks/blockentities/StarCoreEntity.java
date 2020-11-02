package com.fusionflux.fluxtech.blocks.blockentities;

import com.fusionflux.fluxtech.blocks.FluxTechBlocks;
import com.fusionflux.fluxtech.entity.IsRollingAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
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
            Box gravityBox2 = (new Box(this.pos)).expand(25);
            List<Entity> list = this.world.getEntitiesByClass(Entity.class, gravityBox, e -> !(e instanceof PlayerEntity && ((PlayerEntity) e).abilities.flying));
            List<Entity> listb = this.world.getEntitiesByClass(Entity.class, gravityBox2, null);

            for (Entity entity : list) {
                IsRollingAccessor accessor = ((IsRollingAccessor) entity);

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
                        entity.setVelocity(entity.getVelocity().add(0, -1 / decresey, 0));

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
                        accessor.setDirection(Direction.UP);
                    }

                    if (this.pos.getY() > entity.getPos().getY()) {
                        entity.setVelocity(entity.getVelocity().add(0, +1 / decresey, 0));

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
                        accessor.setDirection(Direction.DOWN);
                    }
                }

                if (testz >= testx && testz >= testy) {
                    double decresez = testz;

                    if (decresez < 0) {
                        decresez = 1;
                    }

                    if (this.pos.getZ() < entity.getPos().getZ()) {
                        entity.setVelocity(entity.getVelocity().add(0, 0, -1 / decresez));

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
                        accessor.setDirection(Direction.SOUTH);
                    }

                    if (this.pos.getZ() > entity.getPos().getZ()) {
                        entity.setVelocity(entity.getVelocity().add(0, 0, +1 / decresez));

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
                        entity.setVelocity(entity.getVelocity().add(-1 / decresex, 0, 0));

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
                        entity.setVelocity(entity.getVelocity().add(+1 / decresex, 0, 0));

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
