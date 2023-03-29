package unsw.blackout.models;

import java.util.List;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

import unsw.blackout.movement.ClockwiseMoveStrategy;

public class Device extends Entity {
    private boolean isMoving = false;
    private List<Slope> allSlopes = null;

    public Device(String id, double height, Angle position) {
        super(id, height, position);
    }

    public void setMoving(boolean isMoving, List<Slope> allSlopes) {
        this.isMoving = isMoving;
        this.allSlopes = allSlopes;
        setMoveStrategy(new ClockwiseMoveStrategy());
    }

    private Angle deltaTheta() {
        double h = getHeight();
        int v = getVelocity();
        return Angle.fromDegrees(v / h);
    }

    @Override
    public void move() {
        super.move();

        if (isMoving) {
            boolean inSlope = false;

            double height = MathsHelper.RADIUS_OF_JUPITER;
            for (Slope slop : allSlopes) {
                double currentPosition = this.getPosition().toDegrees();
                if (currentPosition < slop.getEnd() && currentPosition > slop.getStart()) {
                    inSlope = true;

                    double deltaH = deltaTheta().toRadians() * slop.getGradient();
                    height = Math.max(height + deltaH, height);
                }
            }

            if (!inSlope) {
                setHeight(MathsHelper.RADIUS_OF_JUPITER);
            } else {
                setHeight(height);
            }
        }
    }

    public void createFile(String fileName, String content) {
        addFile(fileName, content);
    }

}
