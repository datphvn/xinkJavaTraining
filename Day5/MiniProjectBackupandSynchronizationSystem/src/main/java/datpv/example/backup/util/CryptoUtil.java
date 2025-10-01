package datpv.example.backup.util;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;

/**
 * AES-GCM helper for stream encryption to bytes (demo).
 * In production, use proper IV per object and store it with ciphertext.
 */
public class CryptoUtil {
    private static final String ALGO = "AES/GCM/NoPadding";
    private static final int TAG_BITS = 128;

    public static byte[] encryptStreamToBytes(InputStream in, byte[] key) throws Exception {
        // use random IV
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec ks = new SecretKeySpec(key, "AES");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_BITS, iv);
        cipher.init(Cipher.ENCRYPT_MODE, ks, spec);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // prefix IV
        baos.write(iv);
        try (CipherOutputStream cos = new CipherOutputStream(baos, cipher)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) cos.write(buf, 0, r);
        }
        return baos.toByteArray();
    }

    public static InputStream decryptBytesToStream(byte[] cipherBytes, byte[] key) throws Exception {
        byte[] iv = new byte[12];
        System.arraycopy(cipherBytes, 0, iv, 0, iv.length);
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec ks = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, ks, new GCMParameterSpec(TAG_BITS, iv));
        ByteArrayInputStream bais = new ByteArrayInputStream(cipherBytes, iv.length, cipherBytes.length - iv.length);
        return new CipherInputStream(bais, cipher);
    }
}
