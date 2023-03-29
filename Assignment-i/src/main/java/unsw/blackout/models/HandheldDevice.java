package unsw.blackout.models;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
    public HandheldDevice(String id, double height, Angle position) {
        super(id, height, position);
        setRange(50_000);
    }
}
