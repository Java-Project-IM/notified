# Database Schema Update Instructions

## ⚠️ IMPORTANT: Update Your Database

The database schema has been updated to match the application forms. You need to run the update script to add missing columns.

## Option 1: Using phpMyAdmin (Recommended)

1. **Open phpMyAdmin** in your browser (usually http://localhost/phpmyadmin)
2. **Select** the `notified_DB` database from the left sidebar
3. **Click** on the **SQL** tab at the top
4. **Copy** the contents of `database/update_schema.sql`
5. **Paste** into the SQL query box
6. **Click** "Go" to execute

## Option 2: Using MySQL Command Line

If MySQL is in your PATH:
```powershell
mysql -u root notified_DB < database/update_schema.sql
```

## Option 3: Manual Updates

Run these SQL commands in phpMyAdmin SQL tab:

```sql
USE notified_DB;

-- Add section and guardian_name columns to students table
ALTER TABLE students 
ADD COLUMN section VARCHAR(20) AFTER last_name,
ADD COLUMN guardian_name VARCHAR(100) AFTER section;

-- Make last_name nullable
ALTER TABLE students 
MODIFY COLUMN last_name VARCHAR(50) NULL;

-- Add year_level and section columns to subjects table
ALTER TABLE subjects 
ADD COLUMN year_level INT AFTER subject_name,
ADD COLUMN section VARCHAR(20) AFTER year_level;
```

## What Changed?

### Students Table:
- ✅ Added `section` column (VARCHAR 20)
- ✅ Added `guardian_name` column (VARCHAR 100)
- ✅ Made `last_name` nullable (not required)

### Subjects Table:
- ✅ Added `year_level` column (INT)
- ✅ Added `section` column (VARCHAR 20)

## After Updating

Once you've run the update script, you can:
1. Run the application: `.\run.ps1`
2. Test adding students and subjects
3. Verify data appears correctly in the database

---
**Status**: Schema updates required before running application  
**Action Required**: Run `database/update_schema.sql` in phpMyAdmin
