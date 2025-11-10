package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnection;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.PasswordUtils;
import com.notif1ed.util.ValidationUtils;
import com.notif1ed.util.Constants;
import com.notif1ed.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Controller for the sign up page.
 * Handles new user registration with secure password hashing.
 * 
 * @author Notif1ed Development Team
 * @version 2.0.0
 */
public class SignUpController {
    
    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleSignUpClick(ActionEvent event) {
        String name = ValidationUtils.sanitizeInput(nameField.getText());
        String email = ValidationUtils.sanitizeInput(emailField.getText());
        String password = passwordField.getText();

        // Get current stage for toast notifications
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Validate inputs
        if (!ValidationUtils.areFieldsNotEmpty(name, email, password)) {
            ToastNotification.showWarning(stage, Constants.ERR_EMPTY_FIELDS);
            return;
        }
        
        // Validate email format
        if (!ValidationUtils.isValidEmail(email)) {
            ToastNotification.showWarning(stage, Constants.ERR_INVALID_EMAIL);
            return;
        }
        
        // Validate password strength
        if (!PasswordUtils.isPasswordStrong(password)) {
            ToastNotification.showWarning(stage, Constants.ERR_WEAK_PASSWORD);
            return;
        }

        // Hash password before storing
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        // Save to database
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = Constants.INSERT_USER;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                log.info("New user registered: {}", email);
                ToastNotification.showSuccess(stage, Constants.SUCCESS_SIGNUP + " for " + name + "!");
                
                // Go to login page after short delay
                javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
                delay.setOnFinished(e -> navigateToLogin(event));
                delay.play();
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                ToastNotification.showError(stage, Constants.ERR_EMAIL_EXISTS);
                log.warn("Duplicate email registration attempt: {}", email);
            } else {
                ErrorHandler.handleDatabaseError(stage, e, "create account");
            }
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
