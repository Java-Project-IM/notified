package com.notif1ed.service;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service class for student business logic.
 * Acts as an intermediary between controllers and repositories.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class StudentService {
    
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository repository;
    
    /**
     * Constructor with dependency injection.
     */
    public StudentService() {
        this.repository = new StudentRepository();
    }
    
    /**
     * Constructor for testing with mock repository.
     * 
     * @param repository the repository to use
     */
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Gets all students.
     * 
     * @return list of all students
     */
    public List<StudentEntry> getAllStudents() {
        return repository.findAll();
    }
    
    /**
     * Finds a student by student number.
     * 
     * @param studentNumber the student number to search for
     * @return Optional containing the student if found
     */
    public Optional<StudentEntry> getStudentByNumber(String studentNumber) {
        return repository.findByStudentNumber(studentNumber);
    }
    
    /**
     * Adds a new student.
     * 
     * @param student the student to add
     * @param guardianName the guardian's name
     * @param guardianEmail the guardian's email
     * @param section the student's section
     * @return true if successful, false otherwise
     */
    public boolean addStudent(StudentEntry student, String guardianName, String guardianEmail, String section) {
        log.info("Adding new student: {}", student.getStudentNumber());
        return repository.save(student, guardianName, guardianEmail, section);
    }
    
    /**
     * Updates an existing student.
     * 
     * @param student the student with updated information
     * @param guardianName the guardian's name
     * @param guardianEmail the guardian's email
     * @param section the student's section
     * @return true if successful, false otherwise
     */
    public boolean updateStudent(StudentEntry student, String guardianName, String guardianEmail, String section) {
        log.info("Updating student: {}", student.getStudentNumber());
        return repository.update(student, guardianName, guardianEmail, section);
    }
    
    /**
     * Deletes a student.
     * 
     * @param studentNumber the student number to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteStudent(String studentNumber) {
        log.info("Deleting student: {}", studentNumber);
        return repository.delete(studentNumber);
    }
    
    /**
     * Generates the next available student number.
     * 
     * @param yearPrefix the year prefix (e.g., "25-")
     * @return the next student number
     */
    public String generateNextStudentNumber(String yearPrefix) {
        return repository.getNextStudentNumber(yearPrefix);
    }
}
