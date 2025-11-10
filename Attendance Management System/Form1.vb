Public Class Form1
    Private selectedStudentID As Integer = 0

    Private Sub Form1_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        ' Test database connection first
        If Not DatabaseConnectionTester.QuickTest() Then
            Dim result As DialogResult = MessageBox.Show(
                "Unable to connect to MySQL database. Would you like to see connection details?",
                "Database Connection Error",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning
            )

            If result = DialogResult.Yes Then
                DatabaseConnectionTester.ShowConnectionTestDialog()
            End If
        Else
            ' Initialize database
            DatabaseHelper.InitializeDatabase()
        End If

        ' Set default status
        cboStatus.SelectedIndex = 0

        ' Load all students
        LoadStudents()

        ' Configure DataGridView
        ConfigureDataGridView()
    End Sub

    Private Sub ConfigureDataGridView()
        ' Set column headers and formatting
        dgvStudents.AutoGenerateColumns = True
        dgvStudents.SelectionMode = DataGridViewSelectionMode.FullRowSelect
        dgvStudents.MultiSelect = False
        dgvStudents.AllowUserToAddRows = False
        dgvStudents.AllowUserToDeleteRows = False
        dgvStudents.ReadOnly = True
        dgvStudents.RowHeadersVisible = False
    End Sub

    Private Sub LoadStudents()
        Try
            Dim students As List(Of Student) = DatabaseHelper.GetAllStudents()
            dgvStudents.DataSource = Nothing
            dgvStudents.DataSource = students

            ' Format columns if data exists
            If dgvStudents.Columns.Count > 0 Then
                dgvStudents.Columns("StudentID").HeaderText = "ID"
                dgvStudents.Columns("StudentID").Width = 50
                dgvStudents.Columns("FirstName").HeaderText = "First Name"
                dgvStudents.Columns("LastName").HeaderText = "Last Name"
                dgvStudents.Columns("StudentNumber").HeaderText = "Student #"
                dgvStudents.Columns("Course").HeaderText = "Course"
                dgvStudents.Columns("AttendanceStatus").HeaderText = "Status"
                dgvStudents.Columns("AttendanceDate").HeaderText = "Date"
                dgvStudents.Columns("AttendanceDate").DefaultCellStyle.Format = "MM/dd/yyyy hh:mm tt"

                ' Hide FullName property column if it exists
                If dgvStudents.Columns.Contains("FullName") Then
                    dgvStudents.Columns("FullName").Visible = False
                End If
            End If
        Catch ex As Exception
            MessageBox.Show($"Error loading students: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End Try
    End Sub

    Private Sub btnAdd_Click(sender As Object, e As EventArgs) Handles btnAdd.Click
        If ValidateInput() Then
            Dim newStudent As New Student With {
                .FirstName = txtFirstName.Text.Trim(),
                .LastName = txtLastName.Text.Trim(),
                .StudentNumber = txtStudentNumber.Text.Trim(),
                .Course = txtCourse.Text.Trim(),
                .AttendanceStatus = cboStatus.SelectedItem.ToString(),
                .AttendanceDate = dtpAttendanceDate.Value
            }

            If DatabaseHelper.AddStudent(newStudent) Then
                MessageBox.Show("Student attendance record added successfully!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information)
                LoadStudents()
                ClearInputs()
            End If
        End If
    End Sub

    Private Sub btnUpdate_Click(sender As Object, e As EventArgs) Handles btnUpdate.Click
        If selectedStudentID = 0 Then
            MessageBox.Show("Please select a student record to update.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        If ValidateInput() Then
            Dim updatedStudent As New Student With {
                .StudentID = selectedStudentID,
                .FirstName = txtFirstName.Text.Trim(),
                .LastName = txtLastName.Text.Trim(),
                .StudentNumber = txtStudentNumber.Text.Trim(),
                .Course = txtCourse.Text.Trim(),
                .AttendanceStatus = cboStatus.SelectedItem.ToString(),
                .AttendanceDate = dtpAttendanceDate.Value
            }

            If DatabaseHelper.UpdateStudent(updatedStudent) Then
                MessageBox.Show("Student attendance record updated successfully!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information)
                LoadStudents()
                ClearInputs()
            End If
        End If
    End Sub

    Private Sub btnDelete_Click(sender As Object, e As EventArgs) Handles btnDelete.Click
        If selectedStudentID = 0 Then
            MessageBox.Show("Please select a student record to delete.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        Dim result As DialogResult = MessageBox.Show(
            "Are you sure you want to delete this attendance record?",
            "Confirm Delete",
            MessageBoxButtons.YesNo,
            MessageBoxIcon.Question
        )

        If result = DialogResult.Yes Then
            If DatabaseHelper.DeleteStudent(selectedStudentID) Then
                MessageBox.Show("Student attendance record deleted successfully!", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information)
                LoadStudents()
                ClearInputs()
            End If
        End If
    End Sub

    Private Sub btnClear_Click(sender As Object, e As EventArgs) Handles btnClear.Click
        ClearInputs()
    End Sub

    Private Sub btnSearch_Click(sender As Object, e As EventArgs) Handles btnSearch.Click
        If String.IsNullOrWhiteSpace(txtSearch.Text) Then
            MessageBox.Show("Please enter a search term.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        Dim searchResults As List(Of Student) = DatabaseHelper.SearchStudents(txtSearch.Text.Trim())
        dgvStudents.DataSource = Nothing
        dgvStudents.DataSource = searchResults

        If searchResults.Count = 0 Then
            MessageBox.Show("No records found matching your search.", "Information", MessageBoxButtons.OK, MessageBoxIcon.Information)
        End If
    End Sub

    Private Sub btnRefresh_Click(sender As Object, e As EventArgs) Handles btnRefresh.Click
        LoadStudents()
        txtSearch.Clear()
        ClearInputs()
    End Sub

    Private Sub dgvStudents_CellClick(sender As Object, e As DataGridViewCellEventArgs) Handles dgvStudents.CellClick
        If e.RowIndex >= 0 Then
            Try
                Dim row As DataGridViewRow = dgvStudents.Rows(e.RowIndex)

                selectedStudentID = Convert.ToInt32(row.Cells("StudentID").Value)
                txtFirstName.Text = row.Cells("FirstName").Value.ToString()
                txtLastName.Text = row.Cells("LastName").Value.ToString()
                txtStudentNumber.Text = row.Cells("StudentNumber").Value.ToString()
                txtCourse.Text = row.Cells("Course").Value.ToString()
                cboStatus.SelectedItem = row.Cells("AttendanceStatus").Value.ToString()
                dtpAttendanceDate.Value = Convert.ToDateTime(row.Cells("AttendanceDate").Value)
            Catch ex As Exception
                MessageBox.Show($"Error selecting record: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error)
            End Try
        End If
    End Sub

    Private Function ValidateInput() As Boolean
        If String.IsNullOrWhiteSpace(txtFirstName.Text) Then
            MessageBox.Show("Please enter first name.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            txtFirstName.Focus()
            Return False
        End If

        If String.IsNullOrWhiteSpace(txtLastName.Text) Then
            MessageBox.Show("Please enter last name.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            txtLastName.Focus()
            Return False
        End If

        If String.IsNullOrWhiteSpace(txtStudentNumber.Text) Then
            MessageBox.Show("Please enter student number.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            txtStudentNumber.Focus()
            Return False
        End If

        If String.IsNullOrWhiteSpace(txtCourse.Text) Then
            MessageBox.Show("Please enter course.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            txtCourse.Focus()
            Return False
        End If

        If cboStatus.SelectedIndex = -1 Then
            MessageBox.Show("Please select attendance status.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            cboStatus.Focus()
            Return False
        End If

        Return True
    End Function

    Private Sub ClearInputs()
        selectedStudentID = 0
        txtFirstName.Clear()
        txtLastName.Clear()
        txtStudentNumber.Clear()
        txtCourse.Clear()
        cboStatus.SelectedIndex = 0
        dtpAttendanceDate.Value = Date.Now
        txtFirstName.Focus()
    End Sub

    ' Double-click on title to test database connection
    Private Sub lblTitle_DoubleClick(sender As Object, e As EventArgs) Handles lblTitle.DoubleClick
        DatabaseConnectionTester.ShowConnectionTestDialog()
    End Sub
End Class
