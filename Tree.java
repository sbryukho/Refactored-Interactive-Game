import processing.core.PImage;

import java.util.List;

public class Tree extends Plant{



    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD = 0;
    public static final int TREE_ACTION_PERIOD = 1;
    public static final int TREE_HEALTH = 2;
    public static final int TREE_NUM_PROPERTIES = 3;

    private static final double TREE_ANIMATION_MAX = 0.600;
    private static final double TREE_ANIMATION_MIN = 0.050;
    private static final double TREE_ACTION_MAX = 1.400;
    private static final double TREE_ACTION_MIN = 1.000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    public static String getTreeKey() {
        return TREE_KEY;
    }

    public static int getTreeAnimationPeriod() {
        return TREE_ANIMATION_PERIOD;
    }

    public static int getTreeActionPeriod() {
        return TREE_ACTION_PERIOD;
    }

    public static int getTreeHealth() {
        return TREE_HEALTH;
    }

    public static int getTreeNumProperties() {
        return TREE_NUM_PROPERTIES;
    }

    public static double getTreeAnimationMax() {
        return TREE_ANIMATION_MAX;
    }

    public static double getTreeAnimationMin() {
        return TREE_ANIMATION_MIN;
    }

    public static double getTreeActionMax() {
        return TREE_ACTION_MAX;
    }

    public static double getTreeActionMin() {
        return TREE_ACTION_MIN;
    }

    public static int getTreeHealthMax() {
        return TREE_HEALTH_MAX;
    }

    public static int getTreeHealthMin() {
        return TREE_HEALTH_MIN;
    }

    public Tree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod, health, 0);
    }

    public static Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, actionPeriod, animationPeriod, health, images);
    }
    public boolean transform( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity( stump);

            return true;
        }

        return false;
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!this.transform( world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }



} // END OF CLASS
