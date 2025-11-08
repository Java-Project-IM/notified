-- ========================================
-- Database Schema Update Script
-- Adds missing columns to existing tables
-- Run this in phpMyAdmin SQL tab
-- ========================================

USE notified_DB;

-- Check and add columns to students table
SET @dbname = 'notified_DB';
SET @tablename = 'students';

-- Add section column if not exists
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname 
AND TABLE_NAME = @tablename 
AND COLUMN_NAME = 'section';

SET @query = IF(@col_exists = 0,
    'ALTER TABLE students ADD COLUMN section VARCHAR(20) AFTER last_name',
    'SELECT "Column section already exists" AS status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add guardian_name column if not exists
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname 
AND TABLE_NAME = @tablename 
AND COLUMN_NAME = 'guardian_name';

SET @query = IF(@col_exists = 0,
    'ALTER TABLE students ADD COLUMN guardian_name VARCHAR(100) AFTER section',
    'SELECT "Column guardian_name already exists" AS status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Make last_name nullable
ALTER TABLE students MODIFY COLUMN last_name VARCHAR(50) NULL;

-- Check and add columns to subjects table
SET @tablename = 'subjects';

-- Add year_level column if not exists
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname 
AND TABLE_NAME = @tablename 
AND COLUMN_NAME = 'year_level';

SET @query = IF(@col_exists = 0,
    'ALTER TABLE subjects ADD COLUMN year_level INT AFTER subject_name',
    'SELECT "Column year_level already exists" AS status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add section column if not exists
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname 
AND TABLE_NAME = @tablename 
AND COLUMN_NAME = 'section';

SET @query = IF(@col_exists = 0,
    'ALTER TABLE subjects ADD COLUMN section VARCHAR(20) AFTER year_level',
    'SELECT "Column section already exists" AS status');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Show updated table structures
DESCRIBE students;
DESCRIBE subjects;

SELECT '✅ Database schema update completed!' as Status;
