package unsw.blackout.movement;

import unsw.blackout.models.Entity;
import unsw.utils.Angle;

public class RelayMoveStrategy extends DirectionalMoveStrategy {

    @Override
    public Angle nextPosition(Entity e) {
        Angle old = e.getPosition();

        if (old.compareTo(Angle.fromDegrees(140)) == -1) {
            changeDirection();
        } else if (old.compareTo(Angle.fromDegrees(190)) == 1) {
            changeDirection();
        }

        if (old.compareTo(Angle.fromDegrees(345)) == 1) {
            setPositiveDirection();
        }

        return super.nextPosition(e);
    }
}
