public class ActivityAction extends Action{
    private Actionized entity;
    private WorldModel world;
    private ImageStore imageStore;

    public ActivityAction(Actionized entity, WorldModel world, ImageStore imageStore){
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }



    @Override
    public void executeAction(EventScheduler scheduler) {
        entity.executeActivity(this.world, this.imageStore, scheduler);
 }
    public static ActivityAction createActivityAction(Actionized entity, WorldModel world, ImageStore imageStore) {
        return new ActivityAction(entity, world, imageStore);
    }



}

