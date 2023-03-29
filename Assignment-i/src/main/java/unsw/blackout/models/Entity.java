package unsw.blackout.models;

import java.util.Map;

import unsw.blackout.movement.MoveStrategy;
import unsw.blackout.movement.MovelessStrategy;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Entity {
    private String id;
    private double height;
    private Angle positon;

    private int range = 0;
    private int velocity = 0;
    private MoveStrategy moveStrategy = new MovelessStrategy();
    private Disk disk = new Disk();

    public Entity(String id, double height, Angle position) {
        this.id = id;
        this.height = height;
        this.positon = position;
    }

    protected void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    /* Notes: Only support add complete file */
    public void addFile(String fileName, String content) {
        this.disk.addFile(fileName, content);
    }

    public File getFile(String fileName) {
        return this.disk.getFile(fileName);
    }

    public Disk getDisk() {
        return this.disk;
    }

    protected void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    protected void setRange(int range) {
        this.range = range;
    }

    protected void setDisk(Disk disk) {
        this.disk = disk;
    }

    public String getId() {
        return id;
    }

    public double getHeight() {
        return height;
    }

    protected void setHeight(double height) {
        this.height = height;
    }

    public Angle getPosition() {
        return positon;
    }

    public int getVelocity() {
        return velocity;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public Map<String, FileInfoResponse> getFilesInfo() {
        return disk.getFilesInfo();
    }

    public Angle nextPosition() {
        return moveStrategy.nextPosition(this);
    }

    public void move() {
        this.positon = nextPosition();
    }

    public void next() {
        move();
    }

    public boolean visible(Entity e) {
        return MathsHelper.isVisible(height, positon, e.getHeight(), e.getPosition());
    }

    public double distanceWith(Entity e) {
        return MathsHelper.getDistance(height, positon, e.getHeight(), e.getPosition());
    }

    public boolean communicableWith(Entity e) {
        return visible(e) && this.distanceWith(e) < this.range;
    }

    public static Entity createEntity(String id, String type, double height, Angle position) {
        try {
            Class<?> cls = Class.forName(Entity.class.getPackageName() + "." + type);
            return (Entity) cls.getConstructors()[0].newInstance(id, height, position);
        } catch (Exception e) {
            return new UnimplementedEntity("Unimplemented Entity", 0, Angle.fromRadians(0));
        }
    }
}
