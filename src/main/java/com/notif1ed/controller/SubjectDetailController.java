package com.notif1ed.controller;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.model.SubjectEntry;
import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

public class SubjectDetailController implements Initializable {

    @FXML
    private Text subjectCodeText;
    @FXML
    private Text subjectNameText;
    @FXML
    private Label yearLevelLabel;
    @FXML
    private Label enrollmentCountLabel;
    @FXML
    private Button btnClose;

    // Enrolled Students Table
    @FXML
    private TextField enrolledSearchField;
    @FXML
    private TableView<StudentEntry> enrolledStudentsTable;
    @FXML
    private TableColumn<StudentEntry, String> enrolledStudentNumberCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledFirstNameCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledLastNameCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledEmailCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledDateCol;
    @FXML
    private Button btnRemoveStudent;

    // Available Students Table
    @FXML
    private TextField availableSearchField;
    @FXML
    private TableView<StudentEntry> availableStudentsTable;
    @FXML
    private TableColumn<StudentEntry, String> availableStudentNumberCol;
    @FXML
    private TableColumn<StudentEntry, String> availableFirstNameCol;
    @FXML
    private TableColumn<StudentEntry, String> availableLastNameCol;
    @FXML
    private TableColumn<StudentEntry, String> availableEmailCol;
    @FXML
    private Button btnAddStudent;

