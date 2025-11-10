/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vincent Martin
 */
public class HomepageController implements Initializable {

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalSubjectsLabel;
    @FXML
    private Label totalRecordsLabel;
    @FXML
    private Label todayRecordsLabel;
    @FXML
    private Button homeButton;
    @FXML
    private Button subjectsButton;
    @FXML
    private Button studentsButton;
    @FXML
    private Button recordsButton;
    @FXML
    private Button logoutButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load dashboard statistics
        loadDashboardStats();
    }
    
    @FXML
    private void handleHomeClick(ActionEvent event) {
        // Already on home page, just refresh stats
        refreshStats();
    }
    
    @FXML
    private void handleSubjectsClick(ActionEvent event) {
        navigateToPage(event, "SubjectPage.fxml");
    }
    
    @FXML
    private void handleStudentsClick(ActionEvent event) {
        navigateToPage(event, "StudentPage.fxml");
    }
    
    @FXML
    private void handleRecordsClick(ActionEvent event) {
        navigateToPage(event, "RecordsPage.fxml");
    }
    
    @FXML
    private void handleLogoutClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Show confirmation modal
        boolean confirmed = CustomModal.showConfirmation(
            stage,
            "Logout",
            "Are you sure you want to logout?",
            "Logout",
            "Cancel"
        );
        
        if (confirmed) {
            try {
                // Navigate to login page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/LogIn.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("Notifyed - Login");
                stage.show();
                
                ToastNotification.showInfo(stage, "Logged out successfully");
            } catch (IOException e) {
                ToastNotification.showError(stage, "Error during logout");
                e.printStackTrace();
            }
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
            Stage stage = (Stage) homeButton.getScene().getWindow();
            ToastNotification.show(stage, ToastNotification.ToastType.ERROR, "Could not load page: " + fxmlFile);
            e.printStackTrace();
        }
    }
    
    private void loadDashboardStats() {
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                // Get total students
                String studentsQuery = "SELECT COUNT(*) as count FROM students";
                PreparedStatement stmt = conn.prepareStatement(studentsQuery);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && totalStudentsLabel != null) {
                    totalStudentsLabel.setText(String.valueOf(rs.getInt("count")));
                }
                
                // Get total subjects
                String subjectsQuery = "SELECT COUNT(*) as count FROM subjects";
                stmt = conn.prepareStatement(subjectsQuery);
                rs = stmt.executeQuery();
                if (rs.next() && totalSubjectsLabel != null) {
                    totalSubjectsLabel.setText(String.valueOf(rs.getInt("count")));
                }
                
                // Get total records
                String recordsQuery = "SELECT COUNT(*) as count FROM records";
                stmt = conn.prepareStatement(recordsQuery);
                rs = stmt.executeQuery();
                if (rs.next() && totalRecordsLabel != null) {
                    totalRecordsLabel.setText(String.valueOf(rs.getInt("count")));
                }
                
                // Get today's records
                String todayQuery = "SELECT COUNT(*) as count FROM records WHERE DATE(created_at) = CURDATE()";
                stmt = conn.prepareStatement(todayQuery);
                rs = stmt.executeQuery();
                if (rs.next() && todayRecordsLabel != null) {
                    todayRecordsLabel.setText(String.valueOf(rs.getInt("count")));
                }
                
                System.out.println("✅ Dashboard statistics loaded successfully");
            }
        } catch (SQLException e) {
            Stage stage = (Stage) homeButton.getScene().getWindow();
            ToastNotification.show(stage, ToastNotification.ToastType.ERROR, "Error loading dashboard statistics");
            e.printStackTrace();
        }
    }
    
    public void refreshStats() {
        loadDashboardStats();
    }
}
