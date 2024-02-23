import processing.core.PImage;

import java.util.List;

public abstract class Entity {
    public String id;
    public Point position;
    public List<PImage> images;
    protected int imageIndex;

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }
    public static PImage getCurrentImage(Object object) {
        if (object instanceof Background background) {
            return background.getImages().get(background.getImageIndex());
        } else if (object instanceof Entity entity) {
            return entity.images.get(entity.imageIndex % entity.images.size());
        } else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not supported for %s", object));
        }
    }

    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public List<PImage> getImages() {
        return images;
    }
    public int getImageIndex() {
        return imageIndex;
    }
    public void setPosition(Point position) {
        this.position = position;
    }

    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }

}