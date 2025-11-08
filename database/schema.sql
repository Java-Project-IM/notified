-- ========================================
-- Notif1ed Database Schema
-- Database: notified_DB
-- ========================================

USE notified_DB;

-- ========================================
-- Table: users
-- Stores user account information
-- ========================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ========================================
-- Table: students
-- Stores student information
-- ========================================
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ========================================
-- Table: subjects
-- Stores subject/course information
-- ========================================
CREATE TABLE IF NOT EXISTS subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ========================================
-- Table: student_subjects
-- Links students to their enrolled subjects
-- ========================================
CREATE TABLE IF NOT EXISTS student_subjects (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    enrollment_date DATE DEFAULT (CURRENT_DATE),
    status ENUM('active', 'completed', 'dropped') DEFAULT 'active',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, subject_id)
);

-- ========================================
-- Table: notifications
-- Stores notification records
-- ========================================
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'sent', 'failed') DEFAULT 'pending',
    sent_by INT,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE SET NULL,
    FOREIGN KEY (sent_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ========================================
-- Table: records
-- Stores general records/logs
-- ========================================
CREATE TABLE IF NOT EXISTS records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT,
    record_type VARCHAR(50) NOT NULL,
    record_data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE SET NULL
);

-- ========================================
-- Insert Sample Data (Optional)
-- ========================================

-- Sample User
INSERT INTO users (name, email, password) VALUES 
('Admin User', 'admin@notified.com', 'admin123');
-- Note: In production, passwords should be hashed!

-- Sample Students
INSERT INTO students (student_number, first_name, last_name, email, phone, created_by) VALUES 
('2021-001', 'John', 'Doe', 'john.doe@student.com', '123-456-7890', 1),
('2021-002', 'Jane', 'Smith', 'jane.smith@student.com', '123-456-7891', 1),
('2021-003', 'Bob', 'Johnson', 'bob.johnson@student.com', '123-456-7892', 1);

-- Sample Subjects
INSERT INTO subjects (subject_code, subject_name, description, created_by) VALUES 
('CS101', 'Introduction to Computer Science', 'Basic programming concepts', 1),
('MATH201', 'Calculus I', 'Differential and integral calculus', 1),
('ENG101', 'English Composition', 'Writing and communication skills', 1);

-- Sample Enrollments
INSERT INTO student_subjects (student_id, subject_id, status) VALUES 
(1, 1, 'active'),
(1, 2, 'active'),
(2, 1, 'active'),
(2, 3, 'active'),
(3, 2, 'active'),
(3, 3, 'active');

-- ========================================
-- Indexes for Performance
-- ========================================
CREATE INDEX idx_students_email ON students(email);
CREATE INDEX idx_students_number ON students(student_number);
CREATE INDEX idx_subjects_code ON subjects(subject_code);
CREATE INDEX idx_notifications_status ON notifications(status);
CREATE INDEX idx_notifications_sent_at ON notifications(sent_at);

-- ========================================
-- Views (Optional - for easier queries)
-- ========================================

-- View: Student Enrollments with Subject Details
CREATE OR REPLACE VIEW student_enrollment_details AS
SELECT 
    s.student_id,
    s.student_number,
    CONCAT(s.first_name, ' ', s.last_name) AS student_name,
    s.email AS student_email,
    sub.subject_code,
    sub.subject_name,
    se.enrollment_date,
    se.status
FROM students s
JOIN student_subjects se ON s.student_id = se.student_id
JOIN subjects sub ON se.subject_id = sub.subject_id;

-- View: Notification History
CREATE OR REPLACE VIEW notification_history AS
SELECT 
    n.notification_id,
    CONCAT(s.first_name, ' ', s.last_name) AS student_name,
    s.email AS student_email,
    sub.subject_name,
    n.title,
    n.message,
    n.sent_at,
    n.status,
    u.name AS sent_by
FROM notifications n
JOIN students s ON n.student_id = s.student_id
LEFT JOIN subjects sub ON n.subject_id = sub.subject_id
LEFT JOIN users u ON n.sent_by = u.user_id;

-- ========================================
-- Database Setup Complete!
-- ========================================
SELECT 'Database schema created successfully!' AS Status;
