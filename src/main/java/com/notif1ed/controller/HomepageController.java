/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package notif1ed;

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
import javafx.scene.control.Alert;
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
        navigateToPage(event, "Student Page.fxml");
    }
    
    @FXML
    private void handleRecordsClick(ActionEvent event) {
        navigateToPage(event, "RecordsPage.fxml");
    }
    
    private void navigateToPage(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load page: " + fxmlFile);
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
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading dashboard statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void refreshStats() {
        loadDashboardStats();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
