package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.WelcomeDialog;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
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

        // Get current stage for toast notifications
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            ToastNotification.showWarning(stage, "Please enter both email and password!");
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
                    
                    // Navigate to homepage first
                    navigateToHomepage(event, userName);
                } else {
                    // Login failed
                    ToastNotification.showError(stage, "Invalid email or password!");
                }
            } else {
                ToastNotification.showError(stage, "Could not connect to database!");
            }
        } catch (SQLException e) {
            ToastNotification.showError(stage, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void navigateToHomepage(ActionEvent event, String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/Homepage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Notifyed - Home");
            stage.show();
            
            // Show welcome dialog after the scene is fully loaded
            Platform.runLater(() -> {
                WelcomeDialog.show(stage, userName);
            });
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
