/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnectionn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class for Student Form
 */
public class StudentFormController implements Initializable {

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
    private Button addBTN;
    
    @FXML
    private Button CancelButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Auto-generate student number and make it read-only
        generateStudentNumber();
        studentID.setEditable(false);
        studentID.setStyle("-fx-opacity: 1.0; -fx-background-color: #F5F5F5;");
    }
    
    private void generateStudentNumber() {
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
        
        studentID.setText(newStudentNumber);
    }
    
    @FXML
    private void handleSignUpClick(ActionEvent event) {
        if (event.getSource() == addBTN) {
            addStudent();
        } else if (event.getSource() == CancelButton) {
            closeForm();
        }
    }
    
    private void addStudent() {
        String studentNumber = studentID.getText().trim();
        String firstName = FnameField.getText().trim();
        String lastName = LnameField.getText().trim();
        String section = sectionTXT.getText().trim();
        String studentEmail = studentEmailField.getText().trim();
        String guardianName = GNameField.getText().trim();
        String guardianEmail = GEmailField.getText().trim();
        
        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || studentEmail.isEmpty() || guardianEmail.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                    "Please fill in First Name, Last Name, Student Email, and Guardian's Email!");
            return;
        }
        
        // Validate email formats
        if (!studentEmail.contains("@") || !studentEmail.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                    "Please enter a valid student email address!");
            return;
        }
        
        if (!guardianEmail.contains("@") || !guardianEmail.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                    "Please enter a valid guardian email address!");
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
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "Student added successfully!\n" +
                            "Student Number: " + studentNumber + "\n" +
                            "Name: " + firstName + " " + lastName);
                    
                    // Close the form window after success
                    Stage stage = (Stage) studentID.getScene().getWindow();
                    stage.close();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to database!");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                showAlert(Alert.AlertType.ERROR, "Error", "This student number already exists!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }
    
    private void closeForm() {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
