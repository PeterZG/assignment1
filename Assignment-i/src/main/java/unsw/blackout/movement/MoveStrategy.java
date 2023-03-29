package unsw.blackout.movement;

import unsw.blackout.models.Entity;
import unsw.utils.Angle;

public interface MoveStrategy {
    public Angle nextPosition(Entity e);
}
