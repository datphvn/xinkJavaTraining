package metrics;

public class SlidingWindowStats {
    private double sum = 0;
    private int count = 0;

    public void update(DataPoint dp) {
        sum += dp.value;
        count++;
    }

    public double getAverage() {
        return (count == 0) ? 0 : sum / count;
    }
}
