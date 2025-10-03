package com.BO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class PasswordUtil {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int SALT_BYTES = 16;

    // generate a new random salt (Base64)
    public static String generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        RAND.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash password with salt using SHA-256, return Base64(hash)
    public static String hashPassword(String password, String saltBase64) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            md.update(salt);
            byte[] hashed = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    // verify plain password against stored salt+hash
    public static boolean verifyPassword(String plain, String saltBase64, String storedHashBase64) {
        String h = hashPassword(plain, saltBase64);
        return h.equals(storedHashBase64);
    }
}
