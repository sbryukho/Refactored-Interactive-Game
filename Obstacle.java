import processing.core.PImage;

import java.util.List;

public class Obstacle extends Animated {
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;

    public Obstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod);
    }

}
