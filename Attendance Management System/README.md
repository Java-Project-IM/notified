# Attendance Management System

A comprehensive Windows Forms application built with Visual Basic .NET that implements a complete CRUD (Create, Read, Update, Delete) system for managing student attendance records with **MySQL Database**. Now features **separate interfaces for students and administrators**!

## ?? New Features

### Dual Interface System
- **?? Student Check-In Mode** - Simple interface for students to mark attendance with just their student number
- **?? Admin Dashboard** - Comprehensive control panel for viewing, searching, and managing attendance

## Features

### Core CRUD Operations (Admin)
- **CREATE**: Add new student attendance records
- **READ**: View all attendance records in a searchable DataGridView
- **UPDATE**: Edit existing attendance records
- **DELETE**: Remove attendance records with confirmation

### Student Check-In System ? NEW
- **Quick Check-In**: Students enter only their student number
- **Auto-Status**: Automatically marks Present or Late based on time
- **Duplicate Prevention**: Cannot check in twice on the same day
- **Real-Time Clock**: Displays current time and date
- **Instant Feedback**: Visual confirmation after check-in
- **Student Verification**: Validates against database

### Admin Dashboard ? NEW
- **Real-Time Statistics**: View today's attendance summary
- **Date Range Search**: Find records between specific dates
- **Student Search**: Look up individual student attendance history
- **Export Reports**: Save attendance data to text files
- **Summary Reports**: View detailed statistics and percentages
- **Manage Students**: Access full CRUD interface
- **Password Protected**: Secure admin access

### Additional Features
- **Search Functionality**: Search records by name, student number, or course
- **Data Validation**: Input validation for all required fields
- **MySQL Database**: Enterprise-grade database with relational structure
- **User-Friendly Interface**: Clean, intuitive UI with color-coded buttons
- **Date/Time Tracking**: Automatic attendance date/time recording
- **Multiple Status Options**: Present, Absent, Late, Excused
- **Automatic Student Management**: Creates student records if they don't exist
- **Advanced Reporting**: Views and stored procedures for analytics

## Technology Stack

- **Language**: Visual Basic .NET
- **Framework**: .NET 8.0 (Windows Forms)
- **Database**: MySQL 5.7+ / MariaDB
- **Packages**: 
  - MySql.Data v9.5.0
  - System.Data.SQLite.Core v1.0.119 (legacy)

## System Architecture

### Three Main Interfaces

#### 1. Login Screen
- Choose between Student or Admin mode
- Password protection for admin access
- Clean, modern design

#### 2. Student Check-In
- Minimalist interface
- Large text input for student number
- Real-time clock display
- Visual feedback system
- Auto late detection (after 9:00 AM)

#### 3. Admin Dashboard
- Statistics overview
- Search and filter tools
- Data grid with all records
- Export and reporting features
- Access to full management system

## Database Structure

### Main Tables

#### 1. **students** - Student Master Data
| Column | Type | Description |
|--------|------|-------------|
| student_id | INT (PK, Auto) | Unique student identifier |
| first_name | VARCHAR(100) | Student's first name |
| last_name | VARCHAR(100) | Student's last name |
| student_number | VARCHAR(50) UNIQUE | Student ID number |
| course | VARCHAR(100) | Enrolled course/program |
| email | VARCHAR(100) | Email address |
| phone | VARCHAR(20) | Contact number |
| date_enrolled | DATE | Enrollment date |
| status | ENUM | Active/Inactive/Graduated |
| created_at | TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | Last update time |

#### 2. **attendance_records** - Daily Attendance
| Column | Type | Description |
|--------|------|-------------|
| attendance_id | INT (PK, Auto) | Unique attendance record ID |
| student_id | INT (FK) | Reference to students table |
| attendance_date | DATE | Date of attendance |
| attendance_time | TIME | Time of attendance |
| status | ENUM | Present/Absent/Late/Excused |
| remarks | TEXT | Additional notes |
| created_at | TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | Last update time |

#### 3. **courses** - Course Catalog
Stores available courses with details like course code, name, department, credits, and description.

