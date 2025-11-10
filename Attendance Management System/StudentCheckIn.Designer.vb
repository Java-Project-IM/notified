<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class StudentCheckIn
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
        Me.components = New System.ComponentModel.Container()
        Me.Panel1 = New System.Windows.Forms.Panel()
        Me.lblCurrentDate = New System.Windows.Forms.Label()
        Me.lblCurrentTime = New System.Windows.Forms.Label()
        Me.lblTitle = New System.Windows.Forms.Label()
        Me.lblInstruction = New System.Windows.Forms.Label()
        Me.txtStudentNumber = New System.Windows.Forms.TextBox()
        Me.btnCheckIn = New System.Windows.Forms.Button()
        Me.btnBack = New System.Windows.Forms.Button()
        Me.lblStatus = New System.Windows.Forms.Label()
        Me.lblStudentInfo = New System.Windows.Forms.Label()
        Me.Timer1 = New System.Windows.Forms.Timer(Me.components)
        Me.Panel2 = New System.Windows.Forms.Panel()
        Me.lblNote = New System.Windows.Forms.Label()
        Me.Panel1.SuspendLayout()
        Me.Panel2.SuspendLayout()
        Me.SuspendLayout()
        '
        'Panel1
        '
        Me.Panel1.BackColor = System.Drawing.Color.FromArgb(CType(CType(46, Byte), Integer), CType(CType(204, Byte), Integer), CType(CType(113, Byte), Integer))
        Me.Panel1.Controls.Add(Me.lblCurrentDate)
        Me.Panel1.Controls.Add(Me.lblCurrentTime)
        Me.Panel1.Controls.Add(Me.lblTitle)
        Me.Panel1.Dock = System.Windows.Forms.DockStyle.Top
        Me.Panel1.Location = New System.Drawing.Point(0, 0)
        Me.Panel1.Name = "Panel1"
        Me.Panel1.Size = New System.Drawing.Size(700, 150)
        Me.Panel1.TabIndex = 0
        '
        'lblCurrentDate
        '
        Me.lblCurrentDate.AutoSize = True
        Me.lblCurrentDate.Font = New System.Drawing.Font("Segoe UI", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.lblCurrentDate.ForeColor = System.Drawing.Color.White
        Me.lblCurrentDate.Location = New System.Drawing.Point(200, 110)
        Me.lblCurrentDate.Name = "lblCurrentDate"
        Me.lblCurrentDate.Size = New System.Drawing.Size(150, 21)
        Me.lblCurrentDate.TabIndex = 2
        Me.lblCurrentDate.Text = "January 01, 2024"
        '
        'lblCurrentTime
        '
        Me.lblCurrentTime.AutoSize = True
        Me.lblCurrentTime.Font = New System.Drawing.Font("Segoe UI", 20.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblCurrentTime.ForeColor = System.Drawing.Color.White
        Me.lblCurrentTime.Location = New System.Drawing.Point(240, 70)
        Me.lblCurrentTime.Name = "lblCurrentTime"
        Me.lblCurrentTime.Size = New System.Drawing.Size(161, 37)
        Me.lblCurrentTime.TabIndex = 1
        Me.lblCurrentTime.Text = "00:00:00 AM"
        '
        'lblTitle
        '
        Me.lblTitle.AutoSize = True
        Me.lblTitle.Font = New System.Drawing.Font("Segoe UI", 22.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblTitle.ForeColor = System.Drawing.Color.White
        Me.lblTitle.Location = New System.Drawing.Point(190, 20)
        Me.lblTitle.Name = "lblTitle"
        Me.lblTitle.Size = New System.Drawing.Size(320, 41)
        Me.lblTitle.TabIndex = 0
        Me.lblTitle.Text = "STUDENT CHECK-IN"
        '
        'lblInstruction
        '
        Me.lblInstruction.AutoSize = True
        Me.lblInstruction.Font = New System.Drawing.Font("Segoe UI", 14.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.lblInstruction.Location = New System.Drawing.Point(200, 180)
        Me.lblInstruction.Name = "lblInstruction"
        Me.lblInstruction.Size = New System.Drawing.Size(300, 25)
        Me.lblInstruction.TabIndex = 1
        Me.lblInstruction.Text = "Please enter your Student Number"
        '
        'txtStudentNumber
        '
        Me.txtStudentNumber.Font = New System.Drawing.Font("Segoe UI", 20.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.txtStudentNumber.Location = New System.Drawing.Point(150, 230)
        Me.txtStudentNumber.MaxLength = 50
        Me.txtStudentNumber.Name = "txtStudentNumber"
        Me.txtStudentNumber.Size = New System.Drawing.Size(400, 43)
        Me.txtStudentNumber.TabIndex = 2
        Me.txtStudentNumber.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'btnCheckIn
        '
        Me.btnCheckIn.BackColor = System.Drawing.Color.FromArgb(CType(CType(46, Byte), Integer), CType(CType(204, Byte), Integer), CType(CType(113, Byte), Integer))
        Me.btnCheckIn.FlatAppearance.BorderSize = 0
        Me.btnCheckIn.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnCheckIn.Font = New System.Drawing.Font("Segoe UI", 16.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnCheckIn.ForeColor = System.Drawing.Color.White
        Me.btnCheckIn.Location = New System.Drawing.Point(200, 300)
        Me.btnCheckIn.Name = "btnCheckIn"
        Me.btnCheckIn.Size = New System.Drawing.Size(300, 60)
        Me.btnCheckIn.TabIndex = 3
        Me.btnCheckIn.Text = "? CHECK IN"
        Me.btnCheckIn.UseVisualStyleBackColor = False
        '
        'btnBack
        '
        Me.btnBack.BackColor = System.Drawing.Color.FromArgb(CType(CType(149, Byte), Integer), CType(CType(165, Byte), Integer), CType(CType(166, Byte), Integer))
        Me.btnBack.FlatAppearance.BorderSize = 0
        Me.btnBack.FlatStyle = System.Windows.Forms.FlatStyle.Flat
        Me.btnBack.Font = New System.Drawing.Font("Segoe UI", 11.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.btnBack.ForeColor = System.Drawing.Color.White
        Me.btnBack.Location = New System.Drawing.Point(270, 520)
        Me.btnBack.Name = "btnBack"
        Me.btnBack.Size = New System.Drawing.Size(160, 40)
        Me.btnBack.TabIndex = 4
        Me.btnBack.Text = "? BACK"
        Me.btnBack.UseVisualStyleBackColor = False
        '
        'lblStatus
        '
        Me.lblStatus.Font = New System.Drawing.Font("Segoe UI", 24.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point)
        Me.lblStatus.Location = New System.Drawing.Point(150, 380)
        Me.lblStatus.Name = "lblStatus"
        Me.lblStatus.Size = New System.Drawing.Size(400, 45)
        Me.lblStatus.TabIndex = 5
        Me.lblStatus.Text = "? PRESENT"
        Me.lblStatus.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        Me.lblStatus.Visible = False
        '
        'lblStudentInfo
        '
        Me.lblStudentInfo.Font = New System.Drawing.Font("Segoe UI", 11.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point)
        Me.lblStudentInfo.Location = New System.Drawing.Point(150, 430)
        Me.lblStudentInfo.Name = "lblStudentInfo"
        Me.lblStudentInfo.Size = New System.Drawing.Size(400, 70)
        Me.lblStudentInfo.TabIndex = 6
        Me.lblStudentInfo.Text = "John Doe" & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "Computer Science" & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "Time: 09:00:00 AM"
        Me.lblStudentInfo.TextAlign = System.Drawing.ContentAlignment.MiddleCenter
        Me.lblStudentInfo.Visible = False
        '
        'Timer1
        '
        '
        'Panel2
        '
        Me.Panel2.BackColor = System.Drawing.Color.LightBlue
        Me.Panel2.Controls.Add(Me.lblNote)
        Me.Panel2.Dock = System.Windows.Forms.DockStyle.Bottom
        Me.Panel2.Location = New System.Drawing.Point(0, 570)
        Me.Panel2.Name = "Panel2"
        Me.Panel2.Size = New System.Drawing.Size(700, 50)
        Me.Panel2.TabIndex = 7
        '
        'lblNote
        '
        Me.lblNote.AutoSize = True
        Me.lblNote.Font = New System.Drawing.Font("Segoe UI", 9.0!, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point)
        Me.lblNote.Location = New System.Drawing.Point(180, 15)
        Me.lblNote.Name = "lblNote"
        Me.lblNote.Size = New System.Drawing.Size(340, 15)
        Me.lblNote.TabIndex = 0
        Me.lblNote.Text = "Note: Attendance before 9:00 AM is marked as Present, after as Late"
        '
        'StudentCheckIn
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(7.0!, 15.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.BackColor = System.Drawing.Color.White
        Me.ClientSize = New System.Drawing.Size(700, 620)
        Me.Controls.Add(Me.Panel2)
        Me.Controls.Add(Me.lblStudentInfo)
        Me.Controls.Add(Me.lblStatus)
        Me.Controls.Add(Me.btnBack)
        Me.Controls.Add(Me.btnCheckIn)
        Me.Controls.Add(Me.txtStudentNumber)
        Me.Controls.Add(Me.lblInstruction)
        Me.Controls.Add(Me.Panel1)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog
        Me.MaximizeBox = False
        Me.Name = "StudentCheckIn"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "Student Check-In"
        Me.Panel1.ResumeLayout(False)
        Me.Panel1.PerformLayout()
        Me.Panel2.ResumeLayout(False)
        Me.Panel2.PerformLayout()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents Panel1 As Panel
    Friend WithEvents lblTitle As Label
    Friend WithEvents lblCurrentTime As Label
    Friend WithEvents lblCurrentDate As Label
    Friend WithEvents lblInstruction As Label
    Friend WithEvents txtStudentNumber As TextBox
    Friend WithEvents btnCheckIn As Button
    Friend WithEvents btnBack As Button
    Friend WithEvents lblStatus As Label
    Friend WithEvents lblStudentInfo As Label
    Friend WithEvents Timer1 As Timer
    Friend WithEvents Panel2 As Panel
    Friend WithEvents lblNote As Label
End Class
