package com.notif1ed.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PasswordUtils class.
 * Tests password hashing, verification, and strength validation.
 */
class PasswordUtilsTest {

    @Test
    @DisplayName("Should hash password successfully")
    void testHashPassword() {
        String plainPassword = "mySecurePass123";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);
        
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertNotEquals(plainPassword, hashedPassword, "Hashed password should differ from plain password");
        assertTrue(hashedPassword.startsWith("$2a$"), "BCrypt hash should start with $2a$");
    }

    @Test
    @DisplayName("Should verify correct password")
    void testCheckPasswordCorrect() {
        String plainPassword = "myPassword456";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);
        
        assertTrue(PasswordUtils.checkPassword(plainPassword, hashedPassword), 
                   "Should return true for correct password");
    }

    @Test
    @DisplayName("Should reject incorrect password")
    void testCheckPasswordIncorrect() {
        String correctPassword = "myPassword789";
        String wrongPassword = "wrongPassword";
        String hashedPassword = PasswordUtils.hashPassword(correctPassword);
        
        assertFalse(PasswordUtils.checkPassword(wrongPassword, hashedPassword), 
                    "Should return false for incorrect password");
    }

    @Test
    @DisplayName("Should accept strong password with letters and digits")
    void testIsPasswordStrong_Valid() {
        assertTrue(PasswordUtils.isPasswordStrong("password123"), 
                   "Password with 8+ chars, letter and digit should be strong");
        assertTrue(PasswordUtils.isPasswordStrong("MySecure123"), 
                   "Mixed case password with digit should be strong");
    }

    @Test
    @DisplayName("Should reject weak passwords")
    void testIsPasswordStrong_Invalid() {
        assertFalse(PasswordUtils.isPasswordStrong("short1"), 
                    "Password with less than 8 characters should be weak");
        assertFalse(PasswordUtils.isPasswordStrong("onlyletters"), 
                    "Password without digits should be weak");
        assertFalse(PasswordUtils.isPasswordStrong("12345678"), 
                    "Password without letters should be weak");
        assertFalse(PasswordUtils.isPasswordStrong(""), 
                    "Empty password should be weak");
    }

    @Test
    @DisplayName("Should generate different hashes for same password")
    void testHashPasswordGeneratesDifferentSalts() {
        String plainPassword = "testPassword123";
        String hash1 = PasswordUtils.hashPassword(plainPassword);
        String hash2 = PasswordUtils.hashPassword(plainPassword);
        
        assertNotEquals(hash1, hash2, 
                        "Same password should generate different hashes due to random salt");
        
        // But both should verify correctly
        assertTrue(PasswordUtils.checkPassword(plainPassword, hash1));
        assertTrue(PasswordUtils.checkPassword(plainPassword, hash2));
    }

    @Test
    @DisplayName("Should handle null password gracefully")
    void testCheckPasswordWithNull() {
        String hashedPassword = PasswordUtils.hashPassword("test123");
        
        assertFalse(PasswordUtils.checkPassword(null, hashedPassword), 
                    "Should return false for null password");
        assertFalse(PasswordUtils.isPasswordStrong(null), 
                    "Null password should not be strong");
    }
}
