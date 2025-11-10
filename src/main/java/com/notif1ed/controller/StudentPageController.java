/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class for Student Page
 */
public class StudentPageController implements Initializable {

    @FXML
    private TableView<StudentEntry> studentTable;
    
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
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                String sql = "SELECT student_number, first_name, " +
                           "COALESCE(last_name, guardian_name, '') as last_name, " +
                           "email FROM students ORDER BY student_number";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    StudentEntry student = new StudentEntry(
                        rs.getString("student_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                    );
                    studentList.add(student);
                }
                
                if (studentTable != null) {
                    studentTable.setItems(studentList);
                }
                
                System.out.println("✅ Loaded " + studentList.size() + " students from database");
            }
        } catch (SQLException e) {
            Stage stage = (Stage) studentTable.getScene().getWindow();
            ToastNotification.showError(stage, "Error loading students: " + e.getMessage());
            e.printStackTrace();
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
            
            // Save to database
            try (Connection conn = DatabaseConnectionn.connect()) {
                if (conn != null) {
                    String sql = "INSERT INTO students (student_number, first_name, last_name, email, section, guardian_name, guardian_email, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, studentNumber);
                    stmt.setString(2, firstName);
                    stmt.setString(3, lastName);
                    stmt.setString(4, studentEmail);
                    stmt.setString(5, section);
                    stmt.setString(6, guardianName);
                    stmt.setString(7, guardianEmail);
                    
                    int rowsInserted = stmt.executeUpdate();
                    
                    if (rowsInserted > 0) {
                        ToastNotification.show(stage, ToastNotification.ToastType.SUCCESS, 
                            "Student added successfully: " + firstName + " " + lastName);
                        refreshTable();
                    } else {
                        ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                            "Failed to add student");
                    }
                } else {
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                        "Could not connect to database");
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                        "This student number already exists!");
                } else {
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                        "Database error: " + e.getMessage());
                }
                e.printStackTrace();
            }
        }
    }
    
    private String generateStudentNumber() {
        String newStudentNumber = "25-0001"; // Default
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                // Get the highest student number starting with "25-"
                String sql = "SELECT student_number FROM students " +
                           "WHERE student_number LIKE '25-%' " +
                           "ORDER BY student_number DESC LIMIT 1";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    String lastNumber = rs.getString("student_number");
                    // Extract the numeric part after "25-"
                    String numericPart = lastNumber.substring(3);
                    int nextNumber = Integer.parseInt(numericPart) + 1;
                    // Format with leading zeros (4 digits)
                    newStudentNumber = String.format("25-%04d", nextNumber);
                } else {
                    // No existing students, start with 25-0001
                    newStudentNumber = "25-0001";
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating student number: " + e.getMessage());
            e.printStackTrace();
            // Keep default value
        }
        
        return newStudentNumber;
    }
    
    @FXML
    private void handleSendEmailClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Get selected student or all students
        if (studentTable.getSelectionModel().getSelectedItem() != null) {
            StudentEntry selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            ToastNotification.showInfo(stage, 
                "Opening email prompt for: " + selectedStudent.getEmail());
            openEmailPrompt(selectedStudent.getEmail());
        } else if (studentList.size() > 0) {
            ToastNotification.showInfo(stage, 
                "Opening email prompt for all students (" + studentList.size() + " recipients)");
            openEmailPromptForAll();
        } else {
            ToastNotification.showWarning(stage, 
                "No students available to send email to.");
        }
    }
    
    private void openEmailPrompt(String email) {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/EmailPrompt.fxml"));
            Stage emailStage = new Stage();
            emailStage.setTitle("Send Email - " + email);
            emailStage.setScene(new Scene(root));
            emailStage.show();
        } catch (IOException e) {
            ToastNotification.showError(stage, "Could not open email prompt");
            e.printStackTrace();
        }
    }
    
    private void openEmailPromptForAll() {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/EmailPrompt.fxml"));
            Stage emailStage = new Stage();
            emailStage.setTitle("Send Email to All Students");
            emailStage.setScene(new Scene(root));
            emailStage.show();
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/StudentEditForm.fxml"));
            Parent root = loader.load();
            
            // Get the controller and pass the student data
            StudentEditFormController controller = loader.getController();
            controller.setStudent(student);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Student - " + student.getStudentNumber());
            stage.setScene(new Scene(root));
            stage.show();
            
            // Refresh table when edit window is closed
            stage.setOnHidden(e -> refreshTable());
        } catch (IOException e) {
            ToastNotification.showError((Stage) studentTable.getScene().getWindow(), "Could not open edit form");
            e.printStackTrace();
        }
    }
    
    private void deleteStudent(StudentEntry student) {
        String sql = "DELETE FROM students WHERE student_number = ?";
        Stage stage = (Stage) studentTable.getScene().getWindow();
        
        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getStudentNumber());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ToastNotification.showSuccess(stage, 
                         "Student " + student.getStudentNumber() + " has been deleted successfully.");
                refreshTable();
            } else {
                ToastNotification.showError(stage, 
                         "Could not delete student. Student may not exist.");
            }
            
        } catch (SQLException e) {
            ToastNotification.showError(stage, 
                     "Error deleting student: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
