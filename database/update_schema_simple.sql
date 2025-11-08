-- Simple Database Update for phpMyAdmin
-- Copy and paste this ENTIRE script into phpMyAdmin SQL tab

USE notified_DB;

-- For students table - run one at a time if needed
ALTER TABLE students ADD COLUMN section VARCHAR(20) AFTER last_name;
ALTER TABLE students ADD COLUMN guardian_name VARCHAR(100) AFTER section;
ALTER TABLE students MODIFY COLUMN last_name VARCHAR(50) NULL;

-- For subjects table - run one at a time if needed
ALTER TABLE subjects ADD COLUMN year_level INT AFTER subject_name;
ALTER TABLE subjects ADD COLUMN section VARCHAR(20) AFTER year_level;

-- Verify the changes
DESCRIBE students;
DESCRIBE subjects;
