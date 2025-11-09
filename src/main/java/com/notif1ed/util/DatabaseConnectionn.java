package com.notif1ed.util;

import com.notif1ed.util.DatabaseConnectionn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionn {

    // ✅ Update DB name here
    private static final String URL = "jdbc:mysql://localhost:3306/notified_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        Connection connection = null;
        try {
            // ✅ Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Establish Connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Successfully connected to database: notified_DB");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            System.err.println("➡ Please add mysql-connector-j.jar to your project libraries.");
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            System.err.println("➡ Check if MySQL is running, and verify DB name, username, and password.");
            e.printStackTrace();
        }
        return connection;
    }
}
