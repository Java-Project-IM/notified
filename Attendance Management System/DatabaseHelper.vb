Imports MySql.Data.MySqlClient

Public Class DatabaseHelper
    Private Shared ReadOnly connectionString As String = DatabaseConfig.ConnectionString

    ' Initialize Database and Tables
    Public Shared Sub InitializeDatabase()
        Try
            ' First, try to create the database if it doesn't exist
            CreateDatabaseIfNotExists()

            ' Then create tables
            CreateTables()

            MessageBox.Show("Database initialized successfully!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information)
        Catch ex As Exception
            MessageBox.Show($"Error initializing database: {ex.Message}", "Database Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    ' Create database if it doesn't exist
    Private Shared Sub CreateDatabaseIfNotExists()
        Dim connectionStringWithoutDb As String = $"Server={DatabaseConfig.Server};Port={DatabaseConfig.Port};Uid={DatabaseConfig.Username};Pwd={DatabaseConfig.Password};"

        Using connection As New MySqlConnection(connectionStringWithoutDb)
            connection.Open()

            Dim createDbQuery As String = $"CREATE DATABASE IF NOT EXISTS {DatabaseConfig.Database} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
            Using command As New MySqlCommand(createDbQuery, connection)
                command.ExecuteNonQuery()
            End Using
        End Using
    End Sub

    ' Create all necessary tables
    Private Shared Sub CreateTables()
        Using connection As New MySqlConnection(connectionString)
            connection.Open()

            ' Create Students table
            Dim createStudentsTable As String = "
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
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"

            Using command As New MySqlCommand(createStudentsTable, connection)
                command.ExecuteNonQuery()
            End Using

            ' Create Attendance Records table
            Dim createAttendanceTable As String = "
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
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"

            Using command As New MySqlCommand(createAttendanceTable, connection)
                command.ExecuteNonQuery()
            End Using

            ' Create Courses table
            Dim createCoursesTable As String = "
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
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"

            Using command As New MySqlCommand(createCoursesTable, connection)
                command.ExecuteNonQuery()
            End Using

            ' Create Classes/Sessions table
            Dim createClassesTable As String = "
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
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"

            Using command As New MySqlCommand(createClassesTable, connection)
                command.ExecuteNonQuery()
            End Using

            ' Create Student Enrollments table
            Dim createEnrollmentsTable As String = "
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
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"

            Using command As New MySqlCommand(createEnrollmentsTable, connection)
                command.ExecuteNonQuery()
            End Using
        End Using
    End Sub

    ' CREATE - Add new student attendance record
    Public Shared Function AddStudent(student As Student) As Boolean
        Try
            Using connection As New MySqlConnection(connectionString)
                connection.Open()

                ' First check if student exists, if not create
                Dim studentId As Integer = GetOrCreateStudent(connection, student)

                ' Then add attendance record
                Dim query As String = "INSERT INTO attendance_records (student_id, attendance_date, attendance_time, status, remarks) 
                                      VALUES (@StudentId, @AttendanceDate, @AttendanceTime, @Status, @Remarks)"

                Using command As New MySqlCommand(query, connection)
                    command.Parameters.AddWithValue("@StudentId", studentId)
                    command.Parameters.AddWithValue("@AttendanceDate", student.AttendanceDate.Date)
                    command.Parameters.AddWithValue("@AttendanceTime", student.AttendanceDate.TimeOfDay)
                    command.Parameters.AddWithValue("@Status", student.AttendanceStatus)
                    command.Parameters.AddWithValue("@Remarks", "")
                    command.ExecuteNonQuery()
                End Using
            End Using
            Return True
        Catch ex As Exception
            MessageBox.Show($"Error adding attendance: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
            Return False
        End Try
    End Function

    ' Helper function to get or create student
    Private Shared Function GetOrCreateStudent(connection As MySqlConnection, student As Student) As Integer
        ' Check if student exists
        Dim checkQuery As String = "SELECT student_id FROM students WHERE student_number = @StudentNumber"
        Using checkCmd As New MySqlCommand(checkQuery, connection)
            checkCmd.Parameters.AddWithValue("@StudentNumber", student.StudentNumber)
            Dim result = checkCmd.ExecuteScalar()

            If result IsNot Nothing Then
                Return Convert.ToInt32(result)
            End If
        End Using

        ' If not exists, create new student
        Dim insertQuery As String = "INSERT INTO students (first_name, last_name, student_number, course, date_enrolled, status) 
                                    VALUES (@FirstName, @LastName, @StudentNumber, @Course, @DateEnrolled, 'Active');
                                    SELECT LAST_INSERT_ID();"

        Using insertCmd As New MySqlCommand(insertQuery, connection)
            insertCmd.Parameters.AddWithValue("@FirstName", student.FirstName)
            insertCmd.Parameters.AddWithValue("@LastName", student.LastName)
            insertCmd.Parameters.AddWithValue("@StudentNumber", student.StudentNumber)
            insertCmd.Parameters.AddWithValue("@Course", student.Course)
            insertCmd.Parameters.AddWithValue("@DateEnrolled", Date.Now)
            Return Convert.ToInt32(insertCmd.ExecuteScalar())
        End Using
    End Function

    ' READ - Get all students with attendance
    Public Shared Function GetAllStudents() As List(Of Student)
        Dim students As New List(Of Student)()

        Try
            Using connection As New MySqlConnection(connectionString)
                connection.Open()
                Dim query As String = "
                    SELECT 
                        a.attendance_id,
                        s.student_id,
                        s.first_name,
                        s.last_name,
                        s.student_number,
                        s.course,
                        a.attendance_date,
                        a.attendance_time,
                        a.status
                    FROM attendance_records a
                    INNER JOIN students s ON a.student_id = s.student_id
                    ORDER BY a.attendance_date DESC, a.attendance_time DESC"

                Using command As New MySqlCommand(query, connection)
                    Using reader As MySqlDataReader = command.ExecuteReader()
                        While reader.Read()
                            Dim attendanceDate As Date = Convert.ToDateTime(reader("attendance_date"))
                            Dim attendanceTime As TimeSpan = CType(reader("attendance_time"), TimeSpan)

                            Dim student As New Student() With {
                                .StudentID = Convert.ToInt32(reader("attendance_id")),
                                .FirstName = reader("first_name").ToString(),
                                .LastName = reader("last_name").ToString(),
                                .StudentNumber = reader("student_number").ToString(),
                                .Course = reader("course").ToString(),
                                .AttendanceStatus = reader("status").ToString(),
                                .AttendanceDate = attendanceDate.Add(attendanceTime)
                            }
                            students.Add(student)
                        End While
                    End Using
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error retrieving students: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try

        Return students
    End Function

    ' UPDATE - Update existing attendance record
    Public Shared Function UpdateStudent(student As Student) As Boolean
        Try
            Using connection As New MySqlConnection(connectionString)
                connection.Open()
                Dim query As String = "UPDATE attendance_records 
                                      SET attendance_date = @AttendanceDate, 
                                          attendance_time = @AttendanceTime, 
                                          status = @Status 
                                      WHERE attendance_id = @AttendanceId"

                Using command As New MySqlCommand(query, connection)
                    command.Parameters.AddWithValue("@AttendanceId", student.StudentID)
                    command.Parameters.AddWithValue("@AttendanceDate", student.AttendanceDate.Date)
                    command.Parameters.AddWithValue("@AttendanceTime", student.AttendanceDate.TimeOfDay)
                    command.Parameters.AddWithValue("@Status", student.AttendanceStatus)
                    command.ExecuteNonQuery()
                End Using
            End Using
            Return True
        Catch ex As Exception
            MessageBox.Show($"Error updating attendance: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
            Return False
        End Try
    End Function

    ' DELETE - Delete attendance record
    Public Shared Function DeleteStudent(attendanceId As Integer) As Boolean
        Try
            Using connection As New MySqlConnection(connectionString)
                connection.Open()
                Dim query As String = "DELETE FROM attendance_records WHERE attendance_id = @AttendanceId"

                Using command As New MySqlCommand(query, connection)
                    command.Parameters.AddWithValue("@AttendanceId", attendanceId)
                    command.ExecuteNonQuery()
                End Using
            End Using
            Return True
        Catch ex As Exception
            MessageBox.Show($"Error deleting attendance: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
            Return False
        End Try
    End Function

    ' Search functionality
    Public Shared Function SearchStudents(searchTerm As String) As List(Of Student)
        Dim students As New List(Of Student)()

        Try
            Using connection As New MySqlConnection(connectionString)
                connection.Open()
                Dim query As String = "
                    SELECT 
                        a.attendance_id,
                        s.student_id,
                        s.first_name,
                        s.last_name,
                        s.student_number,
                        s.course,
                        a.attendance_date,
                        a.attendance_time,
                        a.status
                    FROM attendance_records a
                    INNER JOIN students s ON a.student_id = s.student_id
                    WHERE s.first_name LIKE @SearchTerm 
                       OR s.last_name LIKE @SearchTerm 
                       OR s.student_number LIKE @SearchTerm 
                       OR s.student_number LIKE @SearchTerm 
                       OR s.course LIKE @SearchTerm
                    ORDER BY a.attendance_date DESC, a.attendance_time DESC"

                Using command As New MySqlCommand(query, connection)
                    command.Parameters.AddWithValue("@SearchTerm", $"%{searchTerm}%")

                    Using reader As MySqlDataReader = command.ExecuteReader()
                        While reader.Read()
                            Dim attendanceDate As Date = Convert.ToDateTime(reader("attendance_date"))
                            Dim attendanceTime As TimeSpan = CType(reader("attendance_time"), TimeSpan)

                            Dim student As New Student() With {
                                .StudentID = Convert.ToInt32(reader("attendance_id")),
                                .FirstName = reader("first_name").ToString(),
                                .LastName = reader("last_name").ToString(),
                                .StudentNumber = reader("student_number").ToString(),
                                .Course = reader("course").ToString(),
                                .AttendanceStatus = reader("status").ToString(),
                                .AttendanceDate = attendanceDate.Add(attendanceTime)
                            }
                            students.Add(student)
                        End While
                    End Using
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error searching students: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try

        Return students
    End Function

    ' Additional helper methods for reporting

    ' Get attendance statistics
    Public Shared Function GetAttendanceStatistics(startDate As Date, endDate As Date) As DataTable
        Dim dt As New DataTable()

        Try
            Using connection As New MySqlConnection(connectionString)
                connection.Open()
                Dim query As String = "
                    SELECT 
                        s.course,
                        a.status,
                        COUNT(*) as count
                    FROM attendance_records a
                    INNER JOIN students s ON a.student_id = s.student_id
                    WHERE a.attendance_date BETWEEN @StartDate AND @EndDate
                    GROUP BY s.course, a.status
                    ORDER BY s.course, a.status"

                Using command As New MySqlCommand(query, connection)
                    command.Parameters.AddWithValue("@StartDate", startDate)
                    command.Parameters.AddWithValue("@EndDate", endDate)

                    Using adapter As New MySqlDataAdapter(command)
                        adapter.Fill(dt)
                    End Using
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error getting statistics: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try

        Return dt
    End Function
End Class
