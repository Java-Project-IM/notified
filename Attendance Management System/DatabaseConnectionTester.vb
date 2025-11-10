Imports MySql.Data.MySqlClient
Imports System.Text

Public Class DatabaseConnectionTester
    ''' <summary>
    ''' Test MySQL database connection and return detailed status
    ''' </summary>
    Public Shared Function TestConnection() As String
        Dim result As New StringBuilder()
        result.AppendLine("=== MySQL Connection Test ===")
        result.AppendLine()

        ' Display connection settings (without password)
        result.AppendLine("Connection Settings:")
        result.AppendLine($"  Server: {DatabaseConfig.Server}")
        result.AppendLine($"  Port: {DatabaseConfig.Port}")
        result.AppendLine($"  Database: {DatabaseConfig.Database}")
        result.AppendLine($"  Username: {DatabaseConfig.Username}")
        result.AppendLine($"  Password: {If(String.IsNullOrEmpty(DatabaseConfig.Password), "(empty)", "***")}")
        result.AppendLine()

        Try
            ' Test 1: Basic Connection
            result.AppendLine("Test 1: Testing connection to MySQL server...")
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                result.AppendLine("  ✓ Connection successful!")
                result.AppendLine($"  Server Version: {connection.ServerVersion}")
                result.AppendLine()

                ' Test 2: Database Exists
                result.AppendLine("Test 2: Checking if database exists...")
                Dim dbExists As Boolean = CheckDatabaseExists(connection)
                If dbExists Then
                    result.AppendLine("  ✓ Database 'attendance_db' exists")
                Else
                    result.AppendLine("  ✗ Database 'attendance_db' does not exist")
                    result.AppendLine("  Tip: Run the application or execute database_schema.sql")
                End If
                result.AppendLine()

                ' Test 3: Tables Exist
                If dbExists Then
                    result.AppendLine("Test 3: Checking tables...")
                    Dim tables As List(Of String) = GetTables(connection)
                    If tables.Count > 0 Then
                        result.AppendLine($"  ✓ Found {tables.Count} table(s):")
                        For Each table In tables
                            result.AppendLine($"    - {table}")
                        Next
                    Else
                        result.AppendLine("  ✗ No tables found")
                        result.AppendLine("  Tip: Run database_schema.sql to create tables")
                    End If
                    result.AppendLine()

                    ' Test 4: Count Records
                    If tables.Contains("students") And tables.Contains("attendance_records") Then
                        result.AppendLine("Test 4: Counting records...")
                        Dim counts As Dictionary(Of String, Integer) = GetRecordCounts(connection)
                        For Each kvp In counts
                            result.AppendLine($"  {kvp.Key}: {kvp.Value} record(s)")
                        Next
                        result.AppendLine()
                    End If
                End If

                connection.Close()
            End Using

            result.AppendLine("=== All Tests Completed ===")
            result.AppendLine("Status: ✓ Connection is working properly")

        Catch ex As MySqlException
            result.AppendLine()
            result.AppendLine("=== ERROR ===")
            result.AppendLine($"MySQL Error Code: {ex.Number}")
            result.AppendLine($"Message: {ex.Message}")
            result.AppendLine()

            Select Case ex.Number
                Case 0
                    result.AppendLine("Solution: Check if MySQL service is running")
                Case 1042
                    result.AppendLine("Solution: Cannot connect to server. Check server address and port")
                Case 1045
                    result.AppendLine("Solution: Access denied. Check username and password in DatabaseConfig.vb")
                Case 1049
                    result.AppendLine("Solution: Database doesn't exist. Run database_schema.sql or let app create it")
                Case Else
                    result.AppendLine("Solution: Check MySQL error documentation")
            End Select

        Catch ex As Exception
            result.AppendLine()
            result.AppendLine("=== ERROR ===")
            result.AppendLine($"Error: {ex.Message}")
            result.AppendLine($"Type: {ex.GetType().Name}")
        End Try

        Return result.ToString()
    End Function

    ''' <summary>
    ''' Check if database exists
    ''' </summary>
    Private Shared Function CheckDatabaseExists(connection As MySqlConnection) As Boolean
        Try
            Dim query As String = $"SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '{DatabaseConfig.Database}'"
            Using cmd As New MySqlCommand(query, connection)
                Dim result = cmd.ExecuteScalar()
                Return result IsNot Nothing
            End Using
        Catch
            Return False
        End Try
    End Function

    ''' <summary>
    ''' Get list of tables in database
    ''' </summary>
    Private Shared Function GetTables(connection As MySqlConnection) As List(Of String)
        Dim tables As New List(Of String)()
        Try
            Dim query As String = "SHOW TABLES"
            Using cmd As New MySqlCommand(query, connection)
                Using reader As MySqlDataReader = cmd.ExecuteReader()
                    While reader.Read()
                        tables.Add(reader.GetString(0))
                    End While
                End Using
            End Using
        Catch
            ' Ignore errors
        End Try
        Return tables
    End Function

    ''' <summary>
    ''' Get record counts for main tables
    ''' </summary>
    Private Shared Function GetRecordCounts(connection As MySqlConnection) As Dictionary(Of String, Integer)
        Dim counts As New Dictionary(Of String, Integer)()

        Dim tables() As String = {"students", "attendance_records", "courses", "classes", "enrollments"}

        For Each table In tables
            Try
                Dim query As String = $"SELECT COUNT(*) FROM {table}"
                Using cmd As New MySqlCommand(query, connection)
                    Dim count As Integer = Convert.ToInt32(cmd.ExecuteScalar())
                    counts.Add(table, count)
                End Using
            Catch
                counts.Add(table, -1) ' Error counting
            End Try
        Next

        Return counts
    End Function

    ''' <summary>
    ''' Show connection test dialog
    ''' </summary>
    Public Shared Sub ShowConnectionTestDialog()
        Dim testResult As String = TestConnection()
        MessageBox.Show(testResult, "MySQL Connection Test", MessageBoxButtons.OK,
                       If(testResult.Contains("ERROR"), MessageBoxIcon.Error, MessageBoxIcon.Information))
    End Sub

    ''' <summary>
    ''' Quick test - returns true if connection is OK
    ''' </summary>
    Public Shared Function QuickTest() As Boolean
        Try
            Using connection As New MySqlConnection(DatabaseConfig.ConnectionString)
                connection.Open()
                connection.Close()
                Return True
            End Using
        Catch
            Return False
        End Try
    End Function
End Class
