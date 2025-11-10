package com.notif1ed.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationUtils class.
 * Tests email, student number, and field validation.
 */
class ValidationUtilsTest {

    @Test
    @DisplayName("Should validate correct email addresses")
    void testIsValidEmail_Valid() {
        assertTrue(ValidationUtils.isValidEmail("user@example.com"));
        assertTrue(ValidationUtils.isValidEmail("john.doe@company.co.uk"));
        assertTrue(ValidationUtils.isValidEmail("test+tag@email.org"));
        assertTrue(ValidationUtils.isValidEmail("user123@test-domain.com"));
    }

    @Test
    @DisplayName("Should reject invalid email addresses")
    void testIsValidEmail_Invalid() {
        assertFalse(ValidationUtils.isValidEmail("notanemail"));
        assertFalse(ValidationUtils.isValidEmail("@example.com"));
        assertFalse(ValidationUtils.isValidEmail("user@"));
        assertFalse(ValidationUtils.isValidEmail("user @example.com"));
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    @DisplayName("Should validate correct student numbers")
    void testIsValidStudentNumber_Valid() {
        assertTrue(ValidationUtils.isValidStudentNumber("21-0001"));
        assertTrue(ValidationUtils.isValidStudentNumber("2021-123"));
        assertTrue(ValidationUtils.isValidStudentNumber("23-9999"));
        assertTrue(ValidationUtils.isValidStudentNumber("2023-0001"));
    }

    @Test
    @DisplayName("Should reject invalid student numbers")
    void testIsValidStudentNumber_Invalid() {
        assertFalse(ValidationUtils.isValidStudentNumber("1-1"));
        assertFalse(ValidationUtils.isValidStudentNumber("ABC-123"));
        assertFalse(ValidationUtils.isValidStudentNumber("21-12"));
        assertFalse(ValidationUtils.isValidStudentNumber("2021"));
        assertFalse(ValidationUtils.isValidStudentNumber(""));
        assertFalse(ValidationUtils.isValidStudentNumber(null));
    }

    @Test
    @DisplayName("Should detect all fields not empty")
    void testAreFieldsNotEmpty_AllFilled() {
        assertTrue(ValidationUtils.areFieldsNotEmpty("name", "email", "password"));
        assertTrue(ValidationUtils.areFieldsNotEmpty("value"));
    }

    @Test
    @DisplayName("Should detect empty or null fields")
    void testAreFieldsNotEmpty_WithEmpty() {
        assertFalse(ValidationUtils.areFieldsNotEmpty("name", "", "password"));
        assertFalse(ValidationUtils.areFieldsNotEmpty("name", null, "password"));
        assertFalse(ValidationUtils.areFieldsNotEmpty("   ", "email"));
        assertFalse(ValidationUtils.areFieldsNotEmpty(""));
    }

    @Test
    @DisplayName("Should sanitize input by trimming whitespace")
    void testSanitizeInput() {
        assertEquals("test", ValidationUtils.sanitizeInput("  test  "));
        assertEquals("value", ValidationUtils.sanitizeInput("value"));
        assertEquals("", ValidationUtils.sanitizeInput("   "));
        assertEquals("", ValidationUtils.sanitizeInput(""));
    }

    @Test
    @DisplayName("Should handle null input in sanitize")
    void testSanitizeInput_Null() {
        assertNull(ValidationUtils.sanitizeInput(null));
    }

    @Test
    @DisplayName("Should validate correct names")
    void testIsValidName_Valid() {
        assertTrue(ValidationUtils.isValidName("John Doe"));
        assertTrue(ValidationUtils.isValidName("María García"));
        assertTrue(ValidationUtils.isValidName("O'Brien"));
        assertTrue(ValidationUtils.isValidName("Jean-Pierre"));
    }

    @Test
    @DisplayName("Should reject invalid names")
    void testIsValidName_Invalid() {
        assertFalse(ValidationUtils.isValidName("J"));
        assertFalse(ValidationUtils.isValidName("123"));
        assertFalse(ValidationUtils.isValidName("Name@123"));
        assertFalse(ValidationUtils.isValidName(""));
        assertFalse(ValidationUtils.isValidName(null));
    }

    @Test
    @DisplayName("Should validate correct sections")
    void testIsValidSection_Valid() {
        assertTrue(ValidationUtils.isValidSection("A"));
        assertTrue(ValidationUtils.isValidSection("Section 1"));
        assertTrue(ValidationUtils.isValidSection("Grade 10-A"));
    }

    @Test
    @DisplayName("Should reject invalid sections")
    void testIsValidSection_Invalid() {
        assertFalse(ValidationUtils.isValidSection(""));
        assertFalse(ValidationUtils.isValidSection("   "));
        assertFalse(ValidationUtils.isValidSection(null));
    }

    @Test
    @DisplayName("Should validate correct subject codes")
    void testIsValidSubjectCode_Valid() {
        assertTrue(ValidationUtils.isValidSubjectCode("CS101"));
        assertTrue(ValidationUtils.isValidSubjectCode("MATH-202"));
        assertTrue(ValidationUtils.isValidSubjectCode("ENG1A"));
    }

    @Test
    @DisplayName("Should reject invalid subject codes")
    void testIsValidSubjectCode_Invalid() {
        assertFalse(ValidationUtils.isValidSubjectCode("A"));
        assertFalse(ValidationUtils.isValidSubjectCode(""));
        assertFalse(ValidationUtils.isValidSubjectCode(null));
    }
}
