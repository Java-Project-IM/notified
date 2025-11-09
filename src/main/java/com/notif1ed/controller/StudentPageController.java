/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.util.DatabaseConnectionn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading students: " + e.getMessage());
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
        openFormWindow("StudentForm.fxml", "Add New Student");
    }
    
    @FXML
    private void handleSendEmailClick(ActionEvent event) {
        // Get selected student or all students
        if (studentTable.getSelectionModel().getSelectedItem() != null) {
            StudentEntry selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            showAlert(Alert.AlertType.INFORMATION, "Send Email", 
                "Opening email prompt for: " + selectedStudent.getEmail());
            openEmailPrompt(selectedStudent.getEmail());
        } else if (studentList.size() > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Send Email", 
                "Opening email prompt for all students (" + studentList.size() + " recipients)");
            openEmailPromptForAll();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Students", 
                "No students available to send email to.");
        }
    }
    
    private void openEmailPrompt(String email) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/EmailPrompt.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Send Email - " + email);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open email prompt");
            e.printStackTrace();
        }
    }
    
    private void openEmailPromptForAll() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/EmailPrompt.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Send Email to All Students");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open email prompt");
            e.printStackTrace();
        }
    }
    
    private void openFormWindow(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/" + fxmlFile));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            
            // Refresh table when form window is closed
            stage.setOnHidden(e -> refreshTable());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open form: " + fxmlFile);
            e.printStackTrace();
        }
    }
    
    private void navigateToPage(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/" + fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load page: " + fxmlFile);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleEditStudent(ActionEvent event) {
        StudentEntry selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                     "Please select a student to edit.");
            return;
        }
        
        openEditStudentForm(selectedStudent);
    }
    
    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        StudentEntry selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                     "Please select a student to delete.");
            return;
        }
        
        confirmAndDeleteStudent(selectedStudent);
    }
    
    private void confirmAndDeleteStudent(StudentEntry student) {
        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Student");
        confirmAlert.setContentText("Are you sure you want to delete student:\n" +
                                   student.getStudentNumber() + " - " +
                                   student.getFirstName() + " " + 
                                   student.getLastName() + "?\n\n" +
                                   "This will also remove all their enrollments and records.");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                deleteStudent(student);
            }
        });
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
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open edit form");
            e.printStackTrace();
        }
    }
    
    private void deleteStudent(StudentEntry student) {
        String sql = "DELETE FROM students WHERE student_number = ?";
        
        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getStudentNumber());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                         "Student " + student.getStudentNumber() + " has been deleted successfully.");
                refreshTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", 
                         "Could not delete student. Student may not exist.");
            }
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                     "Error deleting student: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
