package unsw.blackout.movement;

import unsw.blackout.models.Entity;
import unsw.utils.Angle;

public class TeleportingMoveStrategy extends DirectionalMoveStrategy {
    public TeleportingMoveStrategy() {
        setPositiveDirection();
    }

    @Override
    public Angle nextPosition(Entity e) {
        Angle position = super.nextPosition(e);

        if ((int) position.toDegrees() == 180) {
            changeDirection();
            return Angle.fromDegrees(0);
        }

        return position;
    }
}
