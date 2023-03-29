package unsw.blackout.models;

public class SpaceBandwidthLimitedDisk extends SpaceLimitedDisk {
    private final int maxSendBandwidth;
    private final int maxReceiveBandwidth;

    public SpaceBandwidthLimitedDisk(int maxFiles, int maxStorage, int maxSendBandwidth, int maxReceiveBandwidth) {
        super(maxFiles, maxStorage);
        this.maxSendBandwidth = maxSendBandwidth;
        this.maxReceiveBandwidth = maxReceiveBandwidth;
    }

    public int getMaxSendBandwidth() {
        return maxSendBandwidth;
    }

    public int getMaxReceiveBandwidth() {
        return maxReceiveBandwidth;
    }
}
