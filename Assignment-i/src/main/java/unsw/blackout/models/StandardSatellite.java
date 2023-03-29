package unsw.blackout.models;

import unsw.blackout.movement.ClockwiseMoveStrategy;
import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    public StandardSatellite(String id, double height, Angle position) {
        super(id, height, position);
        setVelocity(2_500);
        setRange(150_000);
        setMoveStrategy(new ClockwiseMoveStrategy());
        setDisk(new SpaceBandwidthLimitedDisk(3, 80, 1, 1));
    }

    @Override
    public boolean support(Device device) {
        if (device instanceof HandheldDevice) {
            return true;
        }
        if (device instanceof LaptopDevice) {
            return true;
        }
        return false;
    }

}
