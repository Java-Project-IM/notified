Public Class Student
    Public Property StudentID As Integer
    Public Property FirstName As String
    Public Property LastName As String
    Public Property StudentNumber As String
    Public Property Course As String
    Public Property AttendanceStatus As String
    Public Property AttendanceDate As Date

    Public Sub New()
        AttendanceDate = Date.Now
        AttendanceStatus = "Present"
    End Sub

    Public Sub New(id As Integer, firstName As String, lastName As String, studentNumber As String, course As String, status As String, attendanceDate As Date)
        Me.StudentID = id
        Me.FirstName = firstName
        Me.LastName = lastName
        Me.StudentNumber = studentNumber
        Me.Course = course
        Me.AttendanceStatus = status
        Me.AttendanceDate = attendanceDate
    End Sub

    Public ReadOnly Property FullName As String
        Get
            Return $"{FirstName} {LastName}"
        End Get
    End Property
End Class
