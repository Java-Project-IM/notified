/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.notif1ed.controller;

import com.notif1ed.model.SubjectEntry;
import com.notif1ed.util.DatabaseConnectionn;
import com.notif1ed.util.ToastNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 */
public class SubjectPageController implements Initializable {

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
    private Button addSubjectButton;
    
    private ObservableList<SubjectEntry> subjectList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void handleAddSubjectClick(ActionEvent event) {
        openFormWindow("SubjectForm.fxml", "Add New Subject");
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
    
    private void openFormWindow(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/notif1ed/view/" + fxmlFile));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            
            // Refresh table when form window is closed
            stage.setOnHidden(e -> refreshTable());
        } catch (IOException e) {
            Stage ownerStage = (Stage) homeButton.getScene().getWindow();
            ToastNotification.show(ownerStage, ToastNotification.ToastType.ERROR, "Could not open form");
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
        subjectList.clear();
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                String sql = "SELECT subject_id, subject_code, subject_name, " +
                           "COALESCE(year_level, 0) as year_level, " +
                           "COALESCE(section, '') as section " +
                           "FROM subjects ORDER BY subject_code";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    SubjectEntry subject = new SubjectEntry(
                        rs.getInt("subject_id"),
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getInt("year_level"),
                        rs.getString("section")
                    );
                    subjectList.add(subject);
                }
                
                if (subjectTable != null) {
                    subjectTable.setItems(subjectList);
                }
                
                System.out.println("✅ Loaded " + subjectList.size() + " subjects from database");
            }
        } catch (SQLException e) {
            Stage stage = (Stage) homeButton.getScene().getWindow();
            ToastNotification.show(stage, ToastNotification.ToastType.ERROR, "Error loading subjects from database");
            e.printStackTrace();
        }
    }
    
    public void refreshTable() {
        loadSubjects();
    }
}
