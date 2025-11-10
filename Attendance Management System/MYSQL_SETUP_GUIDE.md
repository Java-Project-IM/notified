# MySQL Database Setup Guide
## Attendance Management System

---

## ?? Prerequisites

1. **MySQL Server** installed (version 5.7 or higher recommended)
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Or install XAMPP/WAMP which includes MySQL

2. **MySQL Workbench** (Optional but recommended)
   - Download from: https://dev.mysql.com/downloads/workbench/

---

## ?? Installation Methods

### Method 1: Using MySQL Command Line

1. **Open MySQL Command Line Client**
   ```bash
   mysql -u root -p
   ```

2. **Run the SQL Script**
   ```sql
   source C:/Users/Josh Cabunoc/source/repos/Attendance Management System/Attendance Management System/database_schema.sql
   ```
   
   OR copy and paste the contents of `database_schema.sql` into the MySQL command line.

### Method 2: Using MySQL Workbench

1. **Open MySQL Workbench**
2. **Connect to your MySQL Server**
3. **Open SQL Script**
   - File ? Open SQL Script
   - Select `database_schema.sql`
4. **Execute Script**
   - Click the lightning bolt icon or press Ctrl+Shift+Enter

### Method 3: Using phpMyAdmin (XAMPP/WAMP)

1. **Open phpMyAdmin** (usually at http://localhost/phpmyadmin)
2. **Click "SQL" tab**
3. **Copy and paste** the contents of `database_schema.sql`
4. **Click "Go"** to execute

### Method 4: Automatic (Using the Application)

The application will automatically create the database and tables when you run it for the first time!

---

## ?? Configuration

### Step 1: Update Database Connection Settings

Open `DatabaseConfig.vb` and update the following values:

```vb
Public Shared ReadOnly Server As String = "localhost"        ' Your MySQL server address
Public Shared ReadOnly Port As String = "3306"                ' Default MySQL port
Public Shared ReadOnly Database As String = "attendance_db"   ' Database name
Public Shared ReadOnly Username As String = "root"            ' Your MySQL username
Public Shared ReadOnly Password As String = ""                ' Your MySQL password
```

### Common Configurations:

**XAMPP Default:**
```vb
Server = "localhost"
Port = "3306"
Username = "root"
Password = ""  ' Empty password
```

**WAMP Default:**
```vb
Server = "localhost"
Port = "3306"
Username = "root"
Password = ""  ' Empty password
```

**Custom MySQL Installation:**
```vb
Server = "localhost"  ' or your server IP
Port = "3306"         ' or your custom port
Username = "your_username"
Password = "your_password"
```

---

## ??? Database Structure

### Tables Created:

#### 1. **students** - Student Information
- `student_id` (Primary Key)
- `first_name`, `last_name`
- `student_number` (Unique)
- `course`, `email`, `phone`
- `date_enrolled`, `status`
- Timestamps: `created_at`, `updated_at`

#### 2. **attendance_records** - Daily Attendance
- `attendance_id` (Primary Key)
- `student_id` (Foreign Key)
- `attendance_date`, `attendance_time`
- `status` (Present, Absent, Late, Excused)
- `remarks`
- Timestamps: `created_at`, `updated_at`

#### 3. **courses** - Course Catalog
- `course_id` (Primary Key)
- `course_code` (Unique), `course_name`
- `department`, `credits`
- `description`, `is_active`

#### 4. **classes** - Class Sessions
- `class_id` (Primary Key)
- `course_id` (Foreign Key)
- `class_name`, `schedule_day`, `schedule_time`
- `room_number`, `instructor_name`
- `semester`, `academic_year`

#### 5. **enrollments** - Student Enrollments
- `enrollment_id` (Primary Key)
- `student_id`, `class_id` (Foreign Keys)
- `enrollment_date`, `status`, `grade`

---

## ?? Sample Data Included

The script includes sample data:
- 5 Sample Courses (CS101, MATH101, ENG101, PHYS101, CHEM101)
- 5 Sample Students
- 5 Sample Classes
- 9 Sample Enrollments
- 15 Sample Attendance Records

---

## ?? Useful Views Created

### 1. `vw_student_attendance_summary`
Shows attendance statistics for each student including:
- Total records, Present/Absent/Late/Excused counts
- Attendance percentage

**Usage:**
```sql
SELECT * FROM vw_student_attendance_summary;
```

### 2. `vw_daily_attendance`
Shows daily attendance records with student details

**Usage:**
```sql
SELECT * FROM vw_daily_attendance WHERE attendance_date = CURDATE();
```

### 3. `vw_course_attendance_stats`
Shows attendance statistics by course

**Usage:**
```sql
SELECT * FROM vw_course_attendance_stats;
```

---

## ?? Stored Procedures

### 1. `sp_get_attendance_by_date_range`
Get attendance records for a specific date range

**Usage:**
```sql
CALL sp_get_attendance_by_date_range('2024-01-22', '2024-01-24');
```

### 2. `sp_get_student_attendance_history`
Get attendance history for a specific student

**Usage:**
```sql
CALL sp_get_student_attendance_history('STU2024001');
```

---

## ? Testing the Connection

### Test Query 1: Check if database exists
```sql
SHOW DATABASES LIKE 'attendance_db';
```

### Test Query 2: View all tables
```sql
USE attendance_db;
SHOW TABLES;
```

### Test Query 3: Count records
```sql
SELECT 
    (SELECT COUNT(*) FROM students) as total_students,
    (SELECT COUNT(*) FROM attendance_records) as total_attendance;
```

### Test Query 4: View sample data
```sql
SELECT * FROM students LIMIT 5;
SELECT * FROM attendance_records LIMIT 5;
```

---

## ?? Troubleshooting

### Error: "Access denied for user"
**Solution:** Check your username and password in `DatabaseConfig.vb`

### Error: "Can't connect to MySQL server"
**Solution:** 
- Make sure MySQL service is running
- Check if port 3306 is not blocked by firewall
- Verify server address (localhost vs 127.0.0.1)

### Error: "Unknown database 'attendance_db'"
**Solution:** Run the database_schema.sql script to create the database

### Error: "Table doesn't exist"
**Solution:** 
- Make sure you ran the complete SQL script
- Check if you're using the correct database: `USE attendance_db;`

---

## ?? Security Recommendations

### For Production Use:

1. **Create a dedicated MySQL user** (don't use root):
```sql
CREATE USER 'attendance_user'@'localhost' IDENTIFIED BY 'strong_password_here';
GRANT ALL PRIVILEGES ON attendance_db.* TO 'attendance_user'@'localhost';
FLUSH PRIVILEGES;
```

2. **Update DatabaseConfig.vb** with the new credentials

3. **Use strong passwords**

4. **Enable MySQL SSL** for remote connections

---

## ?? Useful SQL Queries for Reports

### Students with low attendance (below 75%)
```sql
SELECT * FROM vw_student_attendance_summary 
WHERE attendance_percentage < 75 
ORDER BY attendance_percentage ASC;
```

### Today's attendance
```sql
SELECT * FROM vw_daily_attendance 
WHERE attendance_date = CURDATE();
```

### Absent students today
```sql
SELECT * FROM vw_daily_attendance 
WHERE attendance_date = CURDATE() 
AND status = 'Absent';
```

### Monthly attendance summary
```sql
SELECT 
    DATE_FORMAT(attendance_date, '%Y-%m') as month,
    status,
    COUNT(*) as count
FROM attendance_records
GROUP BY month, status
ORDER BY month DESC;
```

### Students never absent
```sql
SELECT * FROM vw_student_attendance_summary 
WHERE absent_count = 0 
AND total_records > 0;
```

---

## ?? Support

If you encounter any issues:
1. Check the error message in the application
2. Verify MySQL service is running
3. Check connection settings in DatabaseConfig.vb
4. Test connection using MySQL Workbench
5. Review this guide's troubleshooting section

---

## ?? Updating the Database

To add more fields or tables in the future:
1. Create a new SQL migration script
2. Test on a copy of the database first
3. Backup your data before applying changes
4. Update the corresponding VB classes

---

**Last Updated:** 2024
**Version:** 1.0
