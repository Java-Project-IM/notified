package com.notif1ed.repository;

import com.notif1ed.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for Record data access operations.
 * Handles all database interactions for records.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class RecordRepository {
    
    private static final Logger log = LoggerFactory.getLogger(RecordRepository.class);
    
    /**
     * Retrieves all records from the database.
     * 
     * @return list of all records
     */
    public List<Record> findAll() {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM records ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
            
            log.info("Retrieved {} records from database", records.size());
            
        } catch (SQLException e) {
            log.error("Error retrieving records", e);
        }
        
        return records;
    }
    
    /**
     * Finds records by type.
     * 
     * @param recordType the record type to filter by
     * @return list of records matching the type
     */
    public List<Record> findByType(String recordType) {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM records WHERE record_type = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, recordType);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToRecord(rs));
                }
            }
            
            log.debug("Found {} records of type: {}", records.size(), recordType);
            
        } catch (SQLException e) {
            log.error("Error finding records by type: {}", recordType, e);
        }
        
        return records;
    }
    
    /**
     * Finds records by date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of records within the date range
     */
    public List<Record> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM records WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToRecord(rs));
                }
            }
            
            log.debug("Found {} records between {} and {}", records.size(), startDate, endDate);
            
        } catch (SQLException e) {
            log.error("Error finding records by date range", e);
        }
        
        return records;
    }
    
    /**
     * Finds a record by its ID.
     * 
     * @param recordId the record ID to search for
     * @return Optional containing the record if found
     */
    public Optional<Record> findById(int recordId) {
        String sql = "SELECT * FROM records WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recordId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Record record = mapResultSetToRecord(rs);
                    log.debug("Found record: {}", recordId);
                    return Optional.of(record);
                }
            }
            
        } catch (SQLException e) {
            log.error("Error finding record by ID: {}", recordId, e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Saves a new record to the database.
     * 
     * @param studentId the student ID
     * @param subjectId the subject ID (can be null)
     * @param recordType the record type
     * @param recordData the record data
     * @return true if saved successfully
     */
    public boolean save(int studentId, Integer subjectId, String recordType, String recordData) {
        String sql = "INSERT INTO records (student_id, subject_id, record_type, record_data, created_at) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            if (subjectId != null) {
                stmt.setInt(2, subjectId);
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setString(3, recordType);
            stmt.setString(4, recordData);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Record saved: {} for student ID: {}", recordType, studentId);
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error saving record", e);
        }
        
        return false;
    }
    
    /**
     * Deletes a record by its ID.
     * 
     * @param recordId the record ID to delete
     * @return true if deleted successfully
     */
    public boolean delete(int recordId) {
        String sql = "DELETE FROM records WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, recordId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                log.info("Record deleted: {}", recordId);
                return true;
            }
            
        } catch (SQLException e) {
            log.error("Error deleting record: {}", recordId, e);
        }
        
        return false;
    }
    
    /**
     * Gets count of records by type.
     * 
     * @param recordType the record type
     * @return count of records
     */
    public int countByType(String recordType) {
        String sql = "SELECT COUNT(*) FROM records WHERE record_type = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, recordType);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            log.error("Error counting records by type: {}", recordType, e);
        }
        
        return 0;
    }
    
    /**
     * Maps a ResultSet row to a Record object.
     * 
     * @param rs the ResultSet positioned at a row
     * @return Record object
     * @throws SQLException if database access error occurs
     */
    private Record mapResultSetToRecord(ResultSet rs) throws SQLException {
        Record record = new Record();
        record.setRecordId(rs.getInt("record_id"));
        record.setStudentId(rs.getInt("student_id"));
        record.setSubjectId(rs.getInt("subject_id"));
        record.setRecordType(rs.getString("record_type"));
        record.setRecordData(rs.getString("record_data"));
        
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            record.setCreatedAt(timestamp.toLocalDateTime());
        }
        
        return record;
    }
    
    /**
     * Simple Record class for repository operations.
     * Matches the actual database schema: record_id, student_id, subject_id, record_type, record_data, created_at
     */
    public static class Record {
        private int recordId;
        private int studentId;
        private int subjectId;
        private String recordType;
        private String recordData;
        private LocalDateTime createdAt;
        
        public int getRecordId() { return recordId; }
        public void setRecordId(int recordId) { this.recordId = recordId; }
        
        public int getStudentId() { return studentId; }
        public void setStudentId(int studentId) { this.studentId = studentId; }
        
        public int getSubjectId() { return subjectId; }
        public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
        
        public String getRecordType() { return recordType; }
        public void setRecordType(String recordType) { this.recordType = recordType; }
        
        public String getRecordData() { return recordData; }
        public void setRecordData(String recordData) { this.recordData = recordData; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
