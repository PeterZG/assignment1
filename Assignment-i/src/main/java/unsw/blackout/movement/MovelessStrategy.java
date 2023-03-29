package unsw.blackout.movement;

import unsw.blackout.models.Entity;
import unsw.utils.Angle;

public class MovelessStrategy implements MoveStrategy {
    @Override
    public Angle nextPosition(Entity e) {
        return e.getPosition();
    }
}
