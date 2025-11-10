package com.notif1ed.service;

import com.notif1ed.repository.RecordRepository;
import com.notif1ed.repository.RecordRepository.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Record business logic.
 * Provides high-level operations for record management.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class RecordService {
    
    private static final Logger log = LoggerFactory.getLogger(RecordService.class);
    private final RecordRepository repository;
    
    /**
     * Default constructor using default repository.
     */
    public RecordService() {
        this.repository = new RecordRepository();
    }
    
    /**
     * Constructor for dependency injection (useful for testing).
     * 
     * @param repository the record repository to use
     */
    public RecordService(RecordRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Retrieves all records.
     * 
     * @return list of all records ordered by date (newest first)
     */
    public List<Record> getAllRecords() {
        log.debug("Fetching all records");
        return repository.findAll();
    }
    
    /**
     * Gets records filtered by type.
     * 
     * @param recordType the record type to filter by
     * @return list of matching records
     */
    public List<Record> getRecordsByType(String recordType) {
        log.debug("Fetching records by type: {}", recordType);
        return repository.findByType(recordType);
    }
    
    /**
     * Gets records within a date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of records within the date range
     */
    public List<Record> getRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching records between {} and {}", startDate, endDate);
        return repository.findByDateRange(startDate, endDate);
    }
    
    /**
     * Gets a record by its ID.
     * 
     * @param recordId the record ID
     * @return Optional containing the record if found
     */
    public Optional<Record> getRecordById(int recordId) {
        log.debug("Fetching record by ID: {}", recordId);
        return repository.findById(recordId);
    }
    
    /**
     * Adds a new record.
     * 
     * @param recordType the record type (e.g., "email_sent", "student_added")
     * @param description brief description of the record
     * @param details detailed information about the record
     * @return true if added successfully
     */
    public boolean addRecord(String recordType, String description, String details) {
        log.info("Adding new record: {} - {}", recordType, description);
        return repository.save(recordType, description, details);
    }
    
    /**
     * Deletes a record.
     * 
     * @param recordId the record ID to delete
     * @return true if deleted successfully
     */
    public boolean deleteRecord(int recordId) {
        log.info("Deleting record: {}", recordId);
        return repository.delete(recordId);
    }
    
    /**
     * Gets the count of records by type.
     * Useful for dashboard statistics.
     * 
     * @param recordType the record type
     * @return count of records
     */
    public int getRecordCountByType(String recordType) {
        return repository.countByType(recordType);
    }
    
    /**
     * Records an email sent event.
     * Convenience method for common record type.
     * 
     * @param recipient the email recipient
     * @param subject the email subject
     * @return true if recorded successfully
     */
    public boolean recordEmailSent(String recipient, String subject) {
        String description = "Email sent to " + recipient;
        String details = "Subject: " + subject;
        return addRecord("email_sent", description, details);
    }
    
    /**
     * Records a student added event.
     * Convenience method for common record type.
     * 
     * @param studentNumber the student number
     * @param studentName the student name
     * @return true if recorded successfully
     */
    public boolean recordStudentAdded(String studentNumber, String studentName) {
        String description = "Student added: " + studentName;
        String details = "Student Number: " + studentNumber;
        return addRecord("student_added", description, details);
    }
    
    /**
     * Records a subject enrollment event.
     * Convenience method for common record type.
     * 
     * @param studentNumber the student number
     * @param subjectCode the subject code
     * @return true if recorded successfully
     */
    public boolean recordEnrollment(String studentNumber, String subjectCode) {
        String description = "Student enrolled in subject";
        String details = "Student: " + studentNumber + ", Subject: " + subjectCode;
        return addRecord("enrollment", description, details);
    }
}
