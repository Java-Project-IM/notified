package com.notif1ed.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for secure password operations using BCrypt hashing.
 * 
 * BCrypt is a password hashing function designed to be slow, making brute-force
 * attacks computationally expensive. It automatically handles salt generation.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class PasswordUtils {
    
    // BCrypt work factor (12 = 2^12 iterations, good balance of security and performance)
    private static final int WORK_FACTOR = 12;
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private PasswordUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Hashes a plain text password using BCrypt.
     * 
     * @param plainPassword the plain text password to hash
     * @return the hashed password with salt
     * @throws IllegalArgumentException if plainPassword is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
    }
    
    /**
     * Verifies a plain text password against a hashed password.
     * 
     * @param plainPassword the plain text password to verify
     * @param hashedPassword the hashed password to check against
     * @return true if the password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }
    
    /**
     * Checks if a password meets minimum security requirements.
     * 
     * Requirements:
     * - At least 8 characters long
     * - Contains at least one digit
     * - Contains at least one letter
     * 
     * @param password the password to validate
     * @return true if password meets requirements, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        
        return hasDigit && hasLetter;
    }
}
