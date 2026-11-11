package com.eprs.dao;

import com.eprs.model.Payroll;
import com.eprs.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDao {

    public boolean addPayroll(Payroll p) {
        String sql = """
                INSERT INTO payroll (emp_name, month, h_rate, total_hours, basic_pay, late, absent,
                sss, pag_ibig, philhealth, tax, gross_salary, total_deductions, net_pay)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.emp_name());
            pstmt.setString(2, p.month());
            pstmt.setInt(3, p.h_rate());
            pstmt.setInt(4, p.total_hours());
            pstmt.setInt(5, p.basic_pay());
            pstmt.setInt(6, p.late());
            pstmt.setInt(7, p.absent());
            pstmt.setDouble(8, p.sss());
            pstmt.setDouble(9, p.pag_ibig());
            pstmt.setDouble(10, p.philhealth());
            pstmt.setDouble(11, p.tax());
            pstmt.setDouble(12, p.gross_salary());
            pstmt.setDouble(13, p.total_deductions());
            pstmt.setDouble(14, p.net_pay());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add Payroll. " + e.getMessage());
            return false;
        }
    }

    public List<Payroll> getAllPayroll() {
        List<Payroll> payrollList = new ArrayList<>();
        String sql = "SELECT id, emp_name, month, h_rate, total_hours, basic_pay, late, absent, sss, pag_ibig, philhealth, tax, gross_salary, total_deductions, net_pay FROM payroll";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payrollList.add(new Payroll(
                        rs.getInt("id"),
                        rs.getString("emp_name"),
                        rs.getString("month"),
                        rs.getInt("h_rate"),
                        rs.getInt("total_hours"),
                        rs.getInt("basic_pay"),
                        rs.getInt("late"),
                        rs.getInt("absent"),
                        rs.getDouble("sss"),
                        rs.getDouble("pag_ibig"),
                        rs.getDouble("philhealth"),
                        rs.getDouble("tax"),
                        rs.getDouble("gross_salary"),
                        rs.getDouble("total_deductions"),
                        rs.getDouble("net_pay")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to retrieve payroll list: " + e.getMessage());
        }
        return payrollList;
    }

    public List<User> getEmployee(){
        List<User> EmployeeList = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE roles = 'EMPLOYEE'";

        try(Connection conn = DatabaseConnection.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                EmployeeList.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("roles"),
                        rs.getString("status")
                ));
            }
        }catch (SQLException e){
            System.out.println("Error Fetching User: "+e.getMessage());
        }
        return EmployeeList;
    }

}