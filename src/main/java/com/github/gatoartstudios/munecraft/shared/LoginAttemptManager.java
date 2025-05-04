package com.github.gatoartstudios.munecraft.shared;

import java.util.HashMap;
import java.util.Map;

public class LoginAttemptManager {
    // Maximum number of allowed login attempts
    private static final int MAX_ATTEMPTS = 5;
    // Time in milliseconds to block a user after exceeding max attempts
    private static final long BLOCK_TIME_MS = 60_000;

    // Map to track login attempts by username
    private static final Map<String, Integer> attemptsByName = new HashMap<>();
    // Map to track block expiration times by username
    private static final Map<String, Long> blockUntil = new HashMap<>();

    // Check if a user is currently blocked
    public static boolean isBlocked(String name) {
        long now = System.currentTimeMillis();
        return blockUntil.getOrDefault(name.toLowerCase(), 0L) > now;
    }

    // Register a login attempt (success or failure)
    public static void registerAttempt(String name, boolean success) {
        name = name.toLowerCase();

        if (success) {
            // Clear attempts and block status on successful login
            attemptsByName.remove(name);
            blockUntil.remove(name);
        } else {
            // Increment the number of failed attempts
            int attempts = attemptsByName.getOrDefault(name, 0) + 1;
            if (attempts >= MAX_ATTEMPTS) {
                // Block the user if max attempts are reached
                blockUntil.put(name, System.currentTimeMillis() + BLOCK_TIME_MS);
                attemptsByName.remove(name);
            } else {
                // Update the number of failed attempts
                attemptsByName.put(name, attempts);
            }
        }
    }
}
