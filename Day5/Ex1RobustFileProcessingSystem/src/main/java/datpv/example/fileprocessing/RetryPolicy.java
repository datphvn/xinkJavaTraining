package datpv.example.fileprocessing;

import java.util.concurrent.Callable;

public class RetryPolicy {
    public static <T> T executeWithRetry(Callable<T> task, int maxRetries, long baseDelayMillis) throws Exception {
        int attempts = 0;
        while (true) {
            try {
                return task.call();
            } catch (Exception e) {
                attempts++;
                if (attempts > maxRetries) throw e;
                long delay = (long) (baseDelayMillis * Math.pow(2, attempts));
                Thread.sleep(delay);
            }
        }
    }
}
