package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnectionn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginClick(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter both email and password!");
            return;
        }

        // Check credentials in database
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                String sql = "SELECT user_id, name, email FROM users WHERE email = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, password);
                
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    // Login successful
                    String userName = rs.getString("name");
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", 
                            "Welcome back, " + userName + "!");
                    
                    // Navigate to homepage or main application
                    navigateToHomepage(event);
                } else {
                    // Login failed
                    showAlert(Alert.AlertType.ERROR, "Login Failed", 
                            "Invalid email or password!");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to database!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
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
    
    private void navigateToHomepage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/Homepage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Notifyed - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignUpRedirect(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/SignUp.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Notifyed - Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
