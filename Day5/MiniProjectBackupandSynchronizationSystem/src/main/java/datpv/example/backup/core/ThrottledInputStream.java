package datpv.example.backup.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * Simple throttled InputStream: sleeps to enforce bytes-per-second.
 */
public class ThrottledInputStream extends InputStream {
    private final InputStream in;
    private final long bytesPerSecond; // 0 = no throttle
    private long bytesReadInWindow = 0;
    private long windowStart = System.nanoTime();

    public ThrottledInputStream(InputStream in, long bytesPerSecond) {
        this.in = in;
        this.bytesPerSecond = bytesPerSecond;
    }

    @Override
    public int read() throws IOException {
        throttle(1);
        int b = in.read();
        if (b != -1) bytesReadInWindow++;
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int toRead = len;
        if (bytesPerSecond > 0) {
            long now = System.nanoTime();
            long elapsedNanos = now - windowStart;
            double allowed = (bytesPerSecond * (elapsedNanos / 1_000_000_000.0)) - bytesReadInWindow;
            if (allowed <= 0) {
                // sleep small amount
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            } else {
                toRead = (int) Math.min(len, Math.max(1, (long) allowed));
            }
        }
        int r = in.read(b, off, toRead);
        if (r > 0) bytesReadInWindow += r;
        // window reset
        if (System.nanoTime() - windowStart > 1_000_000_000L) {
            windowStart = System.nanoTime();
            bytesReadInWindow = 0;
        }
        return r;
    }

    private void throttle(int want) throws IOException {
        if (bytesPerSecond <= 0) return;
        // naive single-byte throttle
        if (bytesReadInWindow >= bytesPerSecond) {
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            bytesReadInWindow = 0;
            windowStart = System.nanoTime();
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
