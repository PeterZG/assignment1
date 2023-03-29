package unsw.blackout.models;

public class Slope {
    private int start;
    private int end;
    private int gradient;

    public Slope(int startAngle, int endAngle, int gradient) {
        this.start = startAngle;
        this.end = endAngle;
        this.gradient = gradient;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getGradient() {
        return this.gradient;
    }
}
