package com.BO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class PasswordUtil {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int SALT_BYTES = 16;
    private static final String DELIMITER = ":";

    // Generate salt+hash combination stored as "salt:hash"
    public static String generateSaltedHash(String password) {
        try {
            // Generate salt
            byte[] salt = new byte[SALT_BYTES];
            RAND.nextBytes(salt);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);

            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashed = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String hashBase64 = Base64.getEncoder().encodeToString(hashed);

            // Return combined string
            return saltBase64 + DELIMITER + hashBase64;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    // Verify plain password against stored salt:hash combination
    public static boolean verifyPassword(String plainPassword, String storedSaltedHash) {
        if (storedSaltedHash == null || !storedSaltedHash.contains(DELIMITER)) {
            return false;
        }

        String[] parts = storedSaltedHash.split(DELIMITER, 2);
        if (parts.length != 2) {
            return false;
        }

        String saltBase64 = parts[0];
        String storedHash = parts[1];

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            md.update(salt);
            byte[] hashed = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            String computedHash = Base64.getEncoder().encodeToString(hashed);

            return computedHash.equals(storedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}