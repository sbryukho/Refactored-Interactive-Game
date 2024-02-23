import processing.core.PImage;

import java.util.*;
import java.util.function.Predicate;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities;

    public WorldModel() {

    }

    public Background[][] getBackground() {
        return background;
    }

    public void setOccupancy(Entity[][] occupancy) {
        this.occupancy = occupancy;
    }

    public void setBackground(Background[][] background) {
        this.background = background;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public void setEntities(Set<Entity> entities) {
        this.entities = entities;
    }

    public void tryAddEntity(Entity entity) {
        if (this.isOccupied( entity.getPosition())) {
            throw new IllegalArgumentException("position occupied");
        }
        addEntity(entity);
    }

    public boolean withinBounds( Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0 && pos.getX() < this.numCols;
    }

    public boolean isOccupied( Point pos) {
        return this.withinBounds( pos) && this.getOccupancyCell( pos) != null;
    }

    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public Optional<Entity> findNearest(Point pos, Predicate<Entity> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities) {
            if (kinds.test(entity)) {
                ofType.add(entity);
            }
        }
        return nearestEntity(ofType, pos);
    }

    public int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.getX() - p2.getX();
        int deltaY = p1.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

    public void addEntity(Entity entity) {
        if (this.withinBounds( entity.getPosition())) {
            this.setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity( EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds( pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell( oldPos, null);
            Optional<Entity> occupant = this.getOccupant( pos);
            occupant.ifPresent(target -> this.removeEntity(scheduler, target));
            this.setOccupancyCell( pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity( EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents( entity);
        this.removeEntityAt( entity.getPosition());
    }

    public void removeEntityAt( Point pos) {
        if (this.withinBounds( pos) && this.getOccupancyCell( pos) != null) {
            Entity entity = this.getOccupancyCell( pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell( pos, null);
        }
    }

    public Optional<Entity> getOccupant( Point pos) {
        if (this.isOccupied( pos)) {
            return Optional.of(this.getOccupancyCell( pos));
        } else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell( Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public  void setOccupancyCell( Point pos, Entity entity) {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    public  void load( Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        Parseable.parseSaveFile(this, saveFile, imageStore, defaultBackground);
        if(this.background == null){
            this.background = new Background[this.numRows][this.numCols];
            for (Background[] row : this.background)
                Arrays.fill(row, defaultBackground);
        }
        if(this.occupancy == null){
            this.occupancy = new Entity[this.numRows][this.numCols];
            this.entities = new HashSet<>();
        }
    }

    public Background getBackgroundCell( Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    public void setBackgroundCell( Point pos, Background background) {
        this.background[pos.getY()][pos.getX()] = background;
    }


    public Optional<PImage> getBackgroundImage( Point pos) {
        if (this.withinBounds(pos)) {
            return Optional.of(Entity.getCurrentImage(this.getBackgroundCell( pos)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }
}
