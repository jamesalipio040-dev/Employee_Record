package com.eprs.dao;

import com.eprs.model.Payroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}