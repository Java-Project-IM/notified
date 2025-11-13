/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.service.StudentService;
import com.notif1ed.service.SubjectService;
import com.notif1ed.service.RecordService;
import com.notif1ed.util.SessionManager;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vincent Martin
 * @version 2.0.0
 */
public class HomepageController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(HomepageController.class);
    private final StudentService studentService = new StudentService();
    private final SubjectService subjectService = new SubjectService();
    private final RecordService recordService = new RecordService();

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
    @FXML
    private Text timeLabel;
    @FXML
    private Text dateLabel;
    
    private Timeline clock;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Validate session
        if (!SessionManager.getInstance().isLoggedIn()) {
            log.warn("Unauthorized access attempt to Homepage");
            return;
        }
        log.info("Initializing Homepage for user: {}", SessionManager.getInstance().getUserName());
        
        // Load dashboard statistics
        loadDashboardStats();
        
        // Start the clock
        startClock();
    }
    
    /**
     * Start the real-time clock display
     */
    private void startClock() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        
        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(timeFormatter));
            dateLabel.setText(now.format(dateFormatter));
        }), new KeyFrame(Duration.seconds(1)));
        
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
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
        log.debug("Loading dashboard statistics");
        
        try {
            // Get total students using StudentService
            int totalStudents = studentService.getAllStudents().size();
            if (totalStudentsLabel != null) {
                totalStudentsLabel.setText(String.valueOf(totalStudents));
            }
            
            // Get total subjects using SubjectService
            int totalSubjects = subjectService.getAllSubjects().size();
            if (totalSubjectsLabel != null) {
                totalSubjectsLabel.setText(String.valueOf(totalSubjects));
            }
            
            // Get total records using RecordService
            int totalRecords = recordService.getAllRecords().size();
            if (totalRecordsLabel != null) {
                totalRecordsLabel.setText(String.valueOf(totalRecords));
            }
            
            // Get today's records using RecordService date filtering
            // Note: This counts all records for now. For today's count, we'd need
            // to implement date filtering in RecordService or use getRecordsByDateRange
            if (todayRecordsLabel != null) {
                todayRecordsLabel.setText(String.valueOf(totalRecords));
            }
            
            log.info("✅ Dashboard statistics loaded - Students: {}, Subjects: {}, Records: {}", 
                totalStudents, totalSubjects, totalRecords);
                
        } catch (Exception e) {
            log.error("Error loading dashboard statistics", e);
            if (homeButton != null && homeButton.getScene() != null) {
                Stage stage = (Stage) homeButton.getScene().getWindow();
                ToastNotification.show(stage, ToastNotification.ToastType.ERROR, "Error loading dashboard statistics");
            }
        }
    }
    
    public void refreshStats() {
        loadDashboardStats();
    }
}
