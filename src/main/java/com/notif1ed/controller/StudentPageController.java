/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.service.StudentService;
import com.notif1ed.service.RecordService;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;
import com.notif1ed.util.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * FXML Controller class for Student Page
 * Uses StudentService for all data operations.
 * 
 * @version 2.0.0
 */
public class StudentPageController implements Initializable {
    
    private static final Logger log = LoggerFactory.getLogger(StudentPageController.class);
    private final StudentService studentService = new StudentService();
    private final RecordService recordService = new RecordService();

    @FXML
    private TableView<StudentEntry> studentTable;
    
    @FXML
    private TableColumn<StudentEntry, Boolean> selectCol;
    
    @FXML
    private TableColumn<StudentEntry, String> studentNumberCol;
    
    @FXML
    private TableColumn<StudentEntry, String> firstNameCol;
    
    @FXML
    private TableColumn<StudentEntry, String> lastNameCol;
    
    @FXML
    private TableColumn<StudentEntry, String> emailCol;
    
    @FXML
    private TableColumn<StudentEntry, Void> actionsCol;
    
    @FXML
    private Button homeButton;
    @FXML
    private Button subjectsButton;
    @FXML
    private Button studentsButton;
    @FXML
    private Button recordsButton;
    @FXML
    private Button addStudentButton;
    @FXML
    private Button btnDownloadReport;
    @FXML
    private javafx.scene.control.TextField searchField;
    
    private ObservableList<StudentEntry> studentList = FXCollections.observableArrayList();
    private javafx.collections.transformation.FilteredList<StudentEntry> filteredStudents;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Session validation - redirect to login if not logged in
        if (!SessionManager.getInstance().isLoggedIn()) {
            log.warn("Unauthorized access attempt to Students page");
            // TODO: Redirect to login page
            return;
        }
        
        log.info("Initializing Students page for user: {}", SessionManager.getInstance().getUserName());
        
        // Setup checkbox column
        if (selectCol != null) {
            selectCol.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
            selectCol.setCellFactory(col -> new TableCell<StudentEntry, Boolean>() {
                private final javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox();
                
                {
                    checkBox.setOnAction(event -> {
                        StudentEntry student = getTableView().getItems().get(getIndex());
                        student.setSelected(checkBox.isSelected());
                    });
                }
                
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        StudentEntry student = getTableView().getItems().get(getIndex());
                        checkBox.setSelected(student.isSelected());
                        setGraphic(checkBox);
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            });
        }
        
        // Setup table columns if they exist
        if (studentNumberCol != null) {
            studentNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        }
        if (firstNameCol != null) {
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        }
        if (lastNameCol != null) {
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        }
        if (emailCol != null) {
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        }
        
