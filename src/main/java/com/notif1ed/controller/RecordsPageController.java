/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.RecordEntry;
import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.SessionManager;
import com.notif1ed.util.ToastNotification;

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
import java.util.ResourceBundle;
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
    
    private ObservableList<RecordEntry> recordsList = FXCollections.observableArrayList();
    private javafx.collections.transformation.FilteredList<RecordEntry> filteredRecords;

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
                
                // Setup filtered list for search functionality
                filteredRecords = new javafx.collections.transformation.FilteredList<>(recordsList, p -> true);
                
                // Setup search filter if searchField exists
                if (searchField != null) {
                    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                        filteredRecords.setPredicate(record -> {
                            // If filter text is empty, display all records
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            
                            String lowerCaseFilter = newValue.toLowerCase();
                            
                            // Search in student number, first name, last name, email, and record type
                            if (record.getStudentNumber().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (record.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (record.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (record.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (record.getRecordType().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            
                            return false; // Does not match
                        });
                    });
                }
                
                if (recordsTable != null) {
                    recordsTable.setItems(filteredRecords);
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
}
