Public Class LoginForm
    Private Sub LoginForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        ' Center form on screen
        Me.CenterToScreen()
    End Sub

    Private Sub btnAdmin_Click(sender As Object, e As EventArgs) Handles btnAdmin.Click
        ' Show admin login dialog
        Dim password As String = InputBox("Enter Admin Password:", "Admin Login", "")
        
        ' Simple password check (you can enhance this with database authentication)
        If password = "admin123" Then
            Dim adminForm As New AdminDashboard()
            Me.Hide()
            adminForm.ShowDialog()
            Me.Show()
        ElseIf Not String.IsNullOrEmpty(password) Then
            MessageBox.Show("Incorrect password!", "Access Denied", MessageBoxButtons.OK, MessageBoxIcon.Error)
        End If
    End Sub

    Private Sub btnStudent_Click(sender As Object, e As EventArgs) Handles btnStudent.Click
        ' Open student check-in form
        Dim studentForm As New StudentCheckIn()
        Me.Hide()
        studentForm.ShowDialog()
        Me.Show()
    End Sub

    Private Sub btnExit_Click(sender As Object, e As EventArgs) Handles btnExit.Click
        Application.Exit()
    End Sub
End Class
