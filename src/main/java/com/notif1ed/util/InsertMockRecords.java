package com.notif1ed.util;

import com.notif1ed.util.DatabaseConnectionn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to insert mock records data for demonstration purposes
 */
public class InsertMockRecords {
    
    public static void main(String[] args) {
        System.out.println("🔄 Starting mock records insertion...\n");
        
        try (Connection conn = DatabaseConnectionn.connect()) {
            if (conn == null) {
                System.out.println("❌ Failed to connect to database");
                return;
            }
            
            // Get existing student IDs
            List<Integer> studentIds = getStudentIds(conn);
            
            if (studentIds.isEmpty()) {
                System.out.println("❌ No students found in database. Please add students first.");
                return;
            }
            
            System.out.println("✅ Found " + studentIds.size() + " students in database\n");
            
            // Insert mock records for each student
            int recordsInserted = insertMockRecords(conn, studentIds);
            
            System.out.println("\n✅ Successfully inserted " + recordsInserted + " mock records!");
            System.out.println("📊 Navigate to the Records page to view them.");
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<Integer> getStudentIds(Connection conn) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT student_id, first_name, last_name, student_number FROM students ORDER BY student_id";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String number = rs.getString("student_number");
                ids.add(id);
                System.out.println("   📝 Student ID: " + id + " - " + name + " (" + number + ")");
            }
        }
        
        return ids;
    }
    
    private static int insertMockRecords(Connection conn, List<Integer> studentIds) throws SQLException {
        String sql = "INSERT INTO records (student_id, record_type, created_at) VALUES (?, ?, ?)";
        int count = 0;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Create varied mock records with different types and timestamps
            
            // November 6, 2025 - Email sent records
            for (int studentId : studentIds) {
                stmt.setInt(1, studentId);
                stmt.setString(2, "EMAIL_SENT");
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(2025, 11, 6, 9, 15, 0)));
                stmt.addBatch();
                count++;
            }
            
            // November 7, 2025 - Enrollment records
            if (studentIds.size() > 0) {
                stmt.setInt(1, studentIds.get(0));
                stmt.setString(2, "ENROLLMENT");
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(2025, 11, 7, 10, 30, 0)));
                stmt.addBatch();
                count++;
            }
            
            // November 8, 2025 - More emails
            if (studentIds.size() > 1) {
                stmt.setInt(1, studentIds.get(1));
                stmt.setString(2, "EMAIL_SENT");
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(2025, 11, 8, 14, 20, 0)));
                stmt.addBatch();
                count++;
            }
            
            // November 9, 2025 - Student added records
            for (int studentId : studentIds) {
                stmt.setInt(1, studentId);
                stmt.setString(2, "STUDENT_ADDED");
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(2025, 11, 9, 8, 45, 0)));
                stmt.addBatch();
                count++;
            }
            
            // November 10, 2025 (today) - Recent emails
            if (studentIds.size() > 0) {
                stmt.setInt(1, studentIds.get(0));
                stmt.setString(2, "EMAIL_SENT");
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(2025, 11, 10, 11, 0, 0)));
                stmt.addBatch();
                count++;
            }
            
            if (studentIds.size() > 1) {
                stmt.setInt(1, studentIds.get(1));
                stmt.setString(2, "EMAIL_SENT");
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(2025, 11, 10, 13, 30, 0)));
                stmt.addBatch();
                count++;
            }
            
            // Execute batch insert
            int[] results = stmt.executeBatch();
            System.out.println("\n📊 Record types created:");
            System.out.println("   • EMAIL_SENT - Email notification sent to student");
            System.out.println("   • ENROLLMENT - Student enrolled in a subject");
            System.out.println("   • STUDENT_ADDED - New student added to system");
            
            return results.length;
        }
    }
}
