package com.notif1ed.controller;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentEditFormController implements Initializable {

    @FXML
    private TextField studentID;
    
    @FXML
    private TextField FnameField;
    
    @FXML
    private TextField LnameField;
    
    @FXML
    private TextField sectionTXT;
    
    @FXML
    private TextField studentEmailField;
    
    @FXML
    private TextField GNameField;
    
    @FXML
    private TextField GEmailField;
    
    @FXML
    private Button updateBTN;
    
    @FXML
    private Button CancelButton;
    
    private StudentEntry currentStudent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Student ID is not editable
        studentID.setEditable(false);
        studentID.setStyle("-fx-opacity: 1.0; -fx-background-color: #F5F5F5;");
    }
    
    public void setStudent(StudentEntry student) {
        this.currentStudent = student;
        loadStudentData();
    }
    
    private void loadStudentData() {
        if (currentStudent != null) {
            studentID.setText(currentStudent.getStudentNumber());
            FnameField.setText(currentStudent.getFirstName());
            LnameField.setText(currentStudent.getLastName());
            studentEmailField.setText(currentStudent.getEmail());
            
            // Load additional data from database
            loadFullStudentData();
        }
    }
    
    private void loadFullStudentData() {
        // Try to load guardian info if columns exist
        String sql = "SELECT section, guardian_name FROM students WHERE student_number = ?";
        
        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, currentStudent.getStudentNumber());
            var rs = stmt.executeQuery();
            
            if (rs.next()) {
                String section = rs.getString("section");
                String guardianName = rs.getString("guardian_name");
                
                if (section != null) sectionTXT.setText(section);
                if (guardianName != null) GNameField.setText(guardianName);
                // GEmailField left empty or can use student email as fallback
                if (GEmailField != null) GEmailField.setText("");
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading student data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleUpdateClick(ActionEvent event) {
        updateStudent();
    }
    
    @FXML
    private void handleCancelClick(ActionEvent event) {
        closeForm();
    }
    
    private void updateStudent() {
        String firstName = FnameField.getText().trim();
        String lastName = LnameField.getText().trim();
        String section = sectionTXT.getText().trim();
        String studentEmail = studentEmailField.getText().trim();
        String guardianName = GNameField.getText().trim();
        String guardianEmail = GEmailField.getText().trim();
        
        Stage stage = (Stage) studentID.getScene().getWindow();
        
        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || studentEmail.isEmpty() || guardianEmail.isEmpty()) {
            ToastNotification.show(stage, ToastNotification.ToastType.WARNING, "Please fill in all required fields");
            return;
        }
        
        // Validate email formats
        if (!studentEmail.contains("@") || !studentEmail.contains(".")) {
            ToastNotification.show(stage, ToastNotification.ToastType.WARNING, "Please enter a valid student email");
            return;
        }
        
        // Skip guardian email validation if empty
        if (!guardianEmail.isEmpty() && (!guardianEmail.contains("@") || !guardianEmail.contains("."))) {
            ToastNotification.show(stage, ToastNotification.ToastType.WARNING, "Please enter a valid guardian email");
            return;
        }
        
        // Update in database (without guardian_email if column doesn't exist)
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, section = ?, guardian_name = ? " +
                    "WHERE student_number = ?";
        
        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, studentEmail);
            stmt.setString(4, section);
            stmt.setString(5, guardianName);
            stmt.setString(6, currentStudent.getStudentNumber());
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                CustomModal.showInfo(stage, "Success", 
                        "Student updated successfully!\n\n" +
                        "Student Number: " + currentStudent.getStudentNumber() + "\n" +
                        "Name: " + firstName + " " + lastName);
                stage.close();
            } else {
                CustomModal.showError(stage, "Error", "Could not update student!");
            }
            
        } catch (SQLException e) {
            CustomModal.showError(stage, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void closeForm() {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
}
