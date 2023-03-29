package unsw.blackout.models;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
    public DesktopDevice(String id, double height, Angle position) {
        super(id, height, position);
        setRange(200_000);
    }
}
