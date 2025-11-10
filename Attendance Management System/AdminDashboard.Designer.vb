<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class AdminDashboard
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()>
    Protected Overrides Sub Dispose(disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()>
    Private Sub InitializeComponent()
        Me.Panel1 = New System.Windows.Forms.Panel()
        Me.lblTitle = New System.Windows.Forms.Label()
        Me.GroupBox1 = New System.Windows.Forms.GroupBox()
        Me.lblAbsentToday = New System.Windows.Forms.Label()
        Me.Label6 = New System.Windows.Forms.Label()
        Me.lblLateToday = New System.Windows.Forms.Label()
        Me.Label4 = New System.Windows.Forms.Label()
        Me.lblPresentToday = New System.Windows.Forms.Label()
        Me.Label3 = New System.Windows.Forms.Label()
        Me.lblTotalToday = New System.Windows.Forms.Label()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.lblTotalStudents = New System.Windows.Forms.Label()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.GroupBox2 = New System.Windows.Forms.GroupBox()
        Me.btnSearchByDate = New System.Windows.Forms.Button()
        Me.dtpEndDate = New System.Windows.Forms.DateTimePicker()
        Me.Label8 = New System.Windows.Forms.Label()
        Me.dtpStartDate = New System.Windows.Forms.DateTimePicker()
        Me.Label7 = New System.Windows.Forms.Label()
        Me.dgvTodayAttendance = New System.Windows.Forms.DataGridView()
        Me.Label5 = New System.Windows.Forms.Label()
        Me.btnRefresh = New System.Windows.Forms.Button()
        Me.btnManageStudents = New System.Windows.Forms.Button()
        Me.btnExportToday = New System.Windows.Forms.Button()
        Me.btnClose = New System.Windows.Forms.Button()
        Me.GroupBox3 = New System.Windows.Forms.GroupBox()
        Me.btnSearchStudent = New System.Windows.Forms.Button()
        Me.txtSearchStudent = New System.Windows.Forms.TextBox()
        Me.Label9 = New System.Windows.Forms.Label()
        Me.btnViewReports = New System.Windows.Forms.Button()
        Me.Panel1.SuspendLayout()
        Me.GroupBox1.SuspendLayout()
        Me.GroupBox2.SuspendLayout()
        CType(Me.dgvTodayAttendance, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.GroupBox3.SuspendLayout()
        Me.SuspendLayout()
        '
        'Panel1
        '
        Me.Panel1.BackColor = System.Drawing.Color.FromArgb(CType(CType(52, Byte), Integer), CType(CType(152, Byte), Integer), CType(CType(219, Byte), Integer))
        Me.Panel1.Controls.Add(Me.lblTitle)
        Me.Panel1.Dock = System.Windows.Forms.DockStyle.Top
        Me.Panel1.Location = New System.Drawing.Point(0, 0)
        Me.Panel1.Name = "Panel1"
        Me.Panel1.Size = New System.Drawing.Size(1200, 80)
        Me.Panel1.TabIndex = 0
        '
        'lblTitle
        '
        Me.lblTitle.AutoSize = True
        Me.lblTitle.Font = New System.Drawing.Font("Segoe UI", 24.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblTitle.ForeColor = System.Drawing.Color.White
        Me.lblTitle.Location = New System.Drawing.Point(400, 20)
        Me.lblTitle.Name = "lblTitle"
        Me.lblTitle.Size = New System.Drawing.Size(400, 45)
        Me.lblTitle.TabIndex = 0
        Me.lblTitle.Text = "ADMIN DASHBOARD"
        '
        'GroupBox1
        '
        Me.GroupBox1.Controls.Add(Me.lblAbsentToday)
        Me.GroupBox1.Controls.Add(Me.Label6)
        Me.GroupBox1.Controls.Add(Me.lblLateToday)
        Me.GroupBox1.Controls.Add(Me.Label4)
        Me.GroupBox1.Controls.Add(Me.lblPresentToday)
        Me.GroupBox1.Controls.Add(Me.Label3)
        Me.GroupBox1.Controls.Add(Me.lblTotalToday)
        Me.GroupBox1.Controls.Add(Me.Label2)
        Me.GroupBox1.Controls.Add(Me.lblTotalStudents)
        Me.GroupBox1.Controls.Add(Me.Label1)
        Me.GroupBox1.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.GroupBox1.Location = New System.Drawing.Point(20, 100)
        Me.GroupBox1.Name = "GroupBox1"
        Me.GroupBox1.Size = New System.Drawing.Size(1160, 100)
        Me.GroupBox1.TabIndex = 1
        Me.GroupBox1.TabStop = False
        Me.GroupBox1.Text = "Today's Statistics"
        '
        'lblAbsentToday
        '
        Me.lblAbsentToday.Font = New System.Drawing.Font("Segoe UI", 18.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblAbsentToday.ForeColor = System.Drawing.Color.FromArgb(CType(CType(231, Byte), Integer), CType(CType(76, Byte), Integer), CType(CType(60, Byte), Integer))
        Me.lblAbsentToday.Location = New System.Drawing.Point(930, 30)
        Me.lblAbsentToday.Name = "lblAbsentToday"
        Me.lblAbsentToday.Size = New System.Drawing.Size(100, 32)
        Me.lblAbsentToday.TabIndex = 9
        Me.lblAbsentToday.Text = "0"
        Me.lblAbsentToday.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        '
        'Label6
        '
        Me.Label6.AutoSize = True
        Me.Label6.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label6.Location = New System.Drawing.Point(950, 62)
        Me.Label6.Name = "Label6"
        Me.Label6.Size = New System.Drawing.Size(53, 19)
        Me.Label6.TabIndex = 8
        Me.Label6.Text = "Absent"
        '
        'lblLateToday
        '
        Me.lblLateToday.Font = New System.Drawing.Font("Segoe UI", 18.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblLateToday.ForeColor = System.Drawing.Color.Orange
        Me.lblLateToday.Location = New System.Drawing.Point(700, 30)
        Me.lblLateToday.Name = "lblLateToday"
        Me.lblLateToday.Size = New System.Drawing.Size(100, 32)
        Me.lblLateToday.TabIndex = 7
        Me.lblLateToday.Text = "0"
        Me.lblLateToday.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        '
        'Label4
        '
        Me.Label4.AutoSize = True
        Me.Label4.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label4.Location = New System.Drawing.Point(730, 62)
        Me.Label4.Name = "Label4"
        Me.Label4.Size = New System.Drawing.Size(35, 19)
        Me.Label4.TabIndex = 6
        Me.Label4.Text = "Late"
        '
        'lblPresentToday
        '
        Me.lblPresentToday.Font = New System.Drawing.Font("Segoe UI", 18.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblPresentToday.ForeColor = System.Drawing.Color.FromArgb(CType(CType(46, Byte), Integer), CType(CType(204, Byte), Integer), CType(CType(113, Byte), Integer))
        Me.lblPresentToday.Location = New System.Drawing.Point(470, 30)
        Me.lblPresentToday.Name = "lblPresentToday"
        Me.lblPresentToday.Size = New System.Drawing.Size(100, 32)
        Me.lblPresentToday.TabIndex = 5
        Me.lblPresentToday.Text = "0"
        Me.lblPresentToday.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        '
        'Label3
        '
        Me.Label3.AutoSize = True
        Me.Label3.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label3.Location = New System.Drawing.Point(493, 62)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(56, 19)
        Me.Label3.TabIndex = 4
        Me.Label3.Text = "Present"
        '
        'lblTotalToday
        '
        Me.lblTotalToday.Font = New System.Drawing.Font("Segoe UI", 18.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblTotalToday.ForeColor = System.Drawing.Color.FromArgb(CType(CType(52, Byte), Integer), CType(CType(152, Byte), Integer), CType(CType(219, Byte), Integer))
        Me.lblTotalToday.Location = New System.Drawing.Point(240, 30)
        Me.lblTotalToday.Name = "lblTotalToday"
        Me.lblTotalToday.Size = New System.Drawing.Size(100, 32)
        Me.lblTotalToday.TabIndex = 3
        Me.lblTotalToday.Text = "0"
        Me.lblTotalToday.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label2.Location = New System.Drawing.Point(237, 62)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(104, 19)
        Me.Label2.TabIndex = 2
        Me.Label2.Text = "Today's Records"
        '
        'lblTotalStudents
        '
        Me.lblTotalStudents.Font = New System.Drawing.Font("Segoe UI", 18.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblTotalStudents.ForeColor = System.Drawing.Color.FromArgb(CType(CType(52, Byte), Integer), CType(CType(73, Byte), Integer), CType(CType(94, Byte), Integer))
        Me.lblTotalStudents.Location = New System.Drawing.Point(30, 30)
        Me.lblTotalStudents.Name = "lblTotalStudents"
        Me.lblTotalStudents.Size = New System.Drawing.Size(100, 32)
        Me.lblTotalStudents.TabIndex = 1
        Me.lblTotalStudents.Text = "0"
        Me.lblTotalStudents.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label1.Location = New System.Drawing.Point(27, 62)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(106, 19)
        Me.Label1.TabIndex = 0
        Me.Label1.Text = "Active Students"
        '
        'GroupBox2
        '
        Me.GroupBox2.Controls.Add(Me.btnSearchByDate)
        Me.GroupBox2.Controls.Add(Me.dtpEndDate)
        Me.GroupBox2.Controls.Add(Me.Label8)
        Me.GroupBox2.Controls.Add(Me.dtpStartDate)
        Me.GroupBox2.Controls.Add(Me.Label7)
        Me.GroupBox2.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.GroupBox2.Location = New System.Drawing.Point(20, 210)
        Me.GroupBox2.Name = "GroupBox2"
        Me.GroupBox2.Size = New System.Drawing.Size(570, 100)
        Me.GroupBox2.TabIndex = 2
        Me.GroupBox2.TabStop = False
        Me.GroupBox2.Text = "Search by Date Range"
        '
        'btnSearchByDate
        '
        Me.btnSearchByDate.BackColor = System.Drawing.Color.FromArgb(CType(CType(155, Byte), Integer), CType(CType(89, Byte), Integer), CType(CType(182, Byte), Integer))
        Me.btnSearchByDate.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnSearchByDate.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnSearchByDate.ForeColor = System.Drawing.Color.White
        Me.btnSearchByDate.Location = New System.Drawing.Point(420, 45)
        Me.btnSearchByDate.Name = "btnSearchByDate"
        Me.btnSearchByDate.Size = New System.Drawing.Size(120, 35)
        Me.btnSearchByDate.TabIndex = 4
        Me.btnSearchByDate.Text = "SEARCH"
        Me.btnSearchByDate.UseVisualStyleBackColor = False
        '
        'dtpEndDate
        '
        Me.dtpEndDate.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.dtpEndDate.Location = New System.Drawing.Point(230, 50)
        Me.dtpEndDate.Name = "dtpEndDate"
        Me.dtpEndDate.Size = New System.Drawing.Size(170, 23)
        Me.dtpEndDate.TabIndex = 3
        '
        'Label8
        '
        Me.Label8.AutoSize = True
        Me.Label8.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label8.Location = New System.Drawing.Point(230, 30)
        Me.Label8.Name = "Label8"
        Me.Label8.Size = New System.Drawing.Size(57, 15)
        Me.Label8.TabIndex = 2
        Me.Label8.Text = "End Date:"
        '
        'dtpStartDate
        '
        Me.dtpStartDate.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.dtpStartDate.Location = New System.Drawing.Point(30, 50)
        Me.dtpStartDate.Name = "dtpStartDate"
        Me.dtpStartDate.Size = New System.Drawing.Size(170, 23)
        Me.dtpStartDate.TabIndex = 1
        '
        'Label7
        '
        Me.Label7.AutoSize = True
        Me.Label7.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label7.Location = New System.Drawing.Point(30, 30)
        Me.Label7.Name = "Label7"
        Me.Label7.Size = New System.Drawing.Size(64, 15)
        Me.Label7.TabIndex = 0
        Me.Label7.Text = "Start Date:"
        '
        'dgvTodayAttendance
        '
        Me.dgvTodayAttendance.AllowUserToAddRows = False
        Me.dgvTodayAttendance.AllowUserToDeleteRows = False
        Me.dgvTodayAttendance.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill
        Me.dgvTodayAttendance.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize
        Me.dgvTodayAttendance.Location = New System.Drawing.Point(20, 350)
        Me.dgvTodayAttendance.Name = "dgvTodayAttendance"
        Me.dgvTodayAttendance.ReadOnly = True
        Me.dgvTodayAttendance.RowTemplate.Height = 25
        Me.dgvTodayAttendance.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect
        Me.dgvTodayAttendance.Size = New System.Drawing.Size(1160, 300)
        Me.dgvTodayAttendance.TabIndex = 3
        '
        'Label5
        '
        Me.Label5.AutoSize = True
        Me.Label5.Font = New System.Drawing.Font("Segoe UI", 11.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.Label5.Location = New System.Drawing.Point(20, 325)
        Me.Label5.Name = "Label5"
        Me.Label5.Size = New System.Drawing.Size(153, 20)
        Me.Label5.TabIndex = 4
        Me.Label5.Text = "Attendance Records:"
        '
        'btnRefresh
        '
        Me.btnRefresh.BackColor = System.Drawing.Color.FromArgb(CType(CType(52, Byte), Integer), CType(CType(73, Byte), Integer), CType(CType(94, Byte), Integer))
        Me.btnRefresh.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnRefresh.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnRefresh.ForeColor = System.Drawing.Color.White
        Me.btnRefresh.Location = New System.Drawing.Point(20, 670)
        Me.btnRefresh.Name = "btnRefresh"
        Me.btnRefresh.Size = New System.Drawing.Size(120, 40)
        Me.btnRefresh.TabIndex = 5
        Me.btnRefresh.Text = "REFRESH"
        Me.btnRefresh.UseVisualStyleBackColor = False
        '
        'btnManageStudents
        '
        Me.btnManageStudents.BackColor = System.Drawing.Color.FromArgb(CType(CType(52, Byte), Integer), CType(CType(152, Byte), Integer), CType(CType(219, Byte), Integer))
        Me.btnManageStudents.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnManageStudents.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnManageStudents.ForeColor = System.Drawing.Color.White
        Me.btnManageStudents.Location = New System.Drawing.Point(160, 670)
        Me.btnManageStudents.Name = "btnManageStudents"
        Me.btnManageStudents.Size = New System.Drawing.Size(180, 40)
        Me.btnManageStudents.TabIndex = 6
        Me.btnManageStudents.Text = "MANAGE STUDENTS"
        Me.btnManageStudents.UseVisualStyleBackColor = False
        '
        'btnExportToday
        '
        Me.btnExportToday.BackColor = System.Drawing.Color.FromArgb(CType(CType(46, Byte), Integer), CType(CType(204, Byte), Integer), CType(CType(113, Byte), Integer))
        Me.btnExportToday.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnExportToday.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnExportToday.ForeColor = System.Drawing.Color.White
        Me.btnExportToday.Location = New System.Drawing.Point(360, 670)
        Me.btnExportToday.Name = "btnExportToday"
        Me.btnExportToday.Size = New System.Drawing.Size(150, 40)
        Me.btnExportToday.TabIndex = 7
        Me.btnExportToday.Text = "EXPORT REPORT"
        Me.btnExportToday.UseVisualStyleBackColor = False
        '
        'btnClose
        '
        Me.btnClose.BackColor = System.Drawing.Color.FromArgb(CType(CType(231, Byte), Integer), CType(CType(76, Byte), Integer), CType(CType(60, Byte), Integer))
        Me.btnClose.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnClose.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnClose.ForeColor = System.Drawing.Color.White
        Me.btnClose.Location = New System.Drawing.Point(1060, 670)
        Me.btnClose.Name = "btnClose"
        Me.btnClose.Size = New System.Drawing.Size(120, 40)
        Me.btnClose.TabIndex = 8
        Me.btnClose.Text = "CLOSE"
        Me.btnClose.UseVisualStyleBackColor = False
        '
        'GroupBox3
        '
        Me.GroupBox3.Controls.Add(Me.btnSearchStudent)
        Me.GroupBox3.Controls.Add(Me.txtSearchStudent)
        Me.GroupBox3.Controls.Add(Me.Label9)
        Me.GroupBox3.Font = New System.Drawing.Font("Segoe UI", 10.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.GroupBox3.Location = New System.Drawing.Point(610, 210)
        Me.GroupBox3.Name = "GroupBox3"
        Me.GroupBox3.Size = New System.Drawing.Size(420, 100)
        Me.GroupBox3.TabIndex = 9
        Me.GroupBox3.TabStop = False
        Me.GroupBox3.Text = "Search Student"
        '
        'btnSearchStudent
        '
        Me.btnSearchStudent.BackColor = System.Drawing.Color.FromArgb(CType(CType(155, Byte), Integer), CType(CType(89, Byte), Integer), CType(CType(182, Byte), Integer))
        Me.btnSearchStudent.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnSearchStudent.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnSearchStudent.ForeColor = System.Drawing.Color.White
        Me.btnSearchStudent.Location = New System.Drawing.Point(280, 45)
        Me.btnSearchStudent.Name = "btnSearchStudent"
        Me.btnSearchStudent.Size = New System.Drawing.Size(120, 35)
        Me.btnSearchStudent.TabIndex = 2
        Me.btnSearchStudent.Text = "SEARCH"
        Me.btnSearchStudent.UseVisualStyleBackColor = False
        '
        'txtSearchStudent
        '
        Me.txtSearchStudent.Font = New System.Drawing.Font("Segoe UI", 11.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.txtSearchStudent.Location = New System.Drawing.Point(20, 48)
        Me.txtSearchStudent.Name = "txtSearchStudent"
        Me.txtSearchStudent.Size = New System.Drawing.Size(240, 27)
        Me.txtSearchStudent.TabIndex = 1
        '
        'Label9
        '
        Me.Label9.AutoSize = True
        Me.Label9.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.Label9.Location = New System.Drawing.Point(20, 30)
        Me.Label9.Name = "Label9"
        Me.Label9.Size = New System.Drawing.Size(136, 15)
        Me.Label9.TabIndex = 0
        Me.Label9.Text = "Student Number / Name"
        '
        'btnViewReports
        '
        Me.btnViewReports.BackColor = System.Drawing.Color.FromArgb(CType(CType(155, Byte), Integer), CType(CType(89, Byte), Integer), CType(CType(182, Byte), Integer))
        Me.btnViewReports.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnViewReports.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnViewReports.ForeColor = System.Drawing.Color.White
        Me.btnViewReports.Location = New System.Drawing.Point(530, 670)
        Me.btnViewReports.Name = "btnViewReports"
        Me.btnViewReports.Size = New System.Drawing.Size(180, 40)
        Me.btnViewReports.TabIndex = 10
        Me.btnViewReports.Text = "VIEW SUMMARY REPORT"
        Me.btnViewReports.UseVisualStyleBackColor = False
        '
        'AdminDashboard
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(7.0!, 15.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.BackColor = System.Drawing.Color.WhiteSmoke
        Me.ClientSize = New System.Drawing.Size(1200, 730)
        Me.Controls.Add(Me.btnViewReports)
        Me.Controls.Add(Me.GroupBox3)
        Me.Controls.Add(Me.btnClose)
        Me.Controls.Add(Me.btnExportToday)
        Me.Controls.Add(Me.btnManageStudents)
        Me.Controls.Add(Me.btnRefresh)
        Me.Controls.Add(Me.Label5)
        Me.Controls.Add(Me.dgvTodayAttendance)
        Me.Controls.Add(Me.GroupBox2)
        Me.Controls.Add(Me.GroupBox1)
        Me.Controls.Add(Me.Panel1)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog
        Me.MaximizeBox = False
        Me.Name = "AdminDashboard"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "Admin Dashboard"
        Me.Panel1.ResumeLayout(False)
        Me.Panel1.PerformLayout()
        Me.GroupBox1.ResumeLayout(False)
        Me.GroupBox1.PerformLayout()
        Me.GroupBox2.ResumeLayout(False)
        Me.GroupBox2.PerformLayout()
        CType(Me.dgvTodayAttendance, System.ComponentModel.ISupportInitialize).EndInit()
        Me.GroupBox3.ResumeLayout(False)
        Me.GroupBox3.PerformLayout()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents Panel1 As Panel
    Friend WithEvents lblTitle As Label
    Friend WithEvents GroupBox1 As GroupBox
    Friend WithEvents Label1 As Label
    Friend WithEvents lblTotalStudents As Label
    Friend WithEvents lblTotalToday As Label
    Friend WithEvents Label2 As Label
    Friend WithEvents lblPresentToday As Label
    Friend WithEvents Label3 As Label
    Friend WithEvents lblLateToday As Label
    Friend WithEvents Label4 As Label
    Friend WithEvents lblAbsentToday As Label
    Friend WithEvents Label6 As Label
    Friend WithEvents GroupBox2 As GroupBox
    Friend WithEvents dtpEndDate As DateTimePicker
    Friend WithEvents Label8 As Label
    Friend WithEvents dtpStartDate As DateTimePicker
    Friend WithEvents Label7 As Label
    Friend WithEvents btnSearchByDate As Button
    Friend WithEvents dgvTodayAttendance As DataGridView
    Friend WithEvents Label5 As Label
    Friend WithEvents btnRefresh As Button
    Friend WithEvents btnManageStudents As Button
    Friend WithEvents btnExportToday As Button
    Friend WithEvents btnClose As Button
    Friend WithEvents GroupBox3 As GroupBox
    Friend WithEvents btnSearchStudent As Button
    Friend WithEvents txtSearchStudent As TextBox
    Friend WithEvents Label9 As Label
    Friend WithEvents btnViewReports As Button
End Class
