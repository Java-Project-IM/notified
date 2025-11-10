package com.notif1ed.util;

/**
 * Centralized constants for the Notif1ed application.
 * Contains SQL queries, error messages, success messages, and other constants.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class Constants {
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
    
    // ==================== SQL QUERIES ====================
    
    // Student Queries
    public static final String SELECT_ALL_STUDENTS = 
        "SELECT student_number, first_name, COALESCE(last_name, guardian_name, '') as last_name, " +
        "COALESCE(section, '') as section, email FROM students ORDER BY student_number";
    
    public static final String INSERT_STUDENT = 
        "INSERT INTO students (student_number, first_name, last_name, email, section, guardian_name, guardian_email, created_by) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
    
    public static final String UPDATE_STUDENT = 
        "UPDATE students SET first_name = ?, last_name = ?, email = ?, section = ?, " +
        "guardian_name = ?, guardian_email = ? WHERE student_number = ?";
    
    public static final String DELETE_STUDENT = 
        "DELETE FROM students WHERE student_number = ?";
    
    public static final String SELECT_STUDENT_BY_NUMBER = 
        "SELECT * FROM students WHERE student_number = ?";
    
    public static final String GET_NEXT_STUDENT_NUMBER = 
        "SELECT student_number FROM students WHERE student_number LIKE ? ORDER BY student_number DESC LIMIT 1";
    
    // Subject Queries
    public static final String SELECT_ALL_SUBJECTS = 
        "SELECT subject_code, subject_name, description, year_level, created_by FROM subjects ORDER BY subject_code";
    
    public static final String INSERT_SUBJECT = 
        "INSERT INTO subjects (subject_code, subject_name, description, year_level, created_by) VALUES (?, ?, ?, ?, 1)";
    
    public static final String UPDATE_SUBJECT = 
        "UPDATE subjects SET subject_name = ?, description = ?, year_level = ? WHERE subject_code = ?";
    
    public static final String DELETE_SUBJECT = 
        "DELETE FROM subjects WHERE subject_code = ?";
    
    // Record Queries
    public static final String SELECT_ALL_RECORDS = 
        "SELECT r.record_id, s.student_number, s.first_name, s.last_name, s.email, " +
        "r.created_at, r.record_type FROM records r " +
        "JOIN students s ON r.student_id = s.student_id ORDER BY r.created_at DESC";
    
    public static final String INSERT_RECORD = 
        "INSERT INTO records (student_id, record_type, created_at) VALUES (?, ?, ?)";
    
    // User Queries
    public static final String SELECT_USER_BY_EMAIL = 
        "SELECT user_id, name, email, password FROM users WHERE email = ?";
    
    public static final String INSERT_USER = 
        "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    
    // Dashboard Queries
    public static final String COUNT_STUDENTS = 
        "SELECT COUNT(*) as count FROM students";
    
    public static final String COUNT_SUBJECTS = 
        "SELECT COUNT(*) as count FROM subjects";
    
    public static final String COUNT_RECORDS = 
        "SELECT COUNT(*) as count FROM records";
    
    // ==================== ERROR MESSAGES ====================
    
    public static final String ERR_DB_CONNECTION = "Could not connect to database";
    public static final String ERR_DB_ERROR = "Database error occurred";
    public static final String ERR_INVALID_EMAIL = "Please enter a valid email address";
    public static final String ERR_INVALID_GUARDIAN_EMAIL = "Please enter a valid guardian email address";
    public static final String ERR_EMPTY_FIELDS = "Please fill in all required fields";
    public static final String ERR_INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ERR_DUPLICATE_ENTRY = "This entry already exists";
    public static final String ERR_STUDENT_NUMBER_EXISTS = "This student number already exists";
    public static final String ERR_SUBJECT_CODE_EXISTS = "This subject code already exists";
    public static final String ERR_EMAIL_EXISTS = "This email is already registered";
    public static final String ERR_WEAK_PASSWORD = "Password must be at least 8 characters with letters and numbers";
    public static final String ERR_LOAD_PAGE = "Could not load page";
    public static final String ERR_INVALID_STUDENT_NUMBER = "Invalid student number format";
    public static final String ERR_INVALID_NAME = "Name can only contain letters, spaces, hyphens, and apostrophes";
    public static final String ERR_INVALID_SECTION = "Invalid section format";
    public static final String ERR_INVALID_SUBJECT_CODE = "Invalid subject code format";
    
    // ==================== SUCCESS MESSAGES ====================
    
    public static final String SUCCESS_STUDENT_ADDED = "Student added successfully";
    public static final String SUCCESS_STUDENT_UPDATED = "Student updated successfully";
    public static final String SUCCESS_STUDENT_DELETED = "Student deleted successfully";
    public static final String SUCCESS_SUBJECT_ADDED = "Subject added successfully";
    public static final String SUCCESS_SUBJECT_UPDATED = "Subject updated successfully";
    public static final String SUCCESS_SUBJECT_DELETED = "Subject deleted successfully";
    public static final String SUCCESS_RECORD_ADDED = "Record added successfully";
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_LOGOUT = "Logged out successfully";
    public static final String SUCCESS_SIGNUP = "Account created successfully";
    public static final String SUCCESS_EMAIL_SENT = "Email sent successfully";
    
    // ==================== WARNING MESSAGES ====================
    
    public static final String WARN_EMPTY_EMAIL_PASSWORD = "Please enter both email and password";
    public static final String WARN_DELETE_CONFIRMATION = "Are you sure you want to delete this item?";
    public static final String WARN_LOGOUT_CONFIRMATION = "Are you sure you want to logout?";
    
    // ==================== RECORD TYPES ====================
    
    public static final String RECORD_TYPE_EMAIL_SENT = "EMAIL_SENT";
    public static final String RECORD_TYPE_STUDENT_ADDED = "STUDENT_ADDED";
    public static final String RECORD_TYPE_STUDENT_UPDATED = "STUDENT_UPDATED";
    public static final String RECORD_TYPE_STUDENT_DELETED = "STUDENT_DELETED";
    public static final String RECORD_TYPE_ENROLLMENT = "ENROLLMENT";
    public static final String RECORD_TYPE_SUBJECT_ADDED = "SUBJECT_ADDED";
    
    // ==================== APPLICATION CONSTANTS ====================
    
    public static final String APP_TITLE = "Notif1ed";
    public static final String DEFAULT_STUDENT_NUMBER_PREFIX = "25-";
    public static final int DEFAULT_STUDENT_NUMBER_LENGTH = 4;
}
