package com.BO;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class TokenUtil {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int TOKEN_BYTES = 32;
    private static final long EXPIRATION_TIME_MS = 30 * 60 * 1000; // 30 minutes

    // Simple in-memory token store (replace with DB in production)
    private static final ConcurrentHashMap<String, TokenEntry> tokenStore = new ConcurrentHashMap<>();

    public static class TokenEntry {
        public String username;
        public long expiry;
        TokenEntry(String username, long expiry) {
            this.username = username;
            this.expiry = expiry;
        }
    }

    public static String generateToken(String username) {
        byte[] tokenBytes = new byte[TOKEN_BYTES];
        RAND.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        long expiry = new Date().getTime() + EXPIRATION_TIME_MS;
        tokenStore.put(token, new TokenEntry(username, expiry));

        return token;
    }

    public static boolean validateToken(String token) {
        TokenEntry entry = tokenStore.get(token);
        if (entry == null) return false;
        if (new Date().getTime() > entry.expiry) {
            tokenStore.remove(token);
            return false;
        }
        return true;
    }

    public static String getUsername(String token) {
        TokenEntry entry = tokenStore.get(token);
        return (entry != null) ? entry.username : null;
    }
}
