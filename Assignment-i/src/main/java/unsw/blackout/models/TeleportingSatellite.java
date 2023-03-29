package unsw.blackout.models;

import unsw.blackout.movement.TeleportingMoveStrategy;
import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
    public TeleportingSatellite(String id, double height, Angle position) {
        super(id, height, position);
        setVelocity(1_000);
        setRange(200_000);
        setMoveStrategy(new TeleportingMoveStrategy());
        setDisk(new SpaceBandwidthLimitedDisk(Integer.MAX_VALUE, 200, 10, 15));
    }

    @Override
    public boolean support(Device device) {
        return true;
    }
}
