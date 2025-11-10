package com.notif1ed.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation operations.
 * Provides centralized validation logic for common input types.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class ValidationUtils {
    
    // Email validation regex (RFC 5322 simplified)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Student number format: YY-NNNN (e.g., 24-1681, 2021-003)
    private static final Pattern STUDENT_NUMBER_PATTERN = Pattern.compile(
        "^\\d{2,4}-\\d{3,4}$"
    );
    
    /**
     * Private constructor to prevent instantiation.
     */
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Validates an email address format.
     * 
     * @param email the email address to validate
     * @return true if email format is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates a student number format.
     * 
     * @param studentNumber the student number to validate
     * @return true if format is valid, false otherwise
     */
    public static boolean isValidStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        return STUDENT_NUMBER_PATTERN.matcher(studentNumber.trim()).matches();
    }
    
    /**
     * Checks if all provided fields are non-empty.
     * 
     * @param fields variable number of string fields to check
     * @return true if all fields are non-empty, false if any is null or empty
     */
    public static boolean areFieldsNotEmpty(String... fields) {
        if (fields == null || fields.length == 0) {
            return false;
        }
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Sanitizes input by trimming whitespace.
     * 
     * @param input the input string to sanitize
     * @return sanitized string, or null if input was null
     */
    public static String sanitizeInput(String input) {
        return input == null ? null : input.trim();
    }
    
    /**
     * Validates a name field (first name, last name, guardian name).
     * Must be at least 2 characters and contain only letters, spaces, hyphens, and apostrophes.
     * 
     * @param name the name to validate
     * @return true if name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        // Must be at least 2 characters and allow letters, spaces, hyphens, apostrophes, accented characters
        return trimmed.length() >= 2 && trimmed.matches("^[a-zA-ZÀ-ÿ\\s'-]+$");
    }
    
    /**
     * Validates a section name (e.g., "BSIT 3A", "Grade 10-A").
     * 
     * @param section the section name to validate
     * @return true if section format is valid, false otherwise
     */
    public static boolean isValidSection(String section) {
        if (section == null || section.trim().isEmpty()) {
            return false;
        }
        // Allow letters, numbers, spaces, hyphens
        return section.matches("^[a-zA-Z0-9\\s-]+$") && section.length() <= 50;
    }
    
    /**
     * Validates a subject code (e.g., "CS101", "MATH-203").
     * Must be at least 2 characters.
     * 
     * @param code the subject code to validate
     * @return true if code format is valid, false otherwise
     */
    public static boolean isValidSubjectCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        String trimmed = code.trim();
        // Allow letters, numbers, hyphens, underscores, must be at least 2 chars
        return trimmed.length() >= 2 && trimmed.matches("^[a-zA-Z0-9_-]+$") && trimmed.length() <= 20;
    }
}
