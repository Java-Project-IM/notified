package com.notif1ed.controller;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.model.SubjectEntry;
import com.notif1ed.util.DatabaseConnectionn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SubjectDetailController implements Initializable {

    @FXML
    private Text subjectCodeText;
    @FXML
    private Text subjectNameText;
    @FXML
    private Label yearLevelLabel;
    @FXML
    private Label enrollmentCountLabel;
    @FXML
    private Button btnClose;

    // Enrolled Students Table
    @FXML
    private TextField enrolledSearchField;
    @FXML
    private TableView<StudentEntry> enrolledStudentsTable;
    @FXML
    private TableColumn<StudentEntry, String> enrolledStudentNumberCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledFirstNameCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledLastNameCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledEmailCol;
    @FXML
    private TableColumn<StudentEntry, String> enrolledDateCol;
    @FXML
    private Button btnRemoveStudent;

    // Available Students Table
    @FXML
    private TextField availableSearchField;
    @FXML
    private TableView<StudentEntry> availableStudentsTable;
    @FXML
    private TableColumn<StudentEntry, String> availableStudentNumberCol;
    @FXML
    private TableColumn<StudentEntry, String> availableFirstNameCol;
    @FXML
    private TableColumn<StudentEntry, String> availableLastNameCol;
    @FXML
    private TableColumn<StudentEntry, String> availableEmailCol;
    @FXML
    private Button btnAddStudent;

    private SubjectEntry currentSubject;
    private ObservableList<StudentEntry> enrolledStudentsList = FXCollections.observableArrayList();
    private ObservableList<StudentEntry> availableStudentsList = FXCollections.observableArrayList();
    private FilteredList<StudentEntry> filteredEnrolledStudents;
    private FilteredList<StudentEntry> filteredAvailableStudents;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupEnrolledStudentsTable();
        setupAvailableStudentsTable();
        setupSearchFilters();
    }

    public void setSubject(SubjectEntry subject) {
        this.currentSubject = subject;
        displaySubjectInfo();
        loadEnrolledStudents();
        loadAvailableStudents();
    }

    private void displaySubjectInfo() {
        if (currentSubject != null) {
            subjectCodeText.setText(currentSubject.getSubjectCode());
            subjectNameText.setText(currentSubject.getSubjectName());
            yearLevelLabel.setText("Year Level: " + currentSubject.getYearLevel() + 
                                 " | Section: " + currentSubject.getSection());
        }
    }

    private void setupEnrolledStudentsTable() {
        enrolledStudentNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        enrolledFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        enrolledLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        enrolledEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        enrolledDateCol.setCellValueFactory(cellData -> {
            // This will be populated from the enrollment_date field
            return new javafx.beans.property.SimpleStringProperty("Recently");
        });
    }

    private void setupAvailableStudentsTable() {
        availableStudentNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        availableFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        availableLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        availableEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void setupSearchFilters() {
        // Filter for enrolled students
        filteredEnrolledStudents = new FilteredList<>(enrolledStudentsList, p -> true);
        enrolledSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEnrolledStudents.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getStudentNumber().toLowerCase().contains(lowerCaseFilter)
                        || student.getFirstName().toLowerCase().contains(lowerCaseFilter)
                        || student.getLastName().toLowerCase().contains(lowerCaseFilter)
                        || student.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
        enrolledStudentsTable.setItems(filteredEnrolledStudents);

        // Filter for available students
        filteredAvailableStudents = new FilteredList<>(availableStudentsList, p -> true);
        availableSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAvailableStudents.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getStudentNumber().toLowerCase().contains(lowerCaseFilter)
                        || student.getFirstName().toLowerCase().contains(lowerCaseFilter)
                        || student.getLastName().toLowerCase().contains(lowerCaseFilter)
                        || student.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
        availableStudentsTable.setItems(filteredAvailableStudents);
    }

    private void loadEnrolledStudents() {
        enrolledStudentsList.clear();

        if (currentSubject == null) return;

        String sql = "SELECT s.student_number, s.first_name, " +
                    "COALESCE(s.last_name, s.guardian_name, '') as last_name, " +
                    "s.email, ss.enrollment_date " +
                    "FROM students s " +
                    "INNER JOIN student_subjects ss ON s.student_id = ss.student_id " +
                    "WHERE ss.subject_id = ? AND ss.status = 'active' " +
                    "ORDER BY s.student_number";

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StudentEntry student = new StudentEntry(
                    rs.getString("student_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
                );
                enrolledStudentsList.add(student);
            }

            updateEnrollmentCount();
            System.out.println("✅ Loaded " + enrolledStudentsList.size() + " enrolled students");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                     "Error loading enrolled students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAvailableStudents() {
        availableStudentsList.clear();

        if (currentSubject == null) return;

        String sql = "SELECT s.student_number, s.first_name, " +
                    "COALESCE(s.last_name, s.guardian_name, '') as last_name, " +
                    "s.email " +
                    "FROM students s " +
                    "WHERE s.student_id NOT IN ( " +
                    "    SELECT student_id FROM student_subjects " +
                    "    WHERE subject_id = ? AND status = 'active' " +
                    ") " +
                    "ORDER BY s.student_number";

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StudentEntry student = new StudentEntry(
                    rs.getString("student_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
                );
                availableStudentsList.add(student);
            }

            System.out.println("✅ Loaded " + availableStudentsList.size() + " available students");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                     "Error loading available students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        StudentEntry selectedStudent = availableStudentsTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                     "Please select a student to add to this class.");
            return;
        }

        // Add student to subject in database
        String sql = "INSERT INTO student_subjects (student_id, subject_id, enrollment_date, status) " +
                    "SELECT s.student_id, ?, CURRENT_DATE, 'active' " +
                    "FROM students s WHERE s.student_number = ?";

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            stmt.setString(2, selectedStudent.getStudentNumber());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                         selectedStudent.getFirstName() + " " + selectedStudent.getLastName() + 
                         " has been added to " + currentSubject.getSubjectCode());
                
                // Refresh both tables
                loadEnrolledStudents();
                loadAvailableStudents();
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                showAlert(Alert.AlertType.ERROR, "Already Enrolled", 
                         "This student is already enrolled in this subject.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", 
                         "Error adding student: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveStudent(ActionEvent event) {
        StudentEntry selectedStudent = enrolledStudentsTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                     "Please select a student to remove from this class.");
            return;
        }

        // Confirm removal
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Removal");
        confirmAlert.setHeaderText("Remove Student from Class");
        confirmAlert.setContentText("Are you sure you want to remove " + 
                                   selectedStudent.getFirstName() + " " + 
                                   selectedStudent.getLastName() + 
                                   " from " + currentSubject.getSubjectCode() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                removeStudentFromSubject(selectedStudent);
            }
        });
    }

    private void removeStudentFromSubject(StudentEntry student) {
        String sql = "UPDATE student_subjects " +
                    "SET status = 'dropped' " +
                    "WHERE subject_id = ? AND student_id = ( " +
                    "    SELECT student_id FROM students WHERE student_number = ? " +
                    ")";

        try (Connection conn = DatabaseConnectionn.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentSubject.getId());
            stmt.setString(2, student.getStudentNumber());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                         student.getFirstName() + " " + student.getLastName() + 
                         " has been removed from " + currentSubject.getSubjectCode());
                
                // Refresh both tables
                loadEnrolledStudents();
                loadAvailableStudents();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                     "Error removing student: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void updateEnrollmentCount() {
        enrollmentCountLabel.setText(enrolledStudentsList.size() + " Student" + 
                                    (enrolledStudentsList.size() != 1 ? "s" : "") + " Enrolled");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
