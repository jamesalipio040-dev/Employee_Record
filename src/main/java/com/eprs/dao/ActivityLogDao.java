package com.eprs.dao;

import com.eprs.model.ActivityLog;
import com.eprs.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDao {

    public void logActivity(int userId, String username, String action){
        String sql = "INSERT INTO activity_logs(user_id, username, action) VALUES (?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, action);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error Saving Activity Log" + e.getMessage());
        }
    }

    public List<ActivityLog> getAllLogs(){
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT id, user_id, username, action, timestamp FROM activity_logs ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                logs.add(new ActivityLog(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("action"),
                        rs.getString("timestamp")
                ));
            }

        } catch (SQLException e){
            System.err.println("[ERROR] Failed to Retrieve logs: " + e.getMessage());
        }
        return logs;
    }

    public List<ActivityLog> getLogsByUserId(int userId) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT id, user_id, username, action, timestamp FROM activity_logs WHERE user_id = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(new ActivityLog(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("action"),
                            rs.getString("timestamp")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to retrieve user logs: " + e.getMessage());
        }
        return logs;
    }


}
