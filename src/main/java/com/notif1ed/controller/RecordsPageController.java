/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.RecordEntry;
import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.SessionManager;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vincent Martin
 * @version 2.0.0
 */
public class RecordsPageController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(RecordsPageController.class);

    @FXML
    private TableView<RecordEntry> recordsTable;
    @FXML
    private TableColumn<RecordEntry, String> studentNumberCol;
    @FXML
    private TableColumn<RecordEntry, String> firstNameCol;
    @FXML
    private TableColumn<RecordEntry, String> lastNameCol;
    @FXML
    private TableColumn<RecordEntry, String> emailCol;
    @FXML
    private TableColumn<RecordEntry, String> dateCol;
    @FXML
    private TableColumn<RecordEntry, String> typeCol;
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
    private javafx.scene.control.TextField searchField;
    @FXML
    private javafx.scene.control.DatePicker datePicker;
    @FXML
    private javafx.scene.text.Text timeLabel;
    @FXML
    private javafx.scene.text.Text dateLabel;
    
    private Timeline clock;
    
    private ObservableList<RecordEntry> recordsList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Validate session
        if (!SessionManager.getInstance().isLoggedIn()) {
            log.warn("Unauthorized access attempt to Records page");
            return;
        }
        log.info("Initializing Records page for user: {}", SessionManager.getInstance().getUserName());
        
        // Set up table columns to match RecordEntry alias properties
        if (studentNumberCol != null) {
            studentNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        }
        if (firstNameCol != null) {
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        }
        if (lastNameCol != null) {
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        }
        if (emailCol != null) {
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        }
        if (dateCol != null) {
            dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        }
        if (typeCol != null) {
            typeCol.setCellValueFactory(new PropertyValueFactory<>("recordType"));
        }
        
        // Load records from database
        loadRecords();
        
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
        navigateToPage(event, "Homepage.fxml");
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
        // Already on records page, just refresh
        refreshTable();
    }
    
    @FXML
    private void handleLogoutClick(ActionEvent event) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        boolean confirmed = com.notif1ed.util.CustomModal.showConfirmation(
            stage,
            "Logout Confirmation",
            "Are you sure you want to logout?",
            "Logout",
            "Cancel"
        );
        
        if (confirmed) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/LandingPage.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Notif1ed - Welcome");
                stage.show();
                
                // Show logout toast
                ToastNotification.showSuccess(stage, "Logged out successfully");
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
    
    private void loadRecords() {
        log.debug("Loading records from database");
        recordsList.clear();
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                // Join records with students to get student details
                String sql = "SELECT r.record_id, s.student_number, s.first_name, s.last_name, s.email, " +
                           "r.created_at, r.record_type " +
                           "FROM records r " +
                           "JOIN students s ON r.student_id = s.student_id " +
                           "ORDER BY r.created_at DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                    LocalDate date = timestamp.toLocalDateTime().toLocalDate();
                    LocalTime time = timestamp.toLocalDateTime().toLocalTime();
                    
                    RecordEntry record = new RecordEntry(
                        rs.getInt("record_id"),
                        rs.getString("student_number"),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("email"),
                        date,
                        time,
                        rs.getString("record_type")
                    );
                    recordsList.add(record);
                }
                
                if (recordsTable != null) {
                    recordsTable.setItems(recordsList);
                }
                
                log.info("✅ Loaded {} records from database", recordsList.size());
            }
        } catch (SQLException e) {
            log.error("Error loading records", e);
            if (homeButton != null && homeButton.getScene() != null) {
                Stage stage = (Stage) homeButton.getScene().getWindow();
                ToastNotification.show(stage, ToastNotification.ToastType.ERROR, "Error loading records from database");
            }
        }
    }
    
    public void refreshTable() {
        loadRecords();
    }
    
    /**
     * Shows attendance summary statistics
     */
    @FXML
    private void handleShowSummary(ActionEvent event) {
        Stage stage = (Stage) recordsTable.getScene().getWindow();
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                // Get today's date
                LocalDate today = LocalDate.now();
                
                // Count arrivals today
                String arrivalSql = "SELECT COUNT(*) as count FROM records WHERE DATE(created_at) = ? AND record_type = 'Arrival'";
                PreparedStatement arrivalStmt = conn.prepareStatement(arrivalSql);
                arrivalStmt.setDate(1, java.sql.Date.valueOf(today));
                ResultSet arrivalRs = arrivalStmt.executeQuery();
                int arrivals = arrivalRs.next() ? arrivalRs.getInt("count") : 0;
                
                // Count departures today
                String departureSql = "SELECT COUNT(*) as count FROM records WHERE DATE(created_at) = ? AND record_type = 'Departure'";
                PreparedStatement departureStmt = conn.prepareStatement(departureSql);
                departureStmt.setDate(1, java.sql.Date.valueOf(today));
                ResultSet departureRs = departureStmt.executeQuery();
                int departures = departureRs.next() ? departureRs.getInt("count") : 0;
                
                // Total students
                String totalSql = "SELECT COUNT(*) as count FROM students";
                PreparedStatement totalStmt = conn.prepareStatement(totalSql);
                ResultSet totalRs = totalStmt.executeQuery();
                int totalStudents = totalRs.next() ? totalRs.getInt("count") : 0;
                
                // Calculate attendance rate
                double attendanceRate = totalStudents > 0 ? (arrivals * 100.0 / totalStudents) : 0;
                
                // Build summary message
                String summary = String.format(
                    "📊 Attendance Summary for %s\n\n" +
                    "✅ Arrivals: %d\n" +
                    "🚪 Departures: %d\n" +
                    "👥 Total Students: %d\n" +
                    "📈 Attendance Rate: %.1f%%",
                    today.format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    arrivals,
                    departures,
                    totalStudents,
                    attendanceRate
                );
                
                CustomModal.showInfo(stage, "Attendance Summary", summary);
                
                log.info("Attendance summary displayed: {} arrivals, {} departures", arrivals, departures);
            }
        } catch (SQLException e) {
            log.error("Error generating attendance summary", e);
            ToastNotification.showError(stage, "Error generating attendance summary");
        }
    }
    
    /**
     * Gets the predefined message for arrival
     */
    public static String getArrivalMessage(String studentName, String guardianName) {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
        String formattedDate = now.format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        
        return String.format(
            "Dear Mr/Ms %s,\n\n" +
            "We would like to inform you that your child, %s, attended their class in CC103 at %s on %s.\n\n" +
            "To confirm this update, you may contact your child directly or coordinate with their course professor.\n\n" +
            "Thank you and have a great day.\n\n" +
            "— Quezon City University",
            guardianName, studentName, formattedTime, formattedDate
        );
    }
    
    /**
     * Gets the predefined message for departure/dismissal
     */
    public static String getDepartureMessage(String studentName, String guardianName) {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
        String formattedDate = now.format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        
        return String.format(
            "Dear Mr/Ms %s,\n\n" +
            "We would like to inform you that your child, %s, was dismissed from their CC103 class at %s on %s.\n\n" +
            "For any questions or further confirmation, you may contact your child directly or coordinate with their course professor.\n\n" +
            "Thank you, and have a great day.\n\n" +
            "— Quezon City University",
            guardianName, studentName, formattedTime, formattedDate
        );
    }
    
    /**
     * Gets the email subject and message based on record type
     * Returns a String array: [0] = subject, [1] = message
     */
    public static String[] getNotificationContent(String recordType, String studentFullName, String guardianName) {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
        String formattedDate = now.format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        
        String subject;
        String message;
        
        if ("Arrival".equalsIgnoreCase(recordType)) {
            subject = "Attendance Notification";
            message = String.format(
                "Dear Mr/Ms %s,\n\n" +
                "We would like to inform you that your child, %s, attended their class in CC103 at %s on %s.\n\n" +
                "To confirm this update, you may contact your child directly or coordinate with their course professor.\n\n" +
                "Thank you and have a great day.\n\n" +
                "— Quezon City University",
                guardianName, studentFullName, formattedTime, formattedDate
            );
        } else {
            subject = "Dismissal Notification";
            message = String.format(
                "Dear Mr/Ms %s,\n\n" +
                "We would like to inform you that your child, %s, was dismissed from their CC103 class at %s on %s.\n\n" +
                "For any questions or further confirmation, you may contact your child directly or coordinate with their course professor.\n\n" +
                "Thank you, and have a great day.\n\n" +
                "— Quezon City University",
                guardianName, studentFullName, formattedTime, formattedDate
            );
        }
        
        return new String[]{subject, message};
    }
}
