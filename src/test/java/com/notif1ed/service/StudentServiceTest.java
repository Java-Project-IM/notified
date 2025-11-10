package com.notif1ed.service;

import com.notif1ed.model.StudentEntry;
import com.notif1ed.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StudentService class.
 * Uses Mockito to mock the StudentRepository dependency.
 */
class StudentServiceTest {

    @Mock
    private StudentRepository mockRepository;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(mockRepository);
    }

    @Test
    @DisplayName("Should retrieve all students")
    void testGetAllStudents() {
        // Arrange
        StudentEntry student1 = new StudentEntry("21-0001", "John", "Doe", "john@example.com");
        student1.setGuardianName("Jane Doe");
        student1.setGuardianEmail("jane@example.com");
        student1.setSection("Section A");
        
        StudentEntry student2 = new StudentEntry("21-0002", "Mary", "Smith", "mary@example.com");
        student2.setGuardianName("Bob Smith");
        student2.setGuardianEmail("bob@example.com");
        student2.setSection("Section B");
        
        List<StudentEntry> expectedStudents = Arrays.asList(student1, student2);
        
        when(mockRepository.findAll()).thenReturn(expectedStudents);
        
        // Act
        List<StudentEntry> actualStudents = studentService.getAllStudents();
        
        // Assert
        assertEquals(2, actualStudents.size());
        assertEquals(expectedStudents, actualStudents);
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve student by number")
    void testGetStudentByNumber_Found() {
        // Arrange
        String studentNumber = "21-0001";
        StudentEntry expectedStudent = new StudentEntry(studentNumber, "John", "Doe", "john@example.com");
        expectedStudent.setGuardianName("Jane Doe");
        expectedStudent.setGuardianEmail("jane@example.com");
        expectedStudent.setSection("Section A");
        
        when(mockRepository.findByStudentNumber(studentNumber)).thenReturn(Optional.of(expectedStudent));
        
        // Act
        Optional<StudentEntry> result = studentService.getStudentByNumber(studentNumber);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedStudent, result.get());
        verify(mockRepository, times(1)).findByStudentNumber(studentNumber);
    }

    @Test
    @DisplayName("Should return empty when student not found")
    void testGetStudentByNumber_NotFound() {
        // Arrange
        String studentNumber = "99-9999";
        when(mockRepository.findByStudentNumber(studentNumber)).thenReturn(Optional.empty());
        
        // Act
        Optional<StudentEntry> result = studentService.getStudentByNumber(studentNumber);
        
        // Assert
        assertFalse(result.isPresent());
        verify(mockRepository, times(1)).findByStudentNumber(studentNumber);
    }

    @Test
    @DisplayName("Should add new student successfully")
    void testAddStudent_Success() {
        // Arrange
        StudentEntry student = new StudentEntry("21-0001", "John", "Doe", "john@example.com");
        when(mockRepository.save(eq(student), anyString(), anyString(), anyString())).thenReturn(true);
        
        // Act
        boolean result = studentService.addStudent(student, "Jane Doe", "jane@example.com", "Section A");
        
        // Assert
        assertTrue(result);
        verify(mockRepository, times(1)).save(eq(student), eq("Jane Doe"), 
                                              eq("jane@example.com"), eq("Section A"));
    }

    @Test
    @DisplayName("Should fail to add student when repository fails")
    void testAddStudent_Failure() {
        // Arrange
        StudentEntry student = new StudentEntry("21-0001", "John", "Doe", "john@example.com");
        when(mockRepository.save(eq(student), anyString(), anyString(), anyString())).thenReturn(false);
        
        // Act
        boolean result = studentService.addStudent(student, "Jane Doe", "jane@example.com", "Section A");
        
        // Assert
        assertFalse(result);
        verify(mockRepository, times(1)).save(any(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should update student successfully")
    void testUpdateStudent_Success() {
        // Arrange
        StudentEntry student = new StudentEntry("21-0001", "John", "Doe Updated", "john@example.com");
        when(mockRepository.update(eq(student), anyString(), anyString(), anyString())).thenReturn(true);
        
        // Act
        boolean result = studentService.updateStudent(student, "Jane Doe", "jane@example.com", "Section B");
        
        // Assert
        assertTrue(result);
        verify(mockRepository, times(1)).update(eq(student), eq("Jane Doe"), 
                                                eq("jane@example.com"), eq("Section B"));
    }

    @Test
    @DisplayName("Should delete student successfully")
    void testDeleteStudent_Success() {
        // Arrange
        String studentNumber = "21-0001";
        when(mockRepository.delete(studentNumber)).thenReturn(true);
        
        // Act
        boolean result = studentService.deleteStudent(studentNumber);
        
        // Assert
        assertTrue(result);
        verify(mockRepository, times(1)).delete(studentNumber);
    }

    @Test
    @DisplayName("Should generate next student number")
    void testGenerateNextStudentNumber() {
        // Arrange
        String yearPrefix = "24";
        String expectedNumber = "24-0005";
        when(mockRepository.getNextStudentNumber(yearPrefix)).thenReturn(expectedNumber);
        
        // Act
        String actualNumber = studentService.generateNextStudentNumber(yearPrefix);
        
        // Assert
        assertEquals(expectedNumber, actualNumber);
        verify(mockRepository, times(1)).getNextStudentNumber(yearPrefix);
    }

    @Test
    @DisplayName("Should handle empty list when no students exist")
    void testGetAllStudents_EmptyList() {
        // Arrange
        when(mockRepository.findAll()).thenReturn(Arrays.asList());
        
        // Act
        List<StudentEntry> students = studentService.getAllStudents();
        
        // Assert
        assertTrue(students.isEmpty());
        verify(mockRepository, times(1)).findAll();
    }
}
