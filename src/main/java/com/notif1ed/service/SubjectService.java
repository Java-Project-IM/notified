package com.notif1ed.service;

import com.notif1ed.repository.SubjectRepository;
import com.notif1ed.repository.SubjectRepository.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Subject business logic.
 * Provides high-level operations for subject management.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class SubjectService {
    
    private static final Logger log = LoggerFactory.getLogger(SubjectService.class);
    private final SubjectRepository repository;
    
    /**
     * Default constructor using default repository.
     */
    public SubjectService() {
        this.repository = new SubjectRepository();
    }
    
    /**
     * Constructor for dependency injection (useful for testing).
     * 
     * @param repository the subject repository to use
     */
    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Retrieves all subjects.
     * 
     * @return list of all subjects
     */
    public List<Subject> getAllSubjects() {
        log.debug("Fetching all subjects");
        return repository.findAll();
    }
    
    /**
     * Gets a subject by its code.
     * 
     * @param subjectCode the subject code to search for
     * @return Optional containing the subject if found
     */
    public Optional<Subject> getSubjectByCode(String subjectCode) {
        log.debug("Fetching subject by code: {}", subjectCode);
        return repository.findByCode(subjectCode);
    }
    
    /**
     * Adds a new subject.
     * 
     * @param subjectCode the subject code
     * @param subjectName the subject name
     * @param yearLevel the year level
     * @param section the section
     * @param description the subject description
     * @return true if added successfully
     */
    public boolean addSubject(String subjectCode, String subjectName, int yearLevel, String section, String description) {
        log.info("Adding new subject: {}", subjectCode);
        
        // Business logic: Check if subject code already exists
        if (repository.findByCode(subjectCode).isPresent()) {
            log.warn("Subject code already exists: {}", subjectCode);
            return false;
        }
        
        return repository.save(subjectCode, subjectName, yearLevel, section, description);
    }
    
    /**
     * Updates an existing subject.
     * 
     * @param subjectCode the subject code
     * @param subjectName the new subject name
     * @param yearLevel the new year level
     * @param section the new section
     * @param description the new description
     * @return true if updated successfully
     */
    public boolean updateSubject(String subjectCode, String subjectName, int yearLevel, String section, String description) {
        log.info("Updating subject: {}", subjectCode);
        
        // Business logic: Check if subject exists
        if (!repository.findByCode(subjectCode).isPresent()) {
            log.warn("Subject not found for update: {}", subjectCode);
            return false;
        }
        
        return repository.update(subjectCode, subjectName, yearLevel, section, description);
    }
    
    /**
     * Deletes a subject.
     * 
     * @param subjectCode the subject code to delete
     * @return true if deleted successfully
     */
    public boolean deleteSubject(String subjectCode) {
        log.info("Deleting subject: {}", subjectCode);
        return repository.delete(subjectCode);
    }
    
    /**
     * Checks if a subject code already exists.
     * 
     * @param subjectCode the subject code to check
     * @return true if exists, false otherwise
     */
    public boolean subjectExists(String subjectCode) {
        return repository.findByCode(subjectCode).isPresent();
    }
}
