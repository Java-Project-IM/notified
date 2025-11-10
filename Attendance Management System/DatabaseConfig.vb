Public Class DatabaseConfig
    ' MySQL Connection Settings
    ' IMPORTANT: Update these values according to your MySQL setup
    Public Shared ReadOnly Server As String = "localhost"
    Public Shared ReadOnly Port As String = "3306"
    Public Shared ReadOnly Database As String = "attendance_db"
    Public Shared ReadOnly Username As String = "root"
    Public Shared ReadOnly Password As String = ""

    ' Build connection string
    Public Shared ReadOnly Property ConnectionString As String
        Get
            Return $"Server={Server};Port={Port};Database={Database};Uid={Username};Pwd={Password};CharSet=utf8mb4;"
        End Get
    End Property
End Class
