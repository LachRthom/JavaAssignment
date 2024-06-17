package com.example.javaassignment.DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteConnectionManager {

    // Database location/filepath
    private static final String URL =
            "jdbc:sqlite:src/main/resources/com/example/javaassignment/DataBase/SurveyQuestionsDB";

    /**
     * Establishes a connection to the SQLite database
     *
     * @return - A Connection object representing the connection to the SQLite database
     * @throws SQLException - If an SQL exception occurs while establishing the connection
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish a connection to the SQLite database
            Connection connection = DriverManager.getConnection(URL);
            System.out.println("Database connection established successfully.");
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }
    }

    /**
     * Authenticates the user based on the provided username and password
     *
     * @param connection - The database connection
     * @param username   - The username for authentication
     * @param password   - The password for authentication
     * @return - true if authentication succeeds, false otherwise
     */
    public static boolean authenticate(Connection connection, String username, String password) {
        // Query the database to check if the provided credentials are valid
        String sql = "SELECT COUNT(*) FROM UserLogins WHERE Username = ? AND Password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            // If the result set has any rows, the username and password are valid
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Authentication failed due to an error
        }
    }
}
