Imports MySql.Data.MySqlClient

Public Class StudentCheckIn
    Private Sub StudentCheckIn_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        txtStudentNumber.Focus()
        UpdateClock()
        Timer1.Interval = 1000 ' Update clock every second
        Timer1.Start()
    End Sub

    Private Sub UpdateClock()
        lblCurrentTime.Text = DateTime.Now.ToString("hh:mm:ss tt")
        lblCurrentDate.Text = DateTime.Now.ToString("MMMM dd, yyyy - dddd")
    End Sub

    Private Sub Timer1_Tick(sender As Object, e As EventArgs) Handles Timer1.Tick
        UpdateClock()
    End Sub

    Private Sub btnCheckIn_Click(sender As Object, e As EventArgs) Handles btnCheckIn.Click
        If String.IsNullOrWhiteSpace(txtStudentNumber.Text) Then
            MessageBox.Show("Please enter your student number!", "Required Field", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            txtStudentNumber.Focus()
            Return
        End If

        Dim studentNumber As String = txtStudentNumber.Text.Trim()
        
        ' Check if student exists and mark attendance
        MarkAttendance(studentNumber)
    End Sub

    Private Sub MarkAttendance(studentNumber As String)
        Try
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                
                ' First, check if student exists
                Dim studentQuery As String = "SELECT student_id, first_name, last_name, course FROM students WHERE student_number = @StudentNumber AND status = 'Active'"
                Dim studentId As Integer = 0
                Dim firstName As String = ""
                Dim lastName As String = ""
                Dim course As String = ""
                
                Using cmd As New MySqlCommand(studentQuery, connection)
                    cmd.Parameters.AddWithValue("@StudentNumber", studentNumber)
                    Using reader As MySqlDataReader = cmd.ExecuteReader()
                        If reader.Read() Then
                            studentId = Convert.ToInt32(reader("student_id"))
                            firstName = reader("first_name").ToString()
                            lastName = reader("last_name").ToString()
                            course = reader("course").ToString()
                        Else
                            MessageBox.Show("Student number not found or inactive!", "Not Found", MessageBoxButtons.OK, MessageBoxIcon.Error)
                            txtStudentNumber.Clear()
                            txtStudentNumber.Focus()
                            Return
                        End If
                    End Using
                End Using
                
                ' Check if already checked in today
                Dim checkQuery As String = "SELECT COUNT(*) FROM attendance_records WHERE student_id = @StudentId AND attendance_date = @Today"
                Using cmd As New MySqlCommand(checkQuery, connection)
                    cmd.Parameters.AddWithValue("@StudentId", studentId)
                    cmd.Parameters.AddWithValue("@Today", DateTime.Now.Date)
                    
                    Dim count As Integer = Convert.ToInt32(cmd.ExecuteScalar())
                    If count > 0 Then
                        MessageBox.Show($"Welcome back {firstName} {lastName}!{vbCrLf}{vbCrLf}You have already checked in today.{vbCrLf}Course: {course}", 
                                      "Already Registered", MessageBoxButtons.OK, MessageBoxIcon.Information)
                        txtStudentNumber.Clear()
                        txtStudentNumber.Focus()
                        Return
                    End If
                End Using
                
                ' Determine if late (assuming 9:00 AM is the start time)
                Dim currentTime As TimeSpan = DateTime.Now.TimeOfDay
                Dim startTime As New TimeSpan(9, 0, 0) ' 9:00 AM
                Dim status As String = If(currentTime > startTime, "Late", "Present")
                
                ' Insert attendance record
                Dim insertQuery As String = "INSERT INTO attendance_records (student_id, attendance_date, attendance_time, status, remarks) 
                                            VALUES (@StudentId, @Date, @Time, @Status, @Remarks)"
                
                Using cmd As New MySqlCommand(insertQuery, connection)
                    cmd.Parameters.AddWithValue("@StudentId", studentId)
                    cmd.Parameters.AddWithValue("@Date", DateTime.Now.Date)
                    cmd.Parameters.AddWithValue("@Time", currentTime)
                    cmd.Parameters.AddWithValue("@Status", status)
                    cmd.Parameters.AddWithValue("@Remarks", If(status = "Late", "Checked in late", "On time"))
                    cmd.ExecuteNonQuery()
                End Using
                
                ' Show success message
                Dim statusMessage As String = If(status = "Late", "?? LATE", "? PRESENT")
                Dim statusColor As Color = If(status = "Late", Color.Orange, Color.Green)
                
                lblStatus.Text = statusMessage
                lblStatus.ForeColor = statusColor
                lblStudentInfo.Text = $"{firstName} {lastName}{vbCrLf}{course}{vbCrLf}Time: {DateTime.Now.ToString("hh:mm:ss tt")}"
                lblStudentInfo.Visible = True
                lblStatus.Visible = True
                
                ' Play a sound or visual feedback
                MessageBox.Show($"Check-in successful!{vbCrLf}{vbCrLf}Name: {firstName} {lastName}{vbCrLf}Course: {course}{vbCrLf}Status: {status}{vbCrLf}Time: {DateTime.Now.ToString("hh:mm:ss tt")}", 
                              "Success", MessageBoxButtons.OK, MessageBoxIcon.Information)
                
                txtStudentNumber.Clear()
                txtStudentNumber.Focus()
                
                ' Hide status after 3 seconds
                Dim hideTimer As New Timer()
                hideTimer.Interval = 3000
                AddHandler hideTimer.Tick, Sub()
                                               lblStatus.Visible = False
                                               lblStudentInfo.Visible = False
                                               hideTimer.Stop()
                                           End Sub
                hideTimer.Start()
                
            End Using
        Catch ex As Exception
            MessageBox.Show($"Error recording attendance: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    Private Sub btnBack_Click(sender As Object, e As EventArgs) Handles btnBack.Click
        Timer1.Stop()
        Me.Close()
    End Sub

    Private Sub txtStudentNumber_KeyPress(sender As Object, e As KeyPressEventArgs) Handles txtStudentNumber.KeyPress
        ' Allow Enter key to submit
        If e.KeyChar = ChrW(Keys.Enter) Then
            e.Handled = True
            btnCheckIn_Click(sender, e)
        End If
    End Sub
End Class
