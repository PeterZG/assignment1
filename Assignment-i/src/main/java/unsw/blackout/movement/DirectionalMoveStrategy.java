package unsw.blackout.movement;

import unsw.blackout.models.Entity;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class DirectionalMoveStrategy implements MoveStrategy {
    private int direction;

    public DirectionalMoveStrategy() {
        this.direction = MathsHelper.CLOCKWISE;
    }

    public void changeDirection() {
        if (this.direction == MathsHelper.ANTI_CLOCKWISE) {
            this.direction = MathsHelper.CLOCKWISE;
        } else {
            this.direction = MathsHelper.ANTI_CLOCKWISE;
        }
    }

    public void setPositiveDirection() {
        this.direction = MathsHelper.ANTI_CLOCKWISE;
    }

    public void setClockwiseDirection() {
        this.direction = MathsHelper.CLOCKWISE;
    }

    public int getDirection() {
        return this.direction;
    }

    @Override
    public Angle nextPosition(Entity e) {
        Angle old = e.getPosition();
        double height = e.getHeight();
        int velocity = e.getVelocity();

        if (this.direction == MathsHelper.ANTI_CLOCKWISE) {
            return old.add(Angle.fromRadians(velocity / height));
        } else {
            return old.subtract(Angle.fromRadians(velocity / height));
        }
    }
}
