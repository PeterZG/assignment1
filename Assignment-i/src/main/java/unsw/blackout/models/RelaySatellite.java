package unsw.blackout.models;

import unsw.blackout.movement.RelayMoveStrategy;
import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    public RelaySatellite(String id, double height, Angle position) {
        super(id, height, position);
        setVelocity(1_500);
        setRange(300_000);
        setMoveStrategy(new RelayMoveStrategy());
        setDisk(new SpaceBandwidthLimitedDisk(0, 0, 0, 0));
    }

    @Override
    public boolean support(Device device) {
        return true;
    }
}
