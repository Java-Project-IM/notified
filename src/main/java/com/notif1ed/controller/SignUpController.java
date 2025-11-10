package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.ToastNotification;

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
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleSignUpClick(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Get current stage for toast notifications
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            ToastNotification.showWarning(stage, "Please fill in all fields!");
            return;
        }

        // Save to database
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password); // Note: In production, hash the password!
                
                int rowsInserted = stmt.executeUpdate();
                
                if (rowsInserted > 0) {
                    ToastNotification.showSuccess(stage, "Account created successfully for " + name + "!");
                    
                    // Go to login page after short delay
                    javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
                    delay.setOnFinished(e -> navigateToLogin(event));
                    delay.play();
                }
            } else {
                ToastNotification.showError(stage, "Could not connect to database!");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                ToastNotification.showError(stage, "This email is already registered!");
            } else {
                ToastNotification.showError(stage, "Database error: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }
    
    private void navigateToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/LogIn.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Notifyed - Log In");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginRedirect(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/LogIn.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Notifyed - Log In");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