#### 4. **classes** - Class Sessions
Manages class schedules including day, time, room, instructor, semester, and academic year.

#### 5. **enrollments** - Student Enrollments
Links students to classes they're enrolled in, including enrollment date, status, and grades.

### Database Relationships
- **students** ? **attendance_records** (One-to-Many)
- **courses** ? **classes** (One-to-Many)
- **students** ? **enrollments** ? **classes** (Many-to-Many)

## Project Structure

```
Attendance Management System/
??? LoginForm.vb                    # Main login interface ? NEW
??? LoginForm.Designer.vb           # Login UI design ? NEW
??? StudentCheckIn.vb               # Student check-in logic ? NEW
??? StudentCheckIn.Designer.vb      # Student UI design ? NEW
??? AdminDashboard.vb               # Admin control panel ? NEW
??? AdminDashboard.Designer.vb      # Admin UI design ? NEW
??? Form1.vb                        # Full CRUD management
??? Form1.Designer.vb               # CRUD UI design
??? Student.vb                      # Student data model
??? DatabaseHelper.vb               # Database operations (CRUD)
??? DatabaseConfig.vb               # MySQL connection settings
??? DatabaseConnectionTester.vb     # Connection testing utility
??? database_schema.sql             # Complete database structure
??? README.md                       # This file
??? USER_GUIDE.md                   # Detailed user manual ? NEW
??? MYSQL_SETUP_GUIDE.md           # MySQL setup instructions
??? DATABASE_TABLES_REFERENCE.md    # Table structures
??? PROJECT_SUMMARY.md              # Quick reference
```

## ?? Quick Start

### Step 1: Configure MySQL Connection
Open **`DatabaseConfig.vb`** and update:
```vb
Public Shared ReadOnly Server As String = "localhost"
Public Shared ReadOnly Port As String = "3306"
Public Shared ReadOnly Database As String = "attendance_db"
Public Shared ReadOnly Username As String = "root"
Public Shared ReadOnly Password As String = ""  ' Your MySQL password here
```

### Step 2: Start MySQL
Make sure MySQL server is running:
- **XAMPP/WAMP**: Start MySQL service
- **Standalone**: Ensure MySQL is running on port 3306

### Step 3: Run the Application
- Press **F5** in Visual Studio
- You'll see the **Login Screen** with two options:
  - **?? ADMIN LOGIN** (Password: `admin123`)
  - **?? STUDENT CHECK-IN**

---

## ?? How to Use

### For Students:

1. **Click "?? STUDENT CHECK-IN"** on login screen
2. **Enter your Student Number** (e.g., STU2024001)
3. **Press Enter** or click "? CHECK IN"
4. **See confirmation** - Present or Late status
5. **Done!** Takes only 5 seconds

**Note:** 
- ? Before 9:00 AM = **Present**
- ? After 9:00 AM = **Late**
- ? Can only check in once per day

### For Administrators:

1. **Click "?? ADMIN LOGIN"** on login screen
2. **Enter password** (default: `admin123`)
3. **Dashboard opens** showing:
   - Today's statistics
   - All attendance records
   - Search and export tools
4. **Use features:**
   - Search by date range
   - Search by student
   - Export reports
   - Manage students (full CRUD)
   - View summary statistics

---

## ?? Admin Dashboard Features

### Statistics Panel
- ?? **Active Students**: Total enrolled
- ?? **Today's Records**: Today's check-ins
- ? **Present**: On-time arrivals
- ?? **Late**: Late arrivals
- ? **Absent**: No check-ins

### Search Options
- **Date Range**: Find records between two dates
- **Student Search**: Look up by student number or name

### Actions
- **Refresh**: Reload today's data
- **Manage Students**: Open full CRUD interface
- **Export Report**: Save to text file
- **View Reports**: Detailed statistics
- **Close**: Return to login

---

## ?? Use Cases

### Daily Morning Routine
```
Students Arrive ? Open Check-In Screen ? Each Student Enters Number ? 
System Records Attendance ? Admin Checks Dashboard
```

### Weekly Report Generation
```
Admin Logs In ? Select Date Range (Mon-Fri) ? Export Report ? 
Save for Records
```

