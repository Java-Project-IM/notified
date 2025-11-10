package com.notif1ed.repository;

import com.notif1ed.util.DatabaseConnection;
import com.notif1ed.util.Constants;
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
 * Repository class for Subject data access operations.
 * Handles all database interactions for subjects.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class SubjectRepository {
    
    private static final Logger log = LoggerFactory.getLogger(SubjectRepository.class);
    
    /**
     * Retrieves all subjects from the database.
     * 
     * @return list of all subjects
     */
    public List<Subject> findAll() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects ORDER BY subject_code";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                subjects.add(mapResultSetToSubject(rs));
            }
            
            log.info("Retrieved {} subjects from database", subjects.size());
            
        } catch (SQLException e) {
            log.error("Error retrieving subjects", e);
        }
        
        return subjects;
    }
    
    /**
     * Finds a subject by its code.
     * 
     * @param subjectCode the subject code to search for
     * @return Optional containing the subject if found, empty otherwise
     */
    public Optional<Subject> findByCode(String subjectCode) {
        String sql = "SELECT * FROM subjects WHERE subject_code = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subjectCode);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Subject subject = mapResultSetToSubject(rs);
                    log.debug("Found subject: {}", subjectCode);
                    return Optional.of(subject);
                }
            }
            
        } catch (SQLException e) {
            log.error("Error finding subject by code: {}", subjectCode, e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Saves a new subject to the database.
     * 
     * @param subjectCode the subject code
     * @param subjectName the subject name
     * @param yearLevel the year level
     * @param section the section
     * @param description the subject description
     * @return true if saved successfully, false otherwise
     */
    public boolean save(String subjectCode, String subjectName, int yearLevel, String section, String description) {
        String sql = "INSERT INTO subjects (subject_code, subject_name, year_level, section, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subjectCode);
            stmt.setString(2, subjectName);
            stmt.setInt(3, yearLevel);
            stmt.setString(4, section);
            stmt.setString(5, description);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Subject saved: {}", subjectCode);
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error saving subject: {}", subjectCode, e);
        }
        
        return false;
    }
    
    /**
     * Updates an existing subject.
     * 
     * @param subjectCode the subject code (cannot be changed)
     * @param subjectName the new subject name
     * @param yearLevel the new year level
     * @param section the new section
     * @param description the new description
     * @return true if updated successfully, false otherwise
     */
    public boolean update(String subjectCode, String subjectName, int yearLevel, String section, String description) {
        String sql = "UPDATE subjects SET subject_name = ?, year_level = ?, section = ?, description = ? WHERE subject_code = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subjectName);
            stmt.setInt(2, yearLevel);
            stmt.setString(3, section);
            stmt.setString(4, description);
            stmt.setString(5, subjectCode);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Subject updated: {}", subjectCode);
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error updating subject: {}", subjectCode, e);
        }
        
        return false;
    }
    
    /**
     * Deletes a subject by its code.
     * 
     * @param subjectCode the subject code to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean delete(String subjectCode) {
        String sql = "DELETE FROM subjects WHERE subject_code = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subjectCode);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Subject deleted: {}", subjectCode);
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error deleting subject: {}", subjectCode, e);
        }
        
        return false;
    }
    
    /**
     * Maps a ResultSet row to a Subject object.
     * 
     * @param rs the ResultSet positioned at a row
     * @return Subject object
     * @throws SQLException if database access error occurs
     */
    private Subject mapResultSetToSubject(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getInt("subject_id"));
        subject.setSubjectCode(rs.getString("subject_code"));
        subject.setSubjectName(rs.getString("subject_name"));
        subject.setYearLevel(rs.getInt("year_level"));
        subject.setSection(rs.getString("section"));
        subject.setDescription(rs.getString("description"));
        return subject;
    }
    
    /**
     * Simple Subject class for repository operations.
     */
    public static class Subject {
        private int subjectId;
        private String subjectCode;
        private String subjectName;
        private int yearLevel;
        private String section;
        private String description;
        
        public int getSubjectId() { return subjectId; }
        public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
        
        public String getSubjectCode() { return subjectCode; }
        public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
        
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        
        public int getYearLevel() { return yearLevel; }
        public void setYearLevel(int yearLevel) { this.yearLevel = yearLevel; }
        
        public String getSection() { return section; }
        public void setSection(String section) { this.section = section; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
