package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnection;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.WelcomeDialog;
import com.notif1ed.util.PasswordUtils;
import com.notif1ed.util.SessionManager;
import com.notif1ed.util.Constants;
import com.notif1ed.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for the login page.
 * Handles user authentication with secure password verification.
 * 
 * @author Notif1ed Development Team
 * @version 2.0.0
 */
public class LoginController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField passwordTextField;
    
    @FXML
    private Button togglePasswordButton;
    
    @FXML
    private Button backButton;
    
    private boolean passwordVisible = false;

    @FXML
    private void handleLoginClick(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Get current stage for toast notifications
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            ToastNotification.showWarning(stage, Constants.WARN_EMPTY_EMAIL_PASSWORD);
            log.warn("Login attempt with empty credentials");
            return;
        }

        // Check credentials in database
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = Constants.SELECT_USER_BY_EMAIL;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String userName = rs.getString("name");
                String storedHash = rs.getString("password");
                
                // Verify password using BCrypt
                if (PasswordUtils.checkPassword(password, storedHash)) {
                    // Login successful - create session
                    SessionManager.getInstance().login(userId, userName, email);
                    log.info("User logged in successfully: {}", email);
                    
                    // Navigate to homepage
                    navigateToHomepage(event, userName);
                } else {
                    // Invalid password
                    ToastNotification.showError(stage, Constants.ERR_INVALID_CREDENTIALS);
                    log.warn("Failed login attempt for: {}", email);
                }
            } else {
                // User not found
                ToastNotification.showError(stage, Constants.ERR_INVALID_CREDENTIALS);
                log.warn("Login attempt for non-existent user: {}", email);
            }
            
        } catch (SQLException e) {
            ErrorHandler.handleDatabaseError(stage, e, "authenticate user");
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
    
    /**
     * Toggle password visibility between masked and visible
     */
    @FXML
    private void handleTogglePassword(ActionEvent event) {
        if (passwordVisible) {
            // Hide password - copy text from TextField to PasswordField
            passwordField.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            passwordField.setVisible(true);
            togglePasswordButton.setText("👁️");
            passwordVisible = false;
        } else {
            // Show password - copy text from PasswordField to TextField
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordTextField.setVisible(true);
            togglePasswordButton.setText("🙈");
            passwordVisible = true;
        }
    }
    
    /**
     * Navigate back to the start/landing page
     */
    @FXML
    private void handleBackClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/LandingPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Notifyed - Welcome");
            stage.show();
            log.info("User returned to landing page");
        } catch (IOException e) {
            log.error("Error loading landing page", e);
            e.printStackTrace();
        }
    }
}
