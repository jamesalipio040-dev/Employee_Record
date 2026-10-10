package com.eprs.dao;

import com.eprs.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public boolean registerUser(String username, String password, String role){
        String sql = "INSERT INTO users(username, password, roles, status) VALUES(?, ?, ?, 'PENDING')";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role.toUpperCase());
            pstmt.executeUpdate();
            System.out.print("SUCCESSFUL REGISTRATION, waiting for approval.");
            return  true;
        }catch(SQLException e){
            System.err.print("FAILED: "+ e.getMessage());
            return false;
        }
    }

    public static User login (String username, String password){
        String sql = "SELECT id, username, password, roles, status FROM users Where username = ? AND password = ?";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("roles"),
                            rs.getString("status"));
                }
            }
        } catch (SQLException e){
            System.err.println("LOGIN FAILED: " + e.getMessage());
        }
        return null;
    }



}
