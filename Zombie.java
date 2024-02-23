import processing.core.PImage;

import java.util.List;

public class Zombie extends Entity{
    public static final String ZOMBIE_KEY = "zombie";
    public Zombie(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
    public static Entity createZombie(String id, Point position, List<PImage> images) {
        return new House(id, position, images);
    }

}