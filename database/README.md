# Database Setup Guide

## Quick Setup

1. **Make sure MySQL is running**

2. **Run the schema file:**
   ```bash
   mysql -u root -p < database/schema.sql
   ```
   
   Or in MySQL Workbench/command line:
   ```sql
   source C:/Users/Josh Cabunoc/Downloads/Notif1ed/database/schema.sql
   ```

3. **Verify the setup:**
   ```sql
   USE notified_DB;
   SHOW TABLES;
   ```

## Database Structure

### Tables Created:
- **users** - User accounts (admin, teachers, etc.)
- **students** - Student information
- **subjects** - Course/subject information
- **student_subjects** - Student enrollment in subjects
- **notifications** - Email notification logs
- **records** - General records/logs

### Sample Data Included:
- ✅ 1 admin user (email: admin@notified.com, password: admin123)
- ✅ 3 sample students
- ✅ 3 sample subjects
- ✅ 6 sample enrollments

## Database Configuration

The application connects using these settings (from `DatabaseConnectionn.java`):
- **Database:** notified_DB
- **Host:** localhost:3306
- **Username:** root
- **Password:** (empty - update if needed)

## Testing the Connection

Run your application with:
```powershell
.\run.ps1
```

The console will show:
- ✅ `Successfully connected to database: notified_DB` - if connection works
- ❌ Error messages - if there's a problem

## Troubleshooting

**Problem:** Connection refused
- Make sure MySQL service is running

**Problem:** Access denied
- Check username/password in `DatabaseConnectionn.java`

**Problem:** Unknown database
- Run the `schema.sql` file to create the database and tables
