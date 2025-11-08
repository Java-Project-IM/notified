/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package notif1ed;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    
    private void loadSubjects() {
        subjectList.clear();
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                String sql = "SELECT id, subject_code, subject_name, year_level, section FROM subjects ORDER BY subject_code";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    SubjectEntry subject = new SubjectEntry(
                        rs.getInt("id"),
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
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading subjects: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void refreshTable() {
        loadSubjects();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
