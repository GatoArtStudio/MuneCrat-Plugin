package com.github.gatoartstudios.munecraft.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    void testHashingProducesDifferentOutput() {
        String hash1 = PasswordUtil.hash("superSecretPassword");
        String hash2 = PasswordUtil.hash("superSecretPassword");

        assertNotEquals(hash1, hash2, () -> "Hashes should not be equal when a password is generated with the same input.");
    }

    @Test
    void testPasswordValidation() {
        String password = "superSecretPassword";
        String hash = PasswordUtil.hash(password);

        assertTrue(PasswordUtil.verify(password, hash), () -> "The password could not be validated with PasswordUtil, which should not happen.");
    }
}
