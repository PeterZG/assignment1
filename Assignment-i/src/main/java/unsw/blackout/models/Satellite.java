package unsw.blackout.models;

import unsw.utils.Angle;

public abstract class Satellite extends Entity {
    public Satellite(String id, double height, Angle position) {
        super(id, height, position);
    }

    public abstract boolean support(Device device);

    @Override
    public boolean communicableWith(Entity e) {
        boolean supported = true;   // a satellite can communicate with any type of satellites
        if (e instanceof Device) {
            supported = this.support((Device) e);
        }
        return supported && super.communicableWith(e);
    }
}
