/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.service.StudentService;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;
import com.notif1ed.util.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * FXML Controller class for Student Page
 * Uses StudentService for all data operations.
 * 
 * @version 2.0.0
 */
public class StudentPageController implements Initializable {
    
    private static final Logger log = LoggerFactory.getLogger(StudentPageController.class);
    private final StudentService studentService = new StudentService();

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
    
    private ObservableList<StudentEntry> studentList = FXCollections.observableArrayList();

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
            
            if (studentTable != null) {
                studentTable.setItems(studentList);
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
    
    private void openEmailPrompt(String email) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/EmailPrompt.fxml"));
            Parent root = loader.load();
            
            // Get controller and set recipient
            EmailPromptController controller = loader.getController();
            controller.setRecipient(email);
            
            // Create modal dialog with backdrop styling
            Stage emailStage = new Stage();
            emailStage.setTitle("Send Email - " + email);
            emailStage.initOwner(stage);
            emailStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            emailStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            emailStage.setResizable(false);
            
            // Wrap content in backdrop
            javafx.scene.layout.StackPane backdrop = new javafx.scene.layout.StackPane();
            backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
            backdrop.setAlignment(javafx.geometry.Pos.CENTER);
            backdrop.getChildren().add(root);
            
            Scene scene = new Scene(backdrop);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            emailStage.setScene(scene);
            
            // Match owner window size and position
            emailStage.setX(stage.getX());
            emailStage.setY(stage.getY());
            emailStage.setWidth(stage.getWidth());
            emailStage.setHeight(stage.getHeight());
            
            // Close on backdrop click
            backdrop.setOnMouseClicked(e -> {
                if (e.getTarget() == backdrop) {
                    emailStage.close();
                }
            });
            
            emailStage.showAndWait();
        } catch (IOException e) {
            ToastNotification.showError(stage, "Could not open email prompt");
            e.printStackTrace();
        }
    }
    
    private void openEmailPromptForAll() {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/EmailPrompt.fxml"));
            Parent root = loader.load();
            
            // Get controller and set multiple recipients
            EmailPromptController controller = loader.getController();
            StringBuilder emails = new StringBuilder();
            for (int i = 0; i < studentList.size(); i++) {
                emails.append(studentList.get(i).getEmail());
                if (i < studentList.size() - 1) {
                    emails.append(", ");
                }
            }
            controller.setMultipleRecipients(emails.toString());
            
            // Create modal dialog with backdrop styling
            Stage emailStage = new Stage();
            emailStage.setTitle("Send Email to All Students (" + studentList.size() + " recipients)");
            emailStage.initOwner(stage);
            emailStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            emailStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            emailStage.setResizable(false);
            
            // Wrap content in backdrop
            javafx.scene.layout.StackPane backdrop = new javafx.scene.layout.StackPane();
            backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
            backdrop.setAlignment(javafx.geometry.Pos.CENTER);
            backdrop.getChildren().add(root);
            
            Scene scene = new Scene(backdrop);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            emailStage.setScene(scene);
            
            // Match owner window size and position
            emailStage.setX(stage.getX());
            emailStage.setY(stage.getY());
            emailStage.setWidth(stage.getWidth());
            emailStage.setHeight(stage.getHeight());
            
            // Close on backdrop click
            backdrop.setOnMouseClicked(e -> {
                if (e.getTarget() == backdrop) {
                    emailStage.close();
                }
            });
            
            emailStage.showAndWait();
        } catch (IOException e) {
            ToastNotification.showError(stage, "Could not open email prompt");
            e.printStackTrace();
        }
    }
    
    private void openEmailPromptForSelected(java.util.List<StudentEntry> selectedStudents) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/EmailPrompt.fxml"));
            Parent root = loader.load();
            
            // Get controller and set multiple recipients
            EmailPromptController controller = loader.getController();
            
            // Collect selected student emails
            StringBuilder emails = new StringBuilder();
            for (int i = 0; i < selectedStudents.size(); i++) {
                emails.append(selectedStudents.get(i).getEmail());
                if (i < selectedStudents.size() - 1) {
                    emails.append(", ");
                }
            }
            controller.setMultipleRecipients(emails.toString());
            
            // Create modal dialog with backdrop styling
            Stage emailStage = new Stage();
            emailStage.setTitle("Send Email - " + selectedStudents.size() + " Recipients");
            emailStage.initOwner(stage);
            emailStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            emailStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            emailStage.setResizable(false);
            
            // Wrap content in backdrop
            javafx.scene.layout.StackPane backdrop = new javafx.scene.layout.StackPane();
            backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
            backdrop.setAlignment(javafx.geometry.Pos.CENTER);
            backdrop.getChildren().add(root);
            
            Scene scene = new Scene(backdrop);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            emailStage.setScene(scene);
            
            // Match owner window size and position
            emailStage.setX(stage.getX());
            emailStage.setY(stage.getY());
            emailStage.setWidth(stage.getWidth());
            emailStage.setHeight(stage.getHeight());
            
            // Close on backdrop click
            backdrop.setOnMouseClicked(e -> {
                if (e.getTarget() == backdrop) {
                    emailStage.close();
                }
            });
            
            emailStage.showAndWait();
        } catch (IOException e) {
            ToastNotification.showError(stage, "Could not open email prompt");
            e.printStackTrace();
        }
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
}
