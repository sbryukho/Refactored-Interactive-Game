import processing.core.PImage;

import java.util.List;

public abstract class Actionized extends Animated {
    protected double actionPeriod;
    public Actionized(String id, Point position, List<PImage> images, double actionPeriod, double AnimationPeriod) {
        super(id, position, images, AnimationPeriod);
        this.actionPeriod = actionPeriod;


    }
    public double getActionPeriod() {
        return actionPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.actionPeriod);
    }






}
