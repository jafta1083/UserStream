package com.userstream.db;

import java.sql.*;

/**
 * Database configuration and connection management.
 * User-specific database operations have been moved to the users module
 * to avoid circular dependencies between common and users modules.
 */
public class DatabaseConfig {

    private static final String URL = "jdbc:sqlite:userstream.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    surname TEXT,
                    email TEXT
                );
                """;

        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute a generic table creation SQL statement.
     */
    public static void executeStatement(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
