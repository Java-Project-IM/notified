Imports MySql.Data.MySqlClient

Public Class AdminDashboard
    Private Sub AdminDashboard_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        ' Initialize date pickers
        dtpStartDate.Value = DateTime.Now.Date
        dtpEndDate.Value = DateTime.Now.Date
        
        ' Load today's attendance
        LoadTodayAttendance()
        
        ' Load statistics
        LoadStatistics()
    End Sub

    Private Sub LoadTodayAttendance()
        Try
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                
                Dim query As String = "
                    SELECT 
                        a.attendance_id AS 'ID',
                        s.student_number AS 'Student Number',
                        CONCAT(s.first_name, ' ', s.last_name) AS 'Full Name',
                        s.course AS 'Course',
                        TIME_FORMAT(a.attendance_time, '%h:%i %p') AS 'Time',
                        a.status AS 'Status',
                        a.remarks AS 'Remarks'
                    FROM attendance_records a
                    INNER JOIN students s ON a.student_id = s.student_id
                    WHERE a.attendance_date = @Today
                    ORDER BY a.attendance_time DESC"
                
                Using cmd As New MySqlCommand(query, connection)
                    cmd.Parameters.AddWithValue("@Today", DateTime.Now.Date)
                    
                    Using adapter As New MySqlDataAdapter(cmd)
                        Dim dt As New DataTable()
                        adapter.Fill(dt)
                        dgvTodayAttendance.DataSource = dt
                        
                        ' Format grid
                        If dgvTodayAttendance.Columns.Count > 0 Then
                            dgvTodayAttendance.Columns("ID").Width = 50
                            dgvTodayAttendance.Columns("Student Number").Width = 120
                            dgvTodayAttendance.Columns("Full Name").Width = 150
                            dgvTodayAttendance.Columns("Course").Width = 150
                            dgvTodayAttendance.Columns("Time").Width = 100
                            dgvTodayAttendance.Columns("Status").Width = 100
                        End If
                    End Using
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error loading attendance: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    Private Sub LoadStatistics()
        Try
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                
                ' Get today's statistics
                Dim statsQuery As String = "
                    SELECT 
                        COUNT(*) AS total,
                        SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS present,
                        SUM(CASE WHEN status = 'Late' THEN 1 ELSE 0 END) AS late,
                        SUM(CASE WHEN status = 'Absent' THEN 1 ELSE 0 END) AS absent,
                        SUM(CASE WHEN status = 'Excused' THEN 1 ELSE 0 END) AS excused
                    FROM attendance_records
                    WHERE attendance_date = @Today"
                
                Using cmd As New MySqlCommand(statsQuery, connection)
                    cmd.Parameters.AddWithValue("@Today", DateTime.Now.Date)
                    Using reader As MySqlDataReader = cmd.ExecuteReader()
                        If reader.Read() Then
                            lblTotalToday.Text = reader("total").ToString()
                            lblPresentToday.Text = reader("present").ToString()
                            lblLateToday.Text = reader("late").ToString()
                            lblAbsentToday.Text = reader("absent").ToString()
                        End If
                    End Using
                End Using
                
                ' Get total students
                Dim studentQuery As String = "SELECT COUNT(*) FROM students WHERE status = 'Active'"
                Using cmd As New MySqlCommand(studentQuery, connection)
                    lblTotalStudents.Text = cmd.ExecuteScalar().ToString()
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error loading statistics: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    Private Sub btnRefresh_Click(sender As Object, e As EventArgs) Handles btnRefresh.Click
        LoadTodayAttendance()
        LoadStatistics()
        MessageBox.Show("Data refreshed!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information)
    End Sub

    Private Sub btnSearchByDate_Click(sender As Object, e As EventArgs) Handles btnSearchByDate.Click
        Try
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                
                Dim query As String = "
                    SELECT 
                        a.attendance_id AS 'ID',
                        s.student_number AS 'Student Number',
                        CONCAT(s.first_name, ' ', s.last_name) AS 'Full Name',
                        s.course AS 'Course',
                        DATE_FORMAT(a.attendance_date, '%Y-%m-%d') AS 'Date',
                        TIME_FORMAT(a.attendance_time, '%h:%i %p') AS 'Time',
                        a.status AS 'Status',
                        a.remarks AS 'Remarks'
                    FROM attendance_records a
                    INNER JOIN students s ON a.student_id = s.student_id
                    WHERE a.attendance_date BETWEEN @StartDate AND @EndDate
                    ORDER BY a.attendance_date DESC, a.attendance_time DESC"
                
                Using cmd As New MySqlCommand(query, connection)
                    cmd.Parameters.AddWithValue("@StartDate", dtpStartDate.Value.Date)
                    cmd.Parameters.AddWithValue("@EndDate", dtpEndDate.Value.Date)
                    
                    Using adapter As New MySqlDataAdapter(cmd)
                        Dim dt As New DataTable()
                        adapter.Fill(dt)
                        dgvTodayAttendance.DataSource = dt
                        
                        MessageBox.Show($"Found {dt.Rows.Count} record(s)", "Search Results", MessageBoxButtons.OK, MessageBoxIcon.Information)
                    End Using
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error searching: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    Private Sub btnSearchStudent_Click(sender As Object, e As EventArgs) Handles btnSearchStudent.Click
        If String.IsNullOrWhiteSpace(txtSearchStudent.Text) Then
            MessageBox.Show("Please enter a student number or name!", "Required", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If
        
        Try
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                
                Dim query As String = "
                    SELECT 
                        a.attendance_id AS 'ID',
                        s.student_number AS 'Student Number',
                        CONCAT(s.first_name, ' ', s.last_name) AS 'Full Name',
                        s.course AS 'Course',
                        DATE_FORMAT(a.attendance_date, '%Y-%m-%d') AS 'Date',
                        TIME_FORMAT(a.attendance_time, '%h:%i %p') AS 'Time',
                        a.status AS 'Status',
                        a.remarks AS 'Remarks'
                    FROM attendance_records a
                    INNER JOIN students s ON a.student_id = s.student_id
                    WHERE s.student_number LIKE @Search 
                       OR s.first_name LIKE @Search 
                       OR s.last_name LIKE @Search
                    ORDER BY a.attendance_date DESC, a.attendance_time DESC"
                
                Using cmd As New MySqlCommand(query, connection)
                    cmd.Parameters.AddWithValue("@Search", $"%{txtSearchStudent.Text.Trim()}%")
                    
                    Using adapter As New MySqlDataAdapter(cmd)
                        Dim dt As New DataTable()
                        adapter.Fill(dt)
                        dgvTodayAttendance.DataSource = dt
                        
                        If dt.Rows.Count = 0 Then
                            MessageBox.Show("No records found!", "Search Results", MessageBoxButtons.OK, MessageBoxIcon.Information)
                        End If
                    End Using
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error searching: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    Private Sub btnExportToday_Click(sender As Object, e As EventArgs) Handles btnExportToday.Click
        Try
            ' Simple export to text file
            Dim saveDialog As New SaveFileDialog()
            saveDialog.Filter = "Text Files (*.txt)|*.txt|CSV Files (*.csv)|*.csv"
            saveDialog.FileName = $"Attendance_{DateTime.Now:yyyyMMdd}.txt"
            
            If saveDialog.ShowDialog() = DialogResult.OK Then
                Using writer As New IO.StreamWriter(saveDialog.FileName)
                    writer.WriteLine($"Attendance Report - {DateTime.Now:yyyy-MM-dd}")
                    writer.WriteLine(New String("="c, 80))
                    writer.WriteLine()
                    
                    ' Write headers
                    writer.WriteLine("Student Number" & vbTab & "Name" & vbTab & "Course" & vbTab & "Time" & vbTab & "Status")
                    writer.WriteLine(New String("-"c, 80))
                    
                    ' Write data
                    For Each row As DataGridViewRow In dgvTodayAttendance.Rows
                        If Not row.IsNewRow Then
                            writer.WriteLine($"{row.Cells("Student Number").Value}" & vbTab & 
                                           $"{row.Cells("Full Name").Value}" & vbTab & 
                                           $"{row.Cells("Course").Value}" & vbTab & 
                                           $"{row.Cells("Time").Value}" & vbTab & 
                                           $"{row.Cells("Status").Value}")
                        End If
                    Next
                    
                    writer.WriteLine()
                    writer.WriteLine($"Total Records: {dgvTodayAttendance.Rows.Count - 1}")
                End Using
                
                MessageBox.Show("Report exported successfully!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information)
            End If
        Catch ex As Exception
            MessageBox.Show($"Error exporting: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    Private Sub btnManageStudents_Click(sender As Object, e As EventArgs) Handles btnManageStudents.Click
        ' Open the original Form1 for full CRUD operations
        Dim manageForm As New Form1()
        manageForm.ShowDialog()
    End Sub

    Private Sub btnClose_Click(sender As Object, e As EventArgs) Handles btnClose.Click
        Me.Close()
    End Sub

    Private Sub btnViewReports_Click(sender As Object, e As EventArgs) Handles btnViewReports.Click
        ' Show attendance summary report
        Try
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                
                Dim query As String = "SELECT * FROM vw_student_attendance_summary ORDER BY attendance_percentage DESC"
                
                Using adapter As New MySqlDataAdapter(query, connection)
                    Dim dt As New DataTable()
                    adapter.Fill(dt)
                    
                    Dim reportForm As New Form()
                    reportForm.Text = "Attendance Summary Report"
                    reportForm.Size = New Size(900, 600)
                    reportForm.StartPosition = FormStartPosition.CenterScreen
                    
                    Dim dgv As New DataGridView()
                    dgv.Dock = DockStyle.Fill
                    dgv.DataSource = dt
                    dgv.ReadOnly = True
                    dgv.AllowUserToAddRows = False
                    dgv.SelectionMode = DataGridViewSelectionMode.FullRowSelect
                    dgv.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill
                    
                    reportForm.Controls.Add(dgv)
                    reportForm.ShowDialog()
                End Using
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error loading report: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub
End Class
