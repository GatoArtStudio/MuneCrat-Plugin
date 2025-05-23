package com.github.gatoartstudios.munecraft.helpers;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class PasswordUtil {

    // Argon2 configuration (Spring Security 6.4.5)
    // saltLength: length of the salt in bytes
    // hashLength: desired length of the hash in bytes
    // parallelism: number of parallel threads
    // memory: amount of memory in kibibytes
    // iterations: number of passes (rounds)
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 1;
    private static final int MEMORY_KIB = 1 << 12;   // 4 096 KiB = 4 MB
    private static final int ITERATIONS = 3;

    // Shared and thread-safe encoder
    private static final Argon2PasswordEncoder encoder =
            new Argon2PasswordEncoder(
                    SALT_LENGTH,
                    HASH_LENGTH,
                    PARALLELISM,
                    MEMORY_KIB,
                    ITERATIONS
            );

    /**
     * Generates an Argon2id hash of the password.
     *
     * @param rawPassword The plain text password.
     * @return The encoded hash (includes salt, parameters, and hash) in Modular Crypto String format.
     */
    public static String hash(String rawPassword) {
        // Argon2PasswordEncoder uses Argon2id by default in Spring 6.4.5
        return encoder.encode(rawPassword);
    }

    /**
     * Verifies that the plain text password matches the stored hash.
     *
     * @param rawPassword The plain text password.
     * @param encodedHash The hash previously generated by hash().
     * @return true if they match; false otherwise.
     */
    public static boolean verify(String rawPassword, String encodedHash) {
        return encoder.matches(rawPassword, encodedHash);
    }
}
