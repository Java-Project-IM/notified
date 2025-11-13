/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.SubjectEntry;
import com.notif1ed.repository.SubjectRepository;
import com.notif1ed.service.SubjectService;
import com.notif1ed.util.SessionManager;
import com.notif1ed.util.ToastNotification;
import com.notif1ed.util.CustomModal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map;
import java.time.LocalDateTime;
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
public class SubjectPageController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(SubjectPageController.class);
    private final SubjectService subjectService = new SubjectService();

    @FXML
    private TableView<SubjectEntry> subjectTable;
    @FXML
    private TableColumn<SubjectEntry, String> subjectCodeCol;
    @FXML
    private TableColumn<SubjectEntry, String> subjectNameCol;
    @FXML
    private TableColumn<SubjectEntry, Integer> yearLevelCol;
    @FXML
    private TableColumn<SubjectEntry, String> sectionCol;
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
    private Button addSubjectButton;
    @FXML
    private javafx.scene.text.Text timeLabel;
    @FXML
    private javafx.scene.text.Text dateLabel;
    
    private Timeline clock;
    
    private ObservableList<SubjectEntry> subjectList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Validate session
        if (!SessionManager.getInstance().isLoggedIn()) {
            log.warn("Unauthorized access attempt to Subjects page");
            return;
        }
        log.info("Initializing Subjects page for user: {}", SessionManager.getInstance().getUserName());
        
        // Set up table columns if they exist
        if (subjectCodeCol != null) {
            subjectCodeCol.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        }
        if (subjectNameCol != null) {
            subjectNameCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        }
        if (yearLevelCol != null) {
            yearLevelCol.setCellValueFactory(new PropertyValueFactory<>("yearLevel"));
        }
        if (sectionCol != null) {
            sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        }
        
        // Load subjects from database
        loadSubjects();
        
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
        // Already on subjects page, just refresh
        refreshTable();
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
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        boolean confirmed = CustomModal.showConfirmation(
            stage,
            "Logout Confirmation",
            "Are you sure you want to logout?",
            "Logout",
            "Cancel"
        );
        
        if (confirmed) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/LandingPage.fxml"));
                Scene scene = new Scene(loader.load());
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
    
    @FXML
    private void handleAddSubjectClick(ActionEvent event) {
        handleAddSubject(event);
    }
    
    @FXML
    private void handleAddSubject(ActionEvent event) {
        Stage stage = (Stage) subjectTable.getScene().getWindow();
        
        // Create form fields
        CustomModal.FormField[] fields = {
            new CustomModal.FormField("subjectCode", "Subject Code", "text", true),
            new CustomModal.FormField("section", "Section", "text", true),
            new CustomModal.FormField("subjectName", "Subject Name", "text", true),
            new CustomModal.FormField("yearLevel", "Year Level", "number", true),
            new CustomModal.FormField("description", "Description", "text", false)
        };
        
        // Show form modal
        Map<String, String> result = CustomModal.showForm(stage, "Add New Subject", "📚", fields);
        
        if (result != null) {
            // Validate and add subject
            String subjectCode = result.get("subjectCode").trim();
            String section = result.get("section").trim();
            String subjectName = result.get("subjectName").trim();
            String yearLevel = result.get("yearLevel").trim();
            String description = result.getOrDefault("description", "").trim();
            
            try {
                int yearLevelInt = Integer.parseInt(yearLevel);
                
                // Add subject using service
                boolean success = subjectService.addSubject(subjectCode, subjectName, yearLevelInt, section, description);
                
                if (success) {
                    log.info("Subject added successfully: {}", subjectCode);
                    ToastNotification.show(stage, ToastNotification.ToastType.SUCCESS, 
                        "Subject added successfully: " + subjectCode);
                    refreshTable();
                } else {
                    log.warn("Failed to add subject - duplicate code: {}", subjectCode);
                    ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                        "Subject code already exists!");
                }
                
            } catch (NumberFormatException e) {
                log.warn("Invalid year level format: {}", yearLevel);
                ToastNotification.show(stage, ToastNotification.ToastType.WARNING, 
                    "Year Level must be a valid number");
            } catch (Exception e) {
                log.error("Error adding subject", e);
                ToastNotification.show(stage, ToastNotification.ToastType.ERROR, 
                    "Error adding subject: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleOpenSubjectClick(ActionEvent event) {
        // Get selected subject
        if (subjectTable.getSelectionModel().getSelectedItem() != null) {
            SubjectEntry selectedSubject = subjectTable.getSelectionModel().getSelectedItem();
            openSubjectDetailView(selectedSubject);
        } else {
            Stage stage = (Stage) subjectTable.getScene().getWindow();
            ToastNotification.show(stage, ToastNotification.ToastType.WARNING, "Please select a subject to open");
        }
    }
    
    private void openSubjectDetailView(SubjectEntry subject) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/notif1ed/view/SubjectDetailView.fxml"));
            Parent root = loader.load();
            
            // Get the controller and pass the subject data
            SubjectDetailController controller = loader.getController();
            controller.setSubject(subject);
            
            Stage stage = new Stage();
            stage.setTitle(subject.getSubjectCode() + " - Class Management");
            stage.setScene(new Scene(root));
            stage.show();
            
            // Refresh table when detail window is closed
            stage.setOnHidden(e -> refreshTable());
        } catch (IOException e) {
            Stage stage = (Stage) subjectTable.getScene().getWindow();
            ToastNotification.show(stage, ToastNotification.ToastType.ERROR, "Could not open subject detail view");
            e.printStackTrace();
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
    
    private void loadSubjects() {
        log.debug("Loading subjects from database");
        subjectList.clear();
        
        try {
            // Get subjects from service and convert to SubjectEntry
            List<SubjectRepository.Subject> serviceSubjects = subjectService.getAllSubjects();
            
            for (SubjectRepository.Subject serviceSubject : serviceSubjects) {
                SubjectEntry entry = new SubjectEntry(
                    serviceSubject.getSubjectId(),
                    serviceSubject.getSubjectCode(),
                    serviceSubject.getSubjectName(),
                    serviceSubject.getYearLevel(),
                    serviceSubject.getSection()
                );
                subjectList.add(entry);
            }
            
            if (subjectTable != null) {
                subjectTable.setItems(subjectList);
            }
            
            log.info("✅ Loaded {} subjects from database", subjectList.size());
        } catch (Exception e) {
            log.error("Error loading subjects", e);
            if (homeButton != null && homeButton.getScene() != null) {
                Stage stage = (Stage) homeButton.getScene().getWindow();
                ToastNotification.show(stage, ToastNotification.ToastType.ERROR, "Error loading subjects from database");
            }
        }
    }
    
    public void refreshTable() {
        loadSubjects();
    }
}
