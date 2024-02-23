import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Sapling extends Plant {

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH = 0;
    public static final int SAPLING_NUM_PROPERTIES = 1;

    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final Random rand = new Random();


    public Sapling(String id, Point position, List<PImage> images, int health) {
        super(id, position, images, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, health, Sapling.SAPLING_HEALTH_LIMIT);
    }
    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList( Stump.STUMP_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity( stump);

            return true;
        } else if (this.health >= this.healthLimit) {
            Tree tree = new Tree(Tree.getTreeKey() + "_" + this.id, this.position, Sapling.getNumFromRange(Tree.getTreeActionMax(), Tree.getTreeActionMin()), Sapling.getNumFromRange(Tree.getTreeAnimationMax(), Tree.getTreeAnimationMin()), Sapling.getIntFromRange(Tree.getTreeHealthMax(), Tree.getTreeHealthMin()), imageStore.getImageList( Tree.getTreeKey()));

            world.removeEntity( scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions( scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        this.health++;
        if (!this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.actionPeriod);
        }

    }

    public static int getIntFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    public static double getNumFromRange(double max, double min) {
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }

}