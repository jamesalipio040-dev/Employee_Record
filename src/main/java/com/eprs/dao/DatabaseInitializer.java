package com.eprs.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.eprs.dao.DatabaseConnection.getConnection;

public class DatabaseInitializer {
    public static void initialize(){
        new UserDao();

        String createActivityLogsTable = """
            CREATE TABLE IF NOT EXISTS activity_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                username TEXT NOT NULL,
                action TEXT NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createActivityLogsTable);
        } catch (SQLException e) {
            System.err.println("[ERROR] Could not create activity_logs table: " + e.getMessage());
        }

        System.out.println(" -> Database tables verified.");
    }
}