package unsw.blackout.models;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String id, double height, Angle position) {
        super(id, height, position);
        setRange(100_000);
    }
}
