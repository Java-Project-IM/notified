package com.notif1ed.repository;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.util.Constants;
import com.notif1ed.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for student data access operations.
 * Handles all database interactions related to students.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class StudentRepository {
    
    private static final Logger log = LoggerFactory.getLogger(StudentRepository.class);
    
    /**
     * Retrieves all students from the database.
     * 
     * @return list of all students
     */
    public List<StudentEntry> findAll() {
        List<StudentEntry> students = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(Constants.SELECT_ALL_STUDENTS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
            log.info("Retrieved {} students from database", students.size());
            
        } catch (SQLException e) {
            log.error("Error retrieving all students", e);
        }
        
        return students;
    }
    
    /**
     * Finds a student by their student number.
     * 
     * @param studentNumber the student number to search for
     * @return Optional containing the student if found, empty otherwise
     */
    public Optional<StudentEntry> findByStudentNumber(String studentNumber) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(Constants.SELECT_STUDENT_BY_NUMBER)) {
            
            stmt.setString(1, studentNumber);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStudent(rs));
                }
            }
            
        } catch (SQLException e) {
            log.error("Error finding student by number: {}", studentNumber, e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Saves a new student to the database.
     * 
     * @param student the student to save
     * @return true if successful, false otherwise
     */
    public boolean save(StudentEntry student, String guardianName, String guardianEmail, String section) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(Constants.INSERT_STUDENT)) {
            
            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, section);
            stmt.setString(6, guardianName);
            stmt.setString(7, guardianEmail);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Student saved: {}", student.getStudentNumber());
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error saving student: {}", student.getStudentNumber(), e);
        }
        
        return false;
    }
    
    /**
     * Updates an existing student in the database.
     * 
     * @param student the student with updated information
     * @return true if successful, false otherwise
     */
    public boolean update(StudentEntry student, String guardianName, String guardianEmail, String section) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(Constants.UPDATE_STUDENT)) {
            
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, section);
            stmt.setString(5, guardianName);
            stmt.setString(6, guardianEmail);
            stmt.setString(7, student.getStudentNumber());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Student updated: {}", student.getStudentNumber());
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error updating student: {}", student.getStudentNumber(), e);
        }
        
        return false;
    }
    
    /**
     * Deletes a student from the database.
     * 
     * @param studentNumber the student number to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(String studentNumber) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(Constants.DELETE_STUDENT)) {
            
            stmt.setString(1, studentNumber);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Student deleted: {}", studentNumber);
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error deleting student: {}", studentNumber, e);
        }
        
        return false;
    }
    
    /**
     * Gets the next available student number based on the year prefix.
     * 
     * @param yearPrefix the year prefix (e.g., "25-")
     * @return the next student number
     */
    public String getNextStudentNumber(String yearPrefix) {
        String defaultNumber = yearPrefix + String.format("%04d", 1);
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(Constants.GET_NEXT_STUDENT_NUMBER)) {
            
            stmt.setString(1, yearPrefix + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String lastNumber = rs.getString("student_number");
                    String numericPart = lastNumber.substring(yearPrefix.length());
                    int nextNumber = Integer.parseInt(numericPart) + 1;
                    return yearPrefix + String.format("%04d", nextNumber);
                }
            }
            
        } catch (SQLException e) {
            log.error("Error generating next student number", e);
        }
        
        return defaultNumber;
    }
    
    /**
     * Maps a ResultSet row to a StudentEntry object.
     * 
     * @param rs the ResultSet to map
     * @return StudentEntry object
     * @throws SQLException if database access error occurs
     */
    private StudentEntry mapResultSetToStudent(ResultSet rs) throws SQLException {
        StudentEntry student = new StudentEntry(
            rs.getString("student_number"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email")
        );
        
        String section = rs.getString("section");
        if (section != null) {
            student.setSection(section);
        }
        
        return student;
    }
}