### Individual Student Tracking
```
Admin Logs In ? Search Student Number ? View Complete History ? 
Check Attendance Patterns
```

---

## ?? Configuration Options

### Change Admin Password
Edit `LoginForm.vb`, line ~12:
```vb
If password = "admin123" Then  ' Change this!
```

### Change Late Time Threshold
Edit `StudentCheckIn.vb`, line ~84:
```vb
Dim startTime As New TimeSpan(9, 0, 0) ' 9:00 AM
```

Examples:
- `8, 30, 0` = 8:30 AM
- `10, 0, 0` = 10:00 AM

---

## Requirements

### Software Requirements
- Windows 10/11
- .NET 8.0 Runtime or SDK
- MySQL Server 5.7+ or MariaDB 10.3+
- Visual Studio 2022 (for development)

### MySQL Setup
- MySQL Server installed and running
- MySQL Workbench (optional, for database management)
- Or XAMPP/WAMP (includes MySQL and phpMyAdmin)

## ?? Security Best Practices

### For Production:

1. **Create dedicated database user** (don't use root):
```sql
CREATE USER 'attendance_user'@'localhost' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON attendance_db.* TO 'attendance_user'@'localhost';
FLUSH PRIVILEGES;
```

2. **Use strong passwords** for database access

3. **Store credentials securely** (consider using config files outside source control)

4. **Enable MySQL SSL** for remote connections

5. **Regular backups** of the database

## ?? Features In Current Version

? Complete CRUD operations with MySQL  
? Automatic database and table creation  
? Relational database design with foreign keys  
? Student master data management  
? Attendance tracking with date/time  
? Search and filter functionality  
? Sample data included  
? Database views for reporting  
? Stored procedures for common queries  
? Indexed columns for performance  

## ?? Future Enhancements

- ?? Advanced reporting dashboard
- ?? Email notifications for absences
- ?? Export to Excel/PDF
- ?? Import from CSV
- ?? Calendar view of attendance
- ?? User authentication system
- ?? Responsive design / Web version
- ?? Graphical statistics and charts
- ?? Automatic alerts for low attendance
- ?? Grade calculation integration

## ?? Troubleshooting

### "Can't connect to MySQL server"
- Check if MySQL service is running
- Verify connection settings in `DatabaseConfig.vb`
- Check firewall settings for port 3306

### "Access denied for user"
- Verify username and password
- Check user privileges in MySQL
- Ensure user has access to `attendance_db`

### "Table doesn't exist"
- Run `database_schema.sql` script
- Or let the application create tables automatically

### Application won't start
- Check .NET 8.0 is installed
- Verify MySQL.Data package is restored
- Check Output window for build errors

## ?? Documentation

- **[MYSQL_SETUP_GUIDE.md](MYSQL_SETUP_GUIDE.md)** - Comprehensive MySQL setup guide
- **[database_schema.sql](database_schema.sql)** - Complete database structure with comments

## ?? Contributing

This is a demonstration project for educational purposes. Feel free to:
- Fork and modify for your needs
- Add new features
- Improve the UI/UX
- Optimize database queries
- Add more reports and analytics

## ?? License

Free to use for educational and personal projects.

## ????? Technical Details

### Database Features:
- **InnoDB Engine** for ACID compliance
- **UTF8MB4 encoding** for international character support
- **Cascading deletes** to maintain referential integrity
- **Indexes** on frequently queried columns
- **Triggers** for automatic timestamp updates
- **Views** for complex queries
- **Stored procedures** for reusable logic

### Application Features:
- **Parameterized queries** to prevent SQL injection
- **Connection pooling** for better performance
- **Try-catch blocks** for error handling
- **Using statements** for proper resource disposal
- **Validation** on all user inputs

## ?? Support

For issues or questions:
1. Check the troubleshooting section
2. Review [MYSQL_SETUP_GUIDE.md](MYSQL_SETUP_GUIDE.md)
3. Verify MySQL is running and accessible
4. Check connection settings

---

**Database:** MySQL  
**Version:** 2.0 (MySQL Edition)  
**Last Updated:** 2024  
**Status:** Production Ready ?
