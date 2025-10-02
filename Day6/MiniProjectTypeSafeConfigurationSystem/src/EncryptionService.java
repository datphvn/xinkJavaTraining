public class EncryptionService {
    public static String encrypt(String value) {
        return "ENCRYPTED(" + value + ")";
    }

    public static String decrypt(String value) {
        return value.replace("ENCRYPTED(", "").replace(")", "");
    }
}
