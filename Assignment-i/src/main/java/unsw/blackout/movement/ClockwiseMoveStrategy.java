package unsw.blackout.movement;

import unsw.blackout.models.Entity;
import unsw.utils.Angle;

public class ClockwiseMoveStrategy extends DirectionalMoveStrategy {

    public ClockwiseMoveStrategy() {
        setClockwiseDirection();
    }

    @Override
    public Angle nextPosition(Entity e) {
        return super.nextPosition(e);
    }
}
