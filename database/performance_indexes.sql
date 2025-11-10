-- =============================================
-- Database Performance Optimization - Indexes
-- Notif1ed Application
-- =============================================
-- This script creates indexes on frequently queried columns
-- to improve database performance.
-- Run this script after initial database setup.
-- =============================================

USE notified_db;

-- =============================================
-- Students Table Indexes
-- =============================================

-- Index on student_number for fast lookups (if not already unique)
-- Used in: Student search, delete, update operations
-- Note: Already has unique constraint, so this creates additional index
CREATE INDEX IF NOT EXISTS idx_student_number 
ON students(student_number);

-- Index on first_name for name searches
-- Used in: Student search by name
CREATE INDEX IF NOT EXISTS idx_student_first_name 
ON students(first_name);

-- Index on last_name for name searches  
-- Used in: Student search by surname
CREATE INDEX IF NOT EXISTS idx_student_last_name
ON students(last_name);

-- Index on created_by for filtering students by creator
-- Used in: Audit trails and user-specific queries
CREATE INDEX IF NOT EXISTS idx_student_created_by 
ON students(created_by);


-- =============================================
-- Users Table Indexes
-- =============================================

-- Index on email for login authentication
-- CRITICAL: Used on every login attempt
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_email 
ON users(email);

-- Index on created_at for user registration reports
-- Used in: User analytics and registration tracking
CREATE INDEX IF NOT EXISTS idx_user_created 
ON users(created_at);


-- =============================================
-- Subjects Table Indexes
-- =============================================

-- Index on subject_code for fast subject lookups
-- Used in: Subject search and enrollment operations
CREATE UNIQUE INDEX IF NOT EXISTS idx_subject_code 
ON subjects(subject_code);

-- Index on subject_name for name-based searches
-- Used in: Subject search functionality
CREATE INDEX IF NOT EXISTS idx_subject_name 
ON subjects(subject_name);


-- =============================================
-- Records Table Indexes
-- =============================================

-- Index on record_type for filtering by record type
-- Used in: Records page filtering (email_sent, student_added, etc.)
CREATE INDEX IF NOT EXISTS idx_record_type 
ON records(record_type);

-- Index on created_at for date filtering and sorting
-- IMPORTANT: Used in date range filters on records page
CREATE INDEX IF NOT EXISTS idx_record_created 
ON records(created_at);

-- Note: Removed student_number indexes as column doesn't exist in records table
-- If you need to link records to students, consider adding a student_id foreign key column


-- =============================================
-- Verification Queries
-- =============================================
-- Run these queries to verify indexes were created successfully

-- Show all indexes in the database
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME,
    NON_UNIQUE,
    SEQ_IN_INDEX
FROM 
    information_schema.STATISTICS
WHERE 
    TABLE_SCHEMA = 'notified_db'
ORDER BY 
    TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;


-- =============================================
-- Performance Analysis
-- =============================================
-- Use EXPLAIN to analyze query performance before/after indexes

-- Example: Check if student lookup uses index
-- EXPLAIN SELECT * FROM students WHERE student_number = '21-0001';

-- Example: Check if email authentication uses index
-- EXPLAIN SELECT * FROM users WHERE email = 'user@example.com';

-- Example: Check if record filtering uses indexes
-- EXPLAIN SELECT * FROM records WHERE record_type = 'email_sent' AND created_at >= '2024-01-01';


-- =============================================
-- Index Maintenance
-- =============================================
-- Periodically analyze tables to update index statistics

ANALYZE TABLE students;
ANALYZE TABLE users;
ANALYZE TABLE subjects;
ANALYZE TABLE records;


-- =============================================
-- Notes
-- =============================================
-- 1. Indexes improve SELECT/WHERE/JOIN performance but slightly slow INSERT/UPDATE/DELETE
-- 2. Use EXPLAIN before adding indexes to verify they're being used
-- 3. UNIQUE indexes also enforce data integrity (no duplicates)
-- 4. Composite indexes are more efficient for queries using multiple columns
-- 5. Run ANALYZE TABLE periodically to keep index statistics up-to-date
-- 6. Monitor index usage with: SHOW INDEX FROM table_name;
