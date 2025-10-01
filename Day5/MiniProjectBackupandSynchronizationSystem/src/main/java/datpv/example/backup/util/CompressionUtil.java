package datpv.example.backup.util;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * Simple gzip compression helper (reads input stream fully).
 * For streaming compression use GZIPOutputStream with streaming copy (omitted for brevity).
 */
public class CompressionUtil {
    public static void gzipCompress(InputStream in, OutputStream out) throws IOException {
        try (GZIPOutputStream gz = new GZIPOutputStream(out)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) gz.write(buf, 0, r);
            gz.finish();
        }
    }
}
