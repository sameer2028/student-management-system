package com.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    // Database connection details
    // Make sure to change 'your_password' to your actual MySQL root password
    private static final String URL = "jdbc:mysql://localhost:3306/sisdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Sameer@1234"; // <--- CHANGE THIS

    // Private constructor to prevent instantiation
    private DBManager() {
    }

    /**
     * Gets a new connection to the database.
     * The code that calls this is responsible for closing it
     * (which 'try-with-resources' does automatically).
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
        
        // Always return a new connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}