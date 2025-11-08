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
import java.time.LocalDate;
import java.time.LocalTime;
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
public class RecordsPageController implements Initializable {

    @FXML
    private TableView<RecordEntry> recordsTable;
    @FXML
    private TableColumn<RecordEntry, String> idCol;
    @FXML
    private TableColumn<RecordEntry, String> surnameCol;
    @FXML
    private TableColumn<RecordEntry, String> firstNameCol;
    @FXML
    private TableColumn<RecordEntry, String> guardianEmailCol;
    @FXML
    private TableColumn<RecordEntry, LocalDate> dateCol;
    @FXML
    private TableColumn<RecordEntry, LocalTime> timeCol;
    @FXML
    private TableColumn<RecordEntry, String> typeCol;
    
    private ObservableList<RecordEntry> recordsList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up table columns if they exist
        if (idCol != null) {
            idCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        }
        if (surnameCol != null) {
            surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        }
        if (firstNameCol != null) {
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        }
        if (guardianEmailCol != null) {
            guardianEmailCol.setCellValueFactory(new PropertyValueFactory<>("guardianEmail"));
        }
        if (dateCol != null) {
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        }
        if (timeCol != null) {
            timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        }
        if (typeCol != null) {
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        }
        
        // Load records from database
        loadRecords();
    }
    
    private void loadRecords() {
        recordsList.clear();
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn != null) {
                // Join records with students to get student details
                String sql = "SELECT r.id, s.student_number, s.first_name, s.last_name, s.email, " +
                           "r.record_date, r.record_time, r.record_type " +
                           "FROM records r " +
                           "JOIN students s ON r.student_id = s.id " +
                           "ORDER BY r.record_date DESC, r.record_time DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    RecordEntry record = new RecordEntry(
                        rs.getInt("id"),
                        rs.getString("student_number"),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("email"),
                        rs.getDate("record_date").toLocalDate(),
                        rs.getTime("record_time").toLocalTime(),
                        rs.getString("record_type")
                    );
                    recordsList.add(record);
                }
                
                if (recordsTable != null) {
                    recordsTable.setItems(recordsList);
                }
                
                System.out.println("✅ Loaded " + recordsList.size() + " records from database");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading records: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void refreshTable() {
        loadRecords();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
