import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ghost extends Actionized implements Move {
    public static final String GHOST_KEY = "ghost";
    public static final int GHOST_ANIMATION_PERIOD = 0;
    public static final int GHOST_ACTION_PERIOD = 1;
    public static final int GHOST_NUM_PROPERTIES = 2;

    public Ghost(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

//    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        scheduler.scheduleEvent(this, new AnimationAction(this, 0), this.getAnimationPeriod());
//    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> ghostTarget = world.findNearest(this.getPosition(), ((entity -> entity instanceof AbstractDude)));

        if (ghostTarget.isPresent()) { //ghost target is zombie
            Point tgtPos = ghostTarget.get().getPosition();

            if (this.moveTo(world, ghostTarget.get(), scheduler)) {
                Entity zombie = Zombie.createZombie("zombie", tgtPos, imageStore.getImageList(Zombie.ZOMBIE_KEY));

//                Zombie zombie = new Zombie(Zombie.ZOMBIE_KEY + ghostTarget.get().getId(), tgtPos, imageStore.getImageList(Zombie.ZOMBIE_KEY));
                world.addEntity(zombie);
            }
            scheduler.scheduleEvent(this,
                    ActivityAction.createActivityAction(this,world, imageStore),
                    this.getActionPeriod());
        }
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPosition(world, (target).getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

}

