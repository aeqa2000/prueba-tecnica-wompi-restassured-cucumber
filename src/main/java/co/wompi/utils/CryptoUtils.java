package co.wompi.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CryptoUtils {

    private CryptoUtils() {
    }

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte value : encodedHash) {
                hexString.append(String.format("%02x", value));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("No fue posible generar la firma SHA-256", exception);
        }
    }
}