/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package notif1ed;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private TextField sectionTXT;
    
    @FXML
    private TextField GNameField;
    
    @FXML
    private PasswordField GEmailField;
    
    @FXML
    private Button addBTN;
    
    @FXML
    private Button CancelButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup button actions
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
        String section = sectionTXT.getText().trim();
        String guardianName = GNameField.getText().trim();
        String guardianEmail = GEmailField.getText().trim();
        
        // Validate inputs
        if (studentNumber.isEmpty() || firstName.isEmpty() || guardianEmail.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                    "Please fill in Student Number, First Name, and Guardian's Email!");
            return;
        }
        
        // Save to database
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                String sql = "INSERT INTO students (student_number, first_name, section, guardian_name, email, created_by) VALUES (?, ?, ?, ?, ?, 1)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, studentNumber);
                stmt.setString(2, firstName);
                stmt.setString(3, section);
                stmt.setString(4, guardianName);
                stmt.setString(5, guardianEmail);
                
                int rowsInserted = stmt.executeUpdate();
                
                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "Student added successfully!\n" +
                            "Student Number: " + studentNumber + "\n" +
                            "Name: " + firstName);
                    
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
    
    private void clearFields() {
        studentID.clear();
        FnameField.clear();
        sectionTXT.clear();
        GNameField.clear();
        GEmailField.clear();
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
