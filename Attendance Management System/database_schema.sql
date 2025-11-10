-- ========================================
-- ATTENDANCE MANAGEMENT SYSTEM - MySQL Database Schema
-- ========================================
-- Created: 2024
-- Database: attendance_db
-- ========================================

-- Create Database
CREATE DATABASE IF NOT EXISTS attendance_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE attendance_db;

-- ========================================
-- TABLE 1: STUDENTS
-- Stores student information
-- ========================================
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    student_number VARCHAR(50) NOT NULL UNIQUE,
    course VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    date_enrolled DATE,
    status ENUM('Active', 'Inactive', 'Graduated') DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_student_number (student_number),
    INDEX idx_course (course),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLE 2: ATTENDANCE_RECORDS
-- Stores daily attendance records
-- ========================================
CREATE TABLE IF NOT EXISTS attendance_records (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    attendance_time TIME NOT NULL,
    status ENUM('Present', 'Absent', 'Late', 'Excused') NOT NULL,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_student_date (student_id, attendance_date),
    INDEX idx_attendance_date (attendance_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLE 3: COURSES
-- Stores course information
-- ========================================
CREATE TABLE IF NOT EXISTS courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(200) NOT NULL,
    department VARCHAR(100),
    credits INT,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_course_code (course_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLE 4: CLASSES
-- Stores class/session information
-- ========================================
CREATE TABLE IF NOT EXISTS classes (
    class_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    schedule_day ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
    schedule_time TIME,
    room_number VARCHAR(50),
    instructor_name VARCHAR(100),
    semester VARCHAR(50),
    academic_year VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    INDEX idx_schedule (schedule_day, schedule_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLE 5: ENROLLMENTS
-- Stores student enrollments in classes
-- ========================================
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    enrollment_date DATE NOT NULL,
    status ENUM('Enrolled', 'Dropped', 'Completed') DEFAULT 'Enrolled',
    grade VARCHAR(5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, class_id),
    INDEX idx_student (student_id),
    INDEX idx_class (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- SAMPLE DATA
-- ========================================

-- Insert Sample Courses
INSERT INTO courses (course_code, course_name, department, credits, description) VALUES
('CS101', 'Introduction to Computer Science', 'Computer Science', 3, 'Fundamental concepts of programming'),
('MATH101', 'Calculus I', 'Mathematics', 4, 'Differential and integral calculus'),
('ENG101', 'English Composition', 'English', 3, 'Writing and composition fundamentals'),
('PHYS101', 'Physics I', 'Physics', 4, 'Mechanics and thermodynamics'),
('CHEM101', 'General Chemistry', 'Chemistry', 4, 'Basic chemistry principles');

-- Insert Sample Students
INSERT INTO students (first_name, last_name, student_number, course, email, phone, date_enrolled, status) VALUES
('John', 'Doe', 'STU2024001', 'Computer Science', 'john.doe@email.com', '123-456-7890', '2024-01-15', 'Active'),
('Jane', 'Smith', 'STU2024002', 'Computer Science', 'jane.smith@email.com', '123-456-7891', '2024-01-15', 'Active'),
('Michael', 'Johnson', 'STU2024003', 'Information Technology', 'michael.j@email.com', '123-456-7892', '2024-01-16', 'Active'),
('Emily', 'Brown', 'STU2024004', 'Software Engineering', 'emily.b@email.com', '123-456-7893', '2024-01-16', 'Active'),
('David', 'Wilson', 'STU2024005', 'Computer Science', 'david.w@email.com', '123-456-7894', '2024-01-17', 'Active');

-- Insert Sample Classes
INSERT INTO classes (course_id, class_name, schedule_day, schedule_time, room_number, instructor_name, semester, academic_year) VALUES
(1, 'CS101-A', 'Monday', '09:00:00', 'Room 101', 'Prof. Anderson', 'Fall', '2024-2025'),
(1, 'CS101-B', 'Wednesday', '14:00:00', 'Room 102', 'Prof. Anderson', 'Fall', '2024-2025'),
(2, 'MATH101-A', 'Tuesday', '10:00:00', 'Room 201', 'Prof. Martinez', 'Fall', '2024-2025'),
(3, 'ENG101-A', 'Thursday', '11:00:00', 'Room 301', 'Prof. Taylor', 'Fall', '2024-2025'),
(4, 'PHYS101-A', 'Friday', '13:00:00', 'Lab 401', 'Prof. Lee', 'Fall', '2024-2025');

-- Insert Sample Enrollments
INSERT INTO enrollments (student_id, class_id, enrollment_date, status) VALUES
(1, 1, '2024-01-20', 'Enrolled'),
(1, 3, '2024-01-20', 'Enrolled'),
(2, 1, '2024-01-20', 'Enrolled'),
(2, 2, '2024-01-21', 'Enrolled'),
(3, 2, '2024-01-21', 'Enrolled'),
(3, 4, '2024-01-21', 'Enrolled'),
(4, 3, '2024-01-22', 'Enrolled'),
(4, 5, '2024-01-22', 'Enrolled'),
(5, 1, '2024-01-22', 'Enrolled');

-- Insert Sample Attendance Records
INSERT INTO attendance_records (student_id, attendance_date, attendance_time, status, remarks) VALUES
-- Day 1: January 22, 2024
(1, '2024-01-22', '09:05:00', 'Present', 'On time'),
(2, '2024-01-22', '09:03:00', 'Present', 'On time'),
(3, '2024-01-22', '09:15:00', 'Late', 'Arrived 15 minutes late'),
(4, '2024-01-22', '09:00:00', 'Present', 'On time'),
(5, '2024-01-22', '00:00:00', 'Absent', 'No show'),

-- Day 2: January 23, 2024
(1, '2024-01-23', '09:02:00', 'Present', 'On time'),
(2, '2024-01-23', '09:01:00', 'Present', 'On time'),
(3, '2024-01-23', '09:00:00', 'Present', 'On time'),
(4, '2024-01-23', '00:00:00', 'Absent', 'Sick leave'),
(5, '2024-01-23', '09:20:00', 'Late', 'Traffic delay'),

-- Day 3: January 24, 2024
(1, '2024-01-24', '09:00:00', 'Present', 'On time'),
(2, '2024-01-24', '00:00:00', 'Excused', 'Medical appointment'),
(3, '2024-01-24', '09:05:00', 'Present', 'On time'),
(4, '2024-01-24', '09:03:00', 'Present', 'On time'),
(5, '2024-01-24', '09:01:00', 'Present', 'On time');

-- ========================================
-- USEFUL VIEWS
-- ========================================

-- View: Student Attendance Summary
CREATE OR REPLACE VIEW vw_student_attendance_summary AS
SELECT 
    s.student_id,
    s.student_number,
    CONCAT(s.first_name, ' ', s.last_name) AS full_name,
    s.course,
    COUNT(a.attendance_id) AS total_records,
    SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_count,
    SUM(CASE WHEN a.status = 'Absent' THEN 1 ELSE 0 END) AS absent_count,
    SUM(CASE WHEN a.status = 'Late' THEN 1 ELSE 0 END) AS late_count,
    SUM(CASE WHEN a.status = 'Excused' THEN 1 ELSE 0 END) AS excused_count,
    ROUND((SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) / COUNT(a.attendance_id)) * 100, 2) AS attendance_percentage
FROM students s
LEFT JOIN attendance_records a ON s.student_id = a.student_id
GROUP BY s.student_id, s.student_number, s.first_name, s.last_name, s.course;

-- View: Daily Attendance Report
CREATE OR REPLACE VIEW vw_daily_attendance AS
SELECT 
    a.attendance_date,
    a.attendance_time,
    s.student_number,
    CONCAT(s.first_name, ' ', s.last_name) AS full_name,
    s.course,
    a.status,
    a.remarks
FROM attendance_records a
INNER JOIN students s ON a.student_id = s.student_id
ORDER BY a.attendance_date DESC, a.attendance_time DESC;

-- View: Course Attendance Statistics
CREATE OR REPLACE VIEW vw_course_attendance_stats AS
SELECT 
    s.course,
    COUNT(DISTINCT s.student_id) AS total_students,
    COUNT(a.attendance_id) AS total_records,
    SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_count,
    SUM(CASE WHEN a.status = 'Absent' THEN 1 ELSE 0 END) AS absent_count,
    ROUND(AVG(CASE WHEN a.status = 'Present' THEN 100 ELSE 0 END), 2) AS average_attendance_rate
FROM students s
LEFT JOIN attendance_records a ON s.student_id = a.student_id
GROUP BY s.course;

-- ========================================
-- USEFUL STORED PROCEDURES
-- ========================================

-- Procedure: Get Attendance by Date Range
DELIMITER //
CREATE PROCEDURE sp_get_attendance_by_date_range(
    IN p_start_date DATE,
    IN p_end_date DATE
)
BEGIN
    SELECT 
        a.attendance_date,
        s.student_number,
        CONCAT(s.first_name, ' ', s.last_name) AS full_name,
        s.course,
        a.status,
        a.remarks
    FROM attendance_records a
    INNER JOIN students s ON a.student_id = s.student_id
    WHERE a.attendance_date BETWEEN p_start_date AND p_end_date
    ORDER BY a.attendance_date DESC, s.last_name, s.first_name;
END //
DELIMITER ;

-- Procedure: Get Student Attendance History
DELIMITER //
CREATE PROCEDURE sp_get_student_attendance_history(
    IN p_student_number VARCHAR(50)
)
BEGIN
    SELECT 
        a.attendance_date,
        a.attendance_time,
        a.status,
        a.remarks
    FROM attendance_records a
    INNER JOIN students s ON a.student_id = s.student_id
    WHERE s.student_number = p_student_number
    ORDER BY a.attendance_date DESC, a.attendance_time DESC;
END //
DELIMITER ;

-- ========================================
-- USEFUL QUERIES
-- ========================================

-- Query 1: Students with low attendance (below 75%)
-- SELECT * FROM vw_student_attendance_summary WHERE attendance_percentage < 75;

-- Query 2: Today's attendance
-- SELECT * FROM vw_daily_attendance WHERE attendance_date = CURDATE();

-- Query 3: Absent students for a specific date
-- SELECT * FROM vw_daily_attendance WHERE attendance_date = '2024-01-22' AND status = 'Absent';

-- Query 4: Course-wise attendance summary
-- SELECT * FROM vw_course_attendance_stats;

-- Query 5: Get attendance for date range (using stored procedure)
-- CALL sp_get_attendance_by_date_range('2024-01-22', '2024-01-24');

-- Query 6: Get specific student's attendance history (using stored procedure)
-- CALL sp_get_student_attendance_history('STU2024001');

-- ========================================
-- DATABASE INFORMATION
-- ========================================
-- To view table structure: DESCRIBE table_name;
-- To view all tables: SHOW TABLES;
-- To view indexes: SHOW INDEX FROM table_name;
-- To view foreign keys: SELECT * FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA = 'attendance_db';