    private SubjectEntry currentSubject;
    private ObservableList<StudentEntry> enrolledStudentsList = FXCollections.observableArrayList();
    private ObservableList<StudentEntry> availableStudentsList = FXCollections.observableArrayList();
    private FilteredList<StudentEntry> filteredEnrolledStudents;
    private FilteredList<StudentEntry> filteredAvailableStudents;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupEnrolledStudentsTable();
        setupAvailableStudentsTable();
        setupSearchFilters();
    }

    public void setSubject(SubjectEntry subject) {
        this.currentSubject = subject;
        displaySubjectInfo();
        loadEnrolledStudents();
        loadAvailableStudents();
    }

    private void displaySubjectInfo() {
        if (currentSubject != null) {
            subjectCodeText.setText(currentSubject.getSubjectCode());
            subjectNameText.setText(currentSubject.getSubjectName());
            yearLevelLabel.setText("Year Level: " + currentSubject.getYearLevel() + 
                                 " | Section: " + currentSubject.getSection());
        }
    }

    private void setupEnrolledStudentsTable() {
        enrolledStudentNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        enrolledFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        enrolledLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        enrolledEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        enrolledDateCol.setCellValueFactory(cellData -> {
            // This will be populated from the enrollment_date field
            return new javafx.beans.property.SimpleStringProperty("Recently");
        });
    }

    private void setupAvailableStudentsTable() {
        availableStudentNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        availableFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        availableLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        availableEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void setupSearchFilters() {
        // Filter for enrolled students
        filteredEnrolledStudents = new FilteredList<>(enrolledStudentsList, p -> true);
        enrolledSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEnrolledStudents.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getStudentNumber().toLowerCase().contains(lowerCaseFilter)
                        || student.getFirstName().toLowerCase().contains(lowerCaseFilter)
                        || student.getLastName().toLowerCase().contains(lowerCaseFilter)
                        || student.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
        enrolledStudentsTable.setItems(filteredEnrolledStudents);

        // Filter for available students
        filteredAvailableStudents = new FilteredList<>(availableStudentsList, p -> true);
        availableSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAvailableStudents.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getStudentNumber().toLowerCase().contains(lowerCaseFilter)
                        || student.getFirstName().toLowerCase().contains(lowerCaseFilter)
                        || student.getLastName().toLowerCase().contains(lowerCaseFilter)
                        || student.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
        availableStudentsTable.setItems(filteredAvailableStudents);
    }

    private void loadEnrolledStudents() {
        enrolledStudentsList.clear();

        if (currentSubject == null) return;

        String sql = "SELECT s.student_number, s.first_name, " +
                    "COALESCE(s.last_name, s.guardian_name, '') as last_name, " +
                    "s.email, ss.enrollment_date " +
                    "FROM students s " +
                    "INNER JOIN student_subjects ss ON s.student_id = ss.student_id " +
                    "WHERE ss.subject_id = ? AND ss.status = 'active' " +
                    "ORDER BY s.student_number";

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StudentEntry student = new StudentEntry(
                    rs.getString("student_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
                );
                enrolledStudentsList.add(student);
            }

            updateEnrollmentCount();
            System.out.println("✅ Loaded " + enrolledStudentsList.size() + " enrolled students");

        } catch (SQLException e) {
            Stage stage = (Stage) enrolledStudentsTable.getScene().getWindow();
            ToastNotification.showError(stage, "Error loading enrolled students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAvailableStudents() {
        availableStudentsList.clear();

        if (currentSubject == null) return;

        String sql = "SELECT s.student_number, s.first_name, " +
                    "COALESCE(s.last_name, s.guardian_name, '') as last_name, " +
                    "s.email " +
                    "FROM students s " +
                    "WHERE s.student_id NOT IN ( " +
                    "    SELECT student_id FROM student_subjects " +
                    "    WHERE subject_id = ? AND status = 'active' " +
                    ") " +
                    "ORDER BY s.student_number";

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StudentEntry student = new StudentEntry(
                    rs.getString("student_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
                );
                availableStudentsList.add(student);
            }

            System.out.println("✅ Loaded " + availableStudentsList.size() + " available students");

        } catch (SQLException e) {
            Stage stage = (Stage) availableStudentsTable.getScene().getWindow();
            ToastNotification.showError(stage, "Error loading available students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        StudentEntry selectedStudent = availableStudentsTable.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) availableStudentsTable.getScene().getWindow();

        if (selectedStudent == null) {
            ToastNotification.showWarning(stage, "Please select a student to add to this class");
            return;
        }

        // Add student to subject in database
        String sql = "INSERT INTO student_subjects (student_id, subject_id, enrollment_date, status) " +
                    "SELECT s.student_id, ?, CURRENT_DATE, 'active' " +
                    "FROM students s WHERE s.student_number = ?";

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            stmt.setString(2, selectedStudent.getStudentNumber());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ToastNotification.showSuccess(stage, 
                    selectedStudent.getFirstName() + " " + selectedStudent.getLastName() + 
                    " added to " + currentSubject.getSubjectCode());
                
                // Refresh both tables
                loadEnrolledStudents();
                loadAvailableStudents();
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                ToastNotification.showError(stage, "This student is already enrolled in this subject");
            } else {
                ToastNotification.showError(stage, "Error adding student: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveStudent(ActionEvent event) {
        StudentEntry selectedStudent = enrolledStudentsTable.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) enrolledStudentsTable.getScene().getWindow();

        if (selectedStudent == null) {
            ToastNotification.showWarning(stage, "Please select a student to remove from this class");
            return;
        }

        // Confirm removal using CustomModal (blocking confirmation is appropriate here)
        boolean confirmed = CustomModal.showConfirmation(
            stage,
            "Remove Student from Class",
            "Are you sure you want to remove " + selectedStudent.getFirstName() + " " + 
            selectedStudent.getLastName() + " from " + currentSubject.getSubjectCode() + "?",
            "Remove",
            "Cancel"
        );
        
        if (confirmed) {
            removeStudentFromSubject(selectedStudent);
        }
    }

    private void removeStudentFromSubject(StudentEntry student) {
        String sql = "UPDATE student_subjects " +
                    "SET status = 'dropped' " +
                    "WHERE subject_id = ? AND student_id = ( " +
                    "    SELECT student_id FROM students WHERE student_number = ? " +
                    ")";

        Stage stage = (Stage) enrolledStudentsTable.getScene().getWindow();

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            stmt.setString(2, student.getStudentNumber());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ToastNotification.showSuccess(stage,
                    student.getFirstName() + " " + student.getLastName() + 
                    " removed from " + currentSubject.getSubjectCode());
                
                // Refresh both tables
                loadEnrolledStudents();
                loadAvailableStudents();
            }

        } catch (SQLException e) {
            ToastNotification.showError(stage, "Error removing student: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Show attendance report for the enrolled students in this subject
     * Displays arrival and departure statistics for all dates
     */
    @FXML
    private void handleCheckAttendance(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        if (currentSubject == null) {
            ToastNotification.showError(stage, "No subject selected");
            return;
        }
        
        // Get total enrolled students count
        int totalEnrolled = enrolledStudentsList.size();
        
        // Query attendance statistics grouped by date
        String sql = "SELECT " +
                    "DATE(r.created_at) as record_date, " +
                    "COUNT(DISTINCT CASE WHEN r.record_type = 'Arrival' THEN r.student_id END) as arrivals, " +
                    "COUNT(DISTINCT CASE WHEN r.record_type = 'Departure' THEN r.student_id END) as departures " +
                    "FROM records r " +
                    "INNER JOIN student_subjects ss ON r.student_id = ss.student_id " +
                    "WHERE ss.subject_id = ? AND ss.status = 'active' " +
                    "GROUP BY DATE(r.created_at) " +
                    "ORDER BY DATE(r.created_at) DESC " +
                    "LIMIT 10";
        
        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, currentSubject.getId());
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder message = new StringBuilder();
            message.append(String.format("📊 Attendance History for %s\n", currentSubject.getSubjectCode()));
            message.append(String.format("👥 Total Enrolled: %d students\n\n", totalEnrolled));
            message.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            
            boolean hasRecords = false;
            int recordCount = 0;
            
            while (rs.next() && recordCount < 10) {
                hasRecords = true;
                String recordDate = rs.getString("record_date");
                int arrivals = rs.getInt("arrivals");
                int departures = rs.getInt("departures");
                int absent = totalEnrolled - arrivals;
                double attendanceRate = totalEnrolled > 0 ? (arrivals * 100.0 / totalEnrolled) : 0;
                
                message.append(String.format("� Date: %s\n", recordDate));
                message.append(String.format("   ✅ Arrivals: %d students\n", arrivals));
                message.append(String.format("   ❌ Absent: %d students\n", absent));
                message.append(String.format("   🚪 Departures: %d students\n", departures));
                message.append(String.format("   � Rate: %.1f%%\n\n", attendanceRate));
                
                recordCount++;
            }
            
            if (!hasRecords) {
                message.append("No attendance records found for this subject.\n");
                message.append("Records will appear here once students check in.");
            } else {
                message.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                message.append(String.format("Showing last %d attendance dates", recordCount));
            }
            
            CustomModal.showInfo(stage, "Attendance History", message.toString());
            
        } catch (SQLException e) {
            ToastNotification.showError(stage, "Error loading attendance data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateEnrollmentCount() {
        enrollmentCountLabel.setText(enrolledStudentsList.size() + " Student" + 
                                    (enrolledStudentsList.size() != 1 ? "s" : "") + " Enrolled");
    }
    
    /**
     * Generate and download PDF report of attendance history
     */
    @FXML
    private void handleDownloadPDFReport(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        if (currentSubject == null) {
            ToastNotification.showError(stage, "No subject selected");
            return;
        }
        
        // Show file chooser for save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Attendance Report");
        fileChooser.setInitialFileName(String.format("Attendance_Report_%s_%s.pdf", 
            currentSubject.getSubjectCode(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return; // User cancelled
        }
        
        try {
            generatePDFReport(file);
            ToastNotification.showSuccess(stage, "PDF report saved successfully!");
        } catch (Exception e) {
            ToastNotification.showError(stage, "Error generating PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generate the PDF report with attendance data
     */
    private void generatePDFReport(File file) throws Exception {
        int totalEnrolled = enrolledStudentsList.size();
        
        // Query attendance statistics grouped by date
        String sql = "SELECT " +
                    "DATE(r.created_at) as record_date, " +
                    "COUNT(DISTINCT CASE WHEN r.record_type = 'Arrival' THEN r.student_id END) as arrivals, " +
                    "COUNT(DISTINCT CASE WHEN r.record_type = 'Departure' THEN r.student_id END) as departures " +
                    "FROM records r " +
                    "INNER JOIN student_subjects ss ON r.student_id = ss.student_id " +
                    "WHERE ss.subject_id = ? AND ss.status = 'active' " +
                    "GROUP BY DATE(r.created_at) " +
                    "ORDER BY DATE(r.created_at) DESC";
        
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        
        // Create fonts
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        // Add header
        Paragraph title = new Paragraph("ATTENDANCE REPORT")
            .setFont(boldFont)
            .setFontSize(20)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(10);
        document.add(title);
        
        Paragraph subTitle = new Paragraph("Quezon City University")
            .setFont(normalFont)
            .setFontSize(12)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20);
        document.add(subTitle);
        
        // Add subject information
        document.add(new Paragraph(String.format("Subject: %s - %s", 
            currentSubject.getSubjectCode(), currentSubject.getSubjectName()))
            .setFont(boldFont)
            .setFontSize(14)
            .setMarginBottom(5));
            
        document.add(new Paragraph(String.format("Year Level: %d | Section: %s", 
            currentSubject.getYearLevel(), currentSubject.getSection()))
            .setFont(normalFont)
            .setFontSize(11)
            .setMarginBottom(5));
            
        document.add(new Paragraph(String.format("Total Enrolled Students: %d", totalEnrolled))
            .setFont(normalFont)
            .setFontSize(11)
            .setMarginBottom(5));
            
        document.add(new Paragraph(String.format("Report Generated: %s", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a"))))
            .setFont(normalFont)
            .setFontSize(10)
            .setMarginBottom(20));
        
        // Create table
        float[] columnWidths = {3, 2, 2, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        
        // Add table headers
        String[] headers = {"Date", "Arrivals", "Departures", "Absent", "Attendance Rate"};
        for (String header : headers) {
            Cell cell = new Cell()
                .add(new Paragraph(header).setFont(boldFont).setFontSize(11))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
            table.addHeaderCell(cell);
        }
        
        // Query and add data rows
        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, currentSubject.getId());
            ResultSet rs = stmt.executeQuery();
            
            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                String recordDate = rs.getString("record_date");
                int arrivals = rs.getInt("arrivals");
                int departures = rs.getInt("departures");
                int absent = totalEnrolled - arrivals;
                double attendanceRate = totalEnrolled > 0 ? (arrivals * 100.0 / totalEnrolled) : 0;
                
                table.addCell(new Cell().add(new Paragraph(recordDate).setFont(normalFont).setFontSize(10)).setTextAlignment(TextAlignment.CENTER).setPadding(5));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(arrivals)).setFont(normalFont).setFontSize(10)).setTextAlignment(TextAlignment.CENTER).setPadding(5));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(departures)).setFont(normalFont).setFontSize(10)).setTextAlignment(TextAlignment.CENTER).setPadding(5));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(absent)).setFont(normalFont).setFontSize(10)).setTextAlignment(TextAlignment.CENTER).setPadding(5));
                table.addCell(new Cell().add(new Paragraph(String.format("%.1f%%", attendanceRate)).setFont(normalFont).setFontSize(10)).setTextAlignment(TextAlignment.CENTER).setPadding(5));
            }
            
            if (!hasRecords) {
                // Add a message if no records found
                Cell noDataCell = new Cell(1, 5)
                    .add(new Paragraph("No attendance records found for this subject.").setFont(normalFont).setFontSize(10).setItalic())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(20);
                table.addCell(noDataCell);
            }
        }
        
        document.add(table);
        
        // Add footer
        document.add(new Paragraph("\n\nThis report was automatically generated by Notif1ed System.")
            .setFont(normalFont)
            .setFontSize(9)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(20)
            .setFontColor(ColorConstants.GRAY));
        
        document.close();
    }
}
