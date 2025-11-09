/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnectionn;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vincent Martin
 */
public class SubjectFormController implements Initializable {

    @FXML
    private TextField nameField;       // Subject Code
    @FXML
    private TextField nameField1;      // Section
    @FXML
    private TextField emailField;      // Subject Name
    @FXML
    private TextField passwordField;   // Year Level
    @FXML
    private Button AddButton1;
    @FXML
    private Button CancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up add button action
        if (AddButton1 != null) {
            AddButton1.setOnAction(this::handleAddClick);
        }
        
        // Set up cancel button action
        if (CancelButton != null) {
            CancelButton.setOnAction(this::handleCancelClick);
        }
    }
    
    @FXML
    private void handleSignUpClick(ActionEvent event) {
        // Legacy method for FXML compatibility
    }
    
    private void handleAddClick(ActionEvent event) {
        String subjectCode = nameField.getText().trim();
        String section = nameField1.getText().trim();
        String subjectName = emailField.getText().trim();
        String yearLevel = passwordField.getText().trim();
        
        // Validate inputs
        if (subjectCode.isEmpty() || section.isEmpty() || subjectName.isEmpty() || yearLevel.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }
        
        // Add subject to database
        try (Connection conn = DatabaseConnectionn.connect()) {
            String sql = "INSERT INTO subjects (subject_code, subject_name, year_level, section) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, subjectCode);
            pstmt.setString(2, subjectName);
            pstmt.setInt(3, Integer.parseInt(yearLevel));
            pstmt.setString(4, section);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Subject added successfully!");
                
                // Close the form window after success
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add subject.");
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Year Level must be a number.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding subject: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleCancelClick(ActionEvent event) {
        // Close the form
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    private void clearFields() {
        nameField.clear();
        nameField1.clear();
        emailField.clear();
        passwordField.clear();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
