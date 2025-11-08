/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package notif1ed;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class for Student Page
 */
public class StudentPageController implements Initializable {

    @FXML
    private TableView<StudentEntry> studentTable;
    
    @FXML
    private TableColumn<StudentEntry, String> studentNumberCol;
    
    @FXML
    private TableColumn<StudentEntry, String> firstNameCol;
    
    @FXML
    private TableColumn<StudentEntry, String> lastNameCol;
    
    @FXML
    private TableColumn<StudentEntry, String> emailCol;
    
    private ObservableList<StudentEntry> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup table columns if they exist
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
        
        // Load students from database
        loadStudents();
    }
    
    private void loadStudents() {
        studentList.clear();
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                String sql = "SELECT student_number, first_name, last_name, email FROM students ORDER BY student_number";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    StudentEntry student = new StudentEntry(
                        rs.getString("student_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                    );
                    studentList.add(student);
                }
                
                if (studentTable != null) {
                    studentTable.setItems(studentList);
                }
                
                System.out.println("✅ Loaded " + studentList.size() + " students from database");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading students: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void refreshTable() {
        loadStudents();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
