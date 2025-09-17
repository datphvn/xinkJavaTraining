package detection;

import metrics.SlidingWindowStats;

public class AnomalyDetector {
    public boolean isAnomalous(SlidingWindowStats stats) {
        return stats.getAverage() > 40; // ví dụ: ngưỡng bất thường
    }
}