        // Setup actions column with Edit and Delete buttons
        if (actionsCol != null) {
            actionsCol.setCellFactory(param -> new TableCell<StudentEntry, Void>() {
                private final Button editBtn = new Button("Edit");
                private final Button deleteBtn = new Button("Delete");
                private final HBox buttons = new HBox(10, editBtn, deleteBtn);
                
                {
                    // Style Edit button
                    editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                   "-fx-font-family: 'Poppins SemiBold'; -fx-font-size: 12px; " +
                                   "-fx-padding: 8 20; -fx-background-radius: 5; -fx-cursor: hand;");
                    
                    // Style Delete button
                    deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; " +
                                     "-fx-font-family: 'Poppins SemiBold'; -fx-font-size: 12px; " +
                                     "-fx-padding: 8 20; -fx-background-radius: 5; -fx-cursor: hand;");
                    
                    // Center buttons
                    buttons.setAlignment(Pos.CENTER);
                    
                    // Edit button action
                    editBtn.setOnAction(event -> {
                        StudentEntry student = getTableView().getItems().get(getIndex());
                        openEditStudentForm(student);
                    });
                    
                    // Delete button action
                    deleteBtn.setOnAction(event -> {
                        StudentEntry student = getTableView().getItems().get(getIndex());
                        confirmAndDeleteStudent(student);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(buttons);
                    }
                }
            });
        }
        
        // Load students from database
        loadStudents();
    }
    
    private void loadStudents() {
        studentList.clear();
        
        try {
            // Use StudentService instead of direct database access
            log.debug("Loading students from database");
            studentList.addAll(studentService.getAllStudents());
            
            // Setup filtered list for search functionality
            filteredStudents = new FilteredList<>(studentList, p -> true);
            
            // Setup search filter if searchField exists
            if (searchField != null) {
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredStudents.setPredicate(student -> {
                        // If filter text is empty, display all students
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        
                        String lowerCaseFilter = newValue.toLowerCase();
                        
                        // Search in student number, first name, last name, and email
                        if (student.getStudentNumber().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (student.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (student.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (student.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        
                        return false; // Does not match
                    });
                });
            }
            
            if (studentTable != null) {
                studentTable.setItems(filteredStudents);
            }
            
            log.info("✅ Loaded {} students from database", studentList.size());
            
        } catch (Exception e) {
            log.error("Error loading students", e);
            Stage stage = (Stage) studentTable.getScene().getWindow();
            ToastNotification.showError(stage, "Error loading students: " + e.getMessage());
        }
    }
    
    public void refreshTable() {
        loadStudents();
    }
    
    @FXML
    private void handleHomeClick(ActionEvent event) {
        navigateToPage(event, "Homepage.fxml");
    }
    
    @FXML
    private void handleSubjectsClick(ActionEvent event) {
        navigateToPage(event, "SubjectPage.fxml");
    }
    
    @FXML
    private void handleStudentsClick(ActionEvent event) {
        // Already on students page, just refresh
        refreshTable();
    }
    
    @FXML
    private void handleRecordsClick(ActionEvent event) {
        navigateToPage(event, "RecordsPage.fxml");
    }
    
    @FXML
    private void handleAddStudentClick(ActionEvent event) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        // Generate student number first
        String newStudentNumber = generateStudentNumber();
        
        // Create form fields
        CustomModal.FormField[] fields = {
            new CustomModal.FormField("studentNumber", "Student Number", "text", newStudentNumber, false, ""),
            new CustomModal.FormField("firstName", "First Name", "text", true),
            new CustomModal.FormField("lastName", "Last Name", "text", true),
            new CustomModal.FormField("section", "Section", "text", "", false, "Optional"),
            new CustomModal.FormField("studentEmail", "Student Email", "email", true),
            new CustomModal.FormField("guardianName", "Guardian Name", "text", true),
            new CustomModal.FormField("guardianEmail", "Guardian Email", "email", true)
        };
        
        // Show form modal
        Map<String, String> result = CustomModal.showForm(stage, "Add New Student", "👤", fields);
        
        if (result != null) {
            // Validate and add student
            String studentNumber = result.get("studentNumber").trim();
            String firstName = result.get("firstName").trim();
            String lastName = result.get("lastName").trim();
            String section = result.get("section").trim();
            String studentEmail = result.get("studentEmail").trim();
            String guardianName = result.get("guardianName").trim();
            String guardianEmail = result.get("guardianEmail").trim();
            
            // Validate email formats
            if (!studentEmail.contains("@") || !studentEmail.contains(".")) {
                ToastNotification.show(stage, ToastNotification.ToastType.WARNING, 
                    "Please enter a valid student email address");
                return;
            }
            
            if (!guardianEmail.contains("@") || !guardianEmail.contains(".")) {
                ToastNotification.show(stage, ToastNotification.ToastType.WARNING, 
                    "Please enter a valid guardian email address");
                return;
            }
            
            // Save to database using StudentService
            try {
                StudentEntry newStudent = new StudentEntry(studentNumber, firstName, lastName, studentEmail);
                newStudent.setSection(section);
                
                boolean success = studentService.addStudent(newStudent, guardianName, guardianEmail, section);
                
                if (success) {
                    log.info("Student added successfully: {}", studentNumber);
                    ToastNotification.show(stage, ToastNotification.ToastType.SUCCESS, 
                        "Student added successfully: " + firstName + " " + lastName);
                    refreshTable();
                } else {
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                        "Failed to add student");
                }
                
            } catch (Exception e) {
                log.error("Error adding student", e);
                if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                        "This student number already exists!");
                } else {
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                        "Database error: " + e.getMessage());
                }
            }
        }
    }
    
    // Alias method for FXML compatibility
    @FXML
    public void handleAddStudent(ActionEvent event) {
        handleAddStudentClick(event);
    }
    
    private String generateStudentNumber() {
        String yearPrefix = "25"; // Current year prefix
        
        try {
            // Use StudentService to generate next student number
            String newNumber = studentService.generateNextStudentNumber(yearPrefix);
            log.debug("Generated new student number: {}", newNumber);
            return newNumber;
            
        } catch (Exception e) {
            log.error("Error generating student number", e);
            // Return default as fallback
            return yearPrefix + "-0001";
        }
    }
    
    @FXML
    private void handleSendEmailClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Get students with checkboxes checked
        java.util.List<StudentEntry> selectedStudents = studentList.stream()
            .filter(StudentEntry::isSelected)
            .collect(java.util.stream.Collectors.toList());
        
        if (selectedStudents.isEmpty()) {
            // No checkboxes selected, use all students
            if (studentList.size() > 0) {
                ToastNotification.showInfo(stage, 
                    "Opening email prompt for all students (" + studentList.size() + " recipients)");
                openEmailPromptForAll();
            } else {
                ToastNotification.showWarning(stage, 
                    "No students available to send email to.");
            }
        } else if (selectedStudents.size() == 1) {
            // Single student selected
            StudentEntry student = selectedStudents.get(0);
            ToastNotification.showInfo(stage, 
                "Opening email prompt for: " + student.getEmail());
            openEmailPrompt(student.getEmail());
        } else {
            // Multiple students selected
            ToastNotification.showInfo(stage, 
                "Opening email prompt for selected students (" + selectedStudents.size() + " recipients)");
            openEmailPromptForSelected(selectedStudents);
        }
    }
    
    @FXML
    private void handleDownloadReportClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        if (studentList.isEmpty()) {
            ToastNotification.showWarning(stage, "No students available to generate report.");
            return;
        }
        
        // Generate filename with current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = LocalDateTime.now().format(formatter);
        String defaultFileName = "Student_Report_" + dateStr + ".pdf";
        
        // Show file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Student Report");
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            try {
                generatePDFReport(file);
                ToastNotification.showSuccess(stage, "PDF report generated successfully!");
                
                // Record the report generation
                recordService.addRecord(0, null, "REPORT_GENERATED", 
                    "Daily student report generated: " + file.getName());
                
                log.info("PDF report generated: {}", file.getAbsolutePath());
            } catch (Exception e) {
                log.error("Error generating PDF report", e);
                ToastNotification.showError(stage, "Failed to generate PDF report: " + e.getMessage());
            }
        }
    }
    
    private void openEmailPrompt(String email) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        CustomModal.showEmailModal(stage, email, (subject, message) -> {
            // Send email
            try {
                boolean success = sendEmail(email, subject, message);
                if (success) {
                    ToastNotification.show(stage, ToastNotification.ToastType.SUCCESS,
                        "Email sent successfully to " + email);
                    
                    // Find student ID and record the email
                    studentList.stream()
                        .filter(s -> s.getEmail().equals(email))
                        .findFirst()
                        .ifPresent(student -> {
                            try {
                                int studentId = Integer.parseInt(student.getId());
                                recordService.recordEmailSent(studentId, email, subject);
                            } catch (NumberFormatException e) {
                                log.warn("Could not parse student ID: {}", student.getId());
                            }
                        });
                } else {
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR,
                        "Failed to send email. Please check your email configuration.");
                }
            } catch (Exception e) {
                log.error("Error sending email", e);
                ToastNotification.show(stage, ToastNotification.ToastType.ERROR,
                    "Error sending email: " + e.getMessage());
            }
        });
    }
    
    private void openEmailPromptForAll() {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        // Build comma-separated email list
        StringBuilder emails = new StringBuilder();
        for (int i = 0; i < studentList.size(); i++) {
            emails.append(studentList.get(i).getEmail());
            if (i < studentList.size() - 1) {
                emails.append(", ");
            }
        }
        
        CustomModal.showEmailModal(stage, emails.toString(), (subject, message) -> {
            // Send email to all students
            int successCount = 0;
            int failCount = 0;
            
            for (StudentEntry student : studentList) {
                try {
                    boolean success = sendEmail(student.getEmail(), subject, message);
                    if (success) {
                        successCount++;
                        try {
                            int studentId = Integer.parseInt(student.getId());
                            recordService.recordEmailSent(studentId, student.getEmail(), subject);
                        } catch (NumberFormatException e) {
                            log.warn("Could not parse student ID: {}", student.getId());
                        }
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    log.error("Error sending email to " + student.getEmail(), e);
                    failCount++;
                }
            }
            
            if (failCount == 0) {
                ToastNotification.show(stage, ToastNotification.ToastType.SUCCESS,
                    "Successfully sent email to all " + successCount + " students");
            } else {
                ToastNotification.show(stage, ToastNotification.ToastType.WARNING,
                    "Sent to " + successCount + " students, " + failCount + " failed");
            }
        });
    }
    
    private void openEmailPromptForSelected(java.util.List<StudentEntry> selectedStudents) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        // Build comma-separated email list
        StringBuilder emails = new StringBuilder();
        for (int i = 0; i < selectedStudents.size(); i++) {
            emails.append(selectedStudents.get(i).getEmail());
            if (i < selectedStudents.size() - 1) {
                emails.append(", ");
            }
        }
        
        CustomModal.showEmailModal(stage, emails.toString(), (subject, message) -> {
            // Send email to selected students
            int successCount = 0;
            int failCount = 0;
            
            for (StudentEntry student : selectedStudents) {
                try {
                    boolean success = sendEmail(student.getEmail(), subject, message);
                    if (success) {
                        successCount++;
                        try {
                            int studentId = Integer.parseInt(student.getId());
                            recordService.recordEmailSent(studentId, student.getEmail(), subject);
                        } catch (NumberFormatException e) {
                            log.warn("Could not parse student ID: {}", student.getId());
                        }
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    log.error("Error sending email to " + student.getEmail(), e);
                    failCount++;
                }
            }
            
            if (failCount == 0) {
                ToastNotification.show(stage, ToastNotification.ToastType.SUCCESS,
                    "Successfully sent email to all " + successCount + " selected students");
            } else {
                ToastNotification.show(stage, ToastNotification.ToastType.WARNING,
                    "Sent to " + successCount + " students, " + failCount + " failed");
            }
        });
    }
    
    private void navigateToPage(ActionEvent event, String fxmlFile) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/" + fxmlFile));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ToastNotification.showError(stage, "Could not load page: " + fxmlFile);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleEditStudent(ActionEvent event) {
        StudentEntry selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        if (selectedStudent == null) {
            ToastNotification.showWarning(stage, "Please select a student to edit.");
            return;
        }
        
        openEditStudentForm(selectedStudent);
    }
    
    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        StudentEntry selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        if (selectedStudent == null) {
            ToastNotification.showWarning(stage, "Please select a student to delete.");
            return;
        }
        
        confirmAndDeleteStudent(selectedStudent);
    }
    
    private void confirmAndDeleteStudent(StudentEntry student) {
        // Get current stage for modal
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        // Confirm deletion with modern modal
        boolean confirmed = CustomModal.showConfirmation(
            stage,
            "Delete Student",
            "Are you sure you want to delete student:\n" +
            student.getStudentNumber() + " - " +
            student.getFirstName() + " " + 
            student.getLastName() + "?\n\n" +
            "This will also remove all their enrollments and records.",
            "Delete",
            "Cancel"
        );
        
        if (confirmed) {
            deleteStudent(student);
        }
    }
    
    private void openEditStudentForm(StudentEntry student) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        // Create form fields with current student data
        CustomModal.FormField[] fields = new CustomModal.FormField[] {
            new CustomModal.FormField("studentNumber", "Student Number (Cannot be changed)", "text", student.getStudentNumber(), true, ""),
            new CustomModal.FormField("firstName", "First Name", "text", student.getFirstName(), true, "Enter first name"),
            new CustomModal.FormField("lastName", "Last Name", "text", student.getLastName(), true, "Enter last name"),
            new CustomModal.FormField("section", "Section", "text", student.getSection(), false, "Enter section (optional)"),
            new CustomModal.FormField("email", "Student Email", "email", student.getEmail(), true, "student@example.com"),
            new CustomModal.FormField("guardianName", "Guardian Name", "text", student.getGuardianName(), true, "Enter guardian name"),
            new CustomModal.FormField("guardianEmail", "Guardian Email", "email", student.getGuardianEmail(), false, "guardian@example.com")
        };
        
        // Show form modal
        Map<String, String> result = CustomModal.showForm(stage, "Edit Student - " + student.getStudentNumber(), "✏️", fields);
        
        if (result != null) {
            // User clicked Submit - update student using StudentService
            try {
                StudentEntry updatedStudent = new StudentEntry(
                    student.getStudentNumber(),
                    result.get("firstName"),
                    result.get("lastName"),
                    result.get("email")
                );
                updatedStudent.setSection(result.get("section"));
                
                boolean success = studentService.updateStudent(
                    updatedStudent,
                    result.get("guardianName"),
                    result.get("guardianEmail"),
                    result.get("section")
                );
                
                if (success) {
                    log.info("Student updated: {}", student.getStudentNumber());
                    ToastNotification.showSuccess(stage, "Student updated successfully!");
                    refreshTable();
                } else {
                    ToastNotification.showError(stage, "Could not update student. Student may not exist.");
                }
                
            } catch (Exception e) {
                log.error("Error updating student: {}", student.getStudentNumber(), e);
                ToastNotification.showError(stage, "Error updating student: " + e.getMessage());
            }
        }
    }
    
    private void deleteStudent(StudentEntry student) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        try {
            // Use StudentService for deletion
            boolean success = studentService.deleteStudent(student.getStudentNumber());
            
            if (success) {
                log.info("Student deleted: {}", student.getStudentNumber());
                ToastNotification.showSuccess(stage, 
                         "Student " + student.getStudentNumber() + " has been deleted successfully.");
                refreshTable();
            } else {
                ToastNotification.showError(stage, 
                         "Could not delete student. Student may not exist.");
            }
            
        } catch (Exception e) {
            log.error("Error deleting student: {}", student.getStudentNumber(), e);
            ToastNotification.showError(stage, 
                     "Error deleting student: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogoutClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Show confirmation modal
        boolean confirmed = CustomModal.showConfirmation(
            stage,
            "Logout",
            "Are you sure you want to logout?",
            "Logout",
            "Cancel"
        );
        
        if (confirmed) {
            try {
                // Navigate to landing page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/LandingPage.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("Notif1ed - Welcome");
                stage.show();
                
                // Show logout toast
                ToastNotification.showSuccess(stage, "Logged out successfully");
            } catch (IOException e) {
                ToastNotification.showError(stage, "Error during logout");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Send email using JavaMail API with Gmail SMTP
     * Reused from EmailPromptController - centralized email sending logic
     */
    private boolean sendEmail(String to, String subject, String body) {
        // SMTP Configuration
        final String SMTP_HOST = "smtp.gmail.com";
        final String SMTP_PORT = "587";
        final String FROM_EMAIL = "venturinachen@gmail.com";
        final String APP_PASSWORD = "surn emra mqyi fmfx";
        
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        // Create authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };
        
        try {
            Session session = Session.getInstance(props, auth);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            
            // Handle multiple recipients (comma-separated)
            if (to.contains(",")) {
                String[] recipients = to.split(",");
                InternetAddress[] addresses = new InternetAddress[recipients.length];
                for (int i = 0; i < recipients.length; i++) {
                    addresses[i] = new InternetAddress(recipients[i].trim());
                }
                message.setRecipients(Message.RecipientType.TO, addresses);
            } else {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            
            message.setSubject(subject);
            
            String htmlBody = "<html><body style='font-family: Poppins, Arial, sans-serif;'>" +
                            "<p>" + body.replace("\n", "<br>") + "</p>" +
                            "<br><br>" +
                            "<p style='color: #757575; font-size: 12px;'>Sent from Notif1ed Student Management System</p>" +
                            "</body></html>";
            message.setContent(htmlBody, "text/html; charset=utf-8");
            
            Transport.send(message);
            
            log.info("✅ Email sent successfully to: {}", to);
            return true;
            
        } catch (MessagingException e) {
            log.error("❌ Email sending failed to: {}", to, e);
            return false;
        }
    }
    
    /**
     * Generate PDF report of all students
     * Professional daily report with iText PDF library
     */
    private void generatePDFReport(File file) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Initialize PDF writer and document
            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(fos);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);
            
            // Set up fonts and colors
            com.itextpdf.kernel.colors.Color primaryColor = com.itextpdf.kernel.colors.ColorConstants.BLUE;
            com.itextpdf.kernel.colors.Color headerColor = new com.itextpdf.kernel.colors.DeviceRgb(25, 118, 210);
            
            // Add title
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now = LocalDateTime.now();
            
            com.itextpdf.layout.element.Paragraph title = new com.itextpdf.layout.element.Paragraph("STUDENT REPORT")
                .setFontSize(24)
                .setBold()
                .setFontColor(headerColor)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(5);
            document.add(title);
            
            // Add date and time
            com.itextpdf.layout.element.Paragraph dateTime = new com.itextpdf.layout.element.Paragraph(
                "Generated: " + now.format(dateFormatter) + " at " + now.format(timeFormatter))
                .setFontSize(10)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(dateTime);
            
            // Add summary section
            com.itextpdf.layout.element.Paragraph summary = new com.itextpdf.layout.element.Paragraph(
                "Total Students: " + studentList.size())
                .setFontSize(12)
                .setBold()
                .setMarginBottom(15);
            document.add(summary);
            
            // Add generated by
            com.itextpdf.layout.element.Paragraph generatedBy = new com.itextpdf.layout.element.Paragraph(
                "Generated by: " + SessionManager.getInstance().getUserName())
                .setFontSize(10)
                .setMarginBottom(20);
            document.add(generatedBy);
            
            // Create table with 4 columns
            float[] columnWidths = {2, 3, 3, 4};
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(columnWidths);
            table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));
            
            // Add table headers
            String[] headers = {"Student Number", "First Name", "Last Name", "Email"};
            for (String header : headers) {
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(header).setBold())
                    .setBackgroundColor(headerColor)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setPadding(8);
                table.addHeaderCell(headerCell);
            }
            
            // Add student data rows
            for (StudentEntry student : studentList) {
                // Student Number
                table.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(student.getStudentNumber()))
                    .setPadding(5));
                
                // First Name
                table.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(student.getFirstName()))
                    .setPadding(5));
                
                // Last Name
                table.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(student.getLastName()))
                    .setPadding(5));
                
                // Email
                table.addCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(student.getEmail()))
                    .setPadding(5));
            }
            
            document.add(table);
            
            // Add footer
            com.itextpdf.layout.element.Paragraph footer = new com.itextpdf.layout.element.Paragraph(
                "\n\nNotif1ed Student Management System")
                .setFontSize(8)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY);
            document.add(footer);
            
            // Close document
            document.close();
            
            log.info("✅ PDF report generated successfully");
        }
    }
}
