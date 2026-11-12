package com.eprs.dao;

import com.eprs.model.Payroll;
import com.eprs.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDao {

    public boolean addPayroll(Payroll p) {
        String sql = """
                INSERT INTO payroll (emp_name, month, h_rate, total_hours, overtime, basic_pay, late, absent,
                sss, pag_ibig, philhealth, tax, gross_salary, total_deductions, net_pay, p_status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING')
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.emp_name());
            pstmt.setString(2, p.month());
            pstmt.setInt(3, p.h_rate());
            pstmt.setInt(4, p.total_hours());
            pstmt.setDouble(5, p.overtime());
            pstmt.setInt(6, p.basic_pay());
            pstmt.setInt(7, p.late());
            pstmt.setInt(8, p.absent());
            pstmt.setDouble(9, p.sss());
            pstmt.setDouble(10, p.pag_ibig());
            pstmt.setDouble(11, p.philhealth());
            pstmt.setDouble(12, p.tax());
            pstmt.setDouble(13, p.gross_salary());
            pstmt.setDouble(14, p.total_deductions());
            pstmt.setDouble(15, p.net_pay());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to add Payroll. " + e.getMessage());
            return false;
        }
    }

    public List<Payroll> getAllPayroll() {
        List<Payroll> payrollList = new ArrayList<>();
        String sql = "SELECT p_id, emp_name, month, h_rate, total_hours, overtime, basic_pay, late, absent, sss, pag_ibig, philhealth, tax, gross_salary, total_deductions, net_pay, p_status FROM payroll";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payrollList.add(new Payroll(
                        rs.getInt("p_id"),
                        rs.getString("emp_name"),
                        rs.getString("month"),
                        rs.getInt("h_rate"),
                        rs.getInt("total_hours"),
                        rs.getDouble("overtime"),
                        rs.getInt("basic_pay"),
                        rs.getInt("late"),
                        rs.getInt("absent"),
                        rs.getDouble("sss"),
                        rs.getDouble("pag_ibig"),
                        rs.getDouble("philhealth"),
                        rs.getDouble("tax"),
                        rs.getDouble("gross_salary"),
                        rs.getDouble("total_deductions"),
                        rs.getDouble("net_pay"),
                        rs.getString("p_status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to retrieve payroll list: " + e.getMessage());
        }
        return payrollList;
    }

    public List<Payroll> getPayrollByMonth(String month) {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT p_id, emp_name, month, h_rate, total_hours, overtime, " +
                "basic_pay, late, absent, sss, pag_ibig, philhealth, tax, gross_salary, total_deductions, net_pay, p_status " +
                "FROM payroll WHERE LOWER(month) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payrolls.add(new Payroll(
                            rs.getInt("p_id"),
                            rs.getString("emp_name"),
                            rs.getString("month"),
                            rs.getInt("h_rate"),
                            rs.getInt("total_hours"),
                            rs.getDouble("overtime"),
                            rs.getInt("basic_pay"),
                            rs.getInt("late"),
                            rs.getInt("absent"),
                            rs.getDouble("sss"),
                            rs.getDouble("pag_ibig"),
                            rs.getDouble("philhealth"),
                            rs.getDouble("tax"),
                            rs.getDouble("gross_salary"),
                            rs.getDouble("total_deductions"),
                            rs.getDouble("net_pay"),
                            rs.getString("p_status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to Retrieve Payroll logs. " + e.getMessage());
        }
        return payrolls;
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

    public boolean updatePayroll(
            int pId,
            String emp_name,
            String month,
            int newh_rate,
            int newtotal_hours,
            double newOvertime,
            int newbasic_pay,
            int newLate,
            int newAbsent,
            double newSSS,
            double newPagibig,
            double newPhilhealth,
            double newTax,
            double newGross_Salary,
            double newTotal_Deductions,
            double newNetpay
    ) {
        String sql = "UPDATE payroll SET emp_name = ?, month = ?, h_rate = ?, total_hours = ?, overtime = ?, basic_pay = ?, late = ?, " +
                "absent = ?, sss = ?, pag_ibig = ?, philhealth = ?, tax = ?, gross_salary = ?, total_deductions = ?, net_pay = ? WHERE p_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, emp_name);
            pstmt.setString(2, month);
            pstmt.setInt(3, newh_rate);
            pstmt.setInt(4, newtotal_hours);
            pstmt.setDouble(5, newOvertime);
            pstmt.setInt(6, newbasic_pay);
            pstmt.setInt(7, newLate);
            pstmt.setInt(8, newAbsent);
            pstmt.setDouble(9, newSSS);
            pstmt.setDouble(10, newPagibig);
            pstmt.setDouble(11, newPhilhealth);
            pstmt.setDouble(12, newTax);
            pstmt.setDouble(13, newGross_Salary);
            pstmt.setDouble(14, newTotal_Deductions);
            pstmt.setDouble(15, newNetpay);
            pstmt.setInt(16, pId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" -> Payroll ID " + pId + " UPDATED SUCCESSFULLY!");
                return true;
            } else {
                System.out.println(" -> Payroll ID not found.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to Update Payroll: " + e.getMessage());
            return false;
        }
    }

    public void deletePayroll(int p_id){
        String sql = "DELETE FROM payroll WHERE p_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, p_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete Payroll: " + e.getMessage());
        }
    }

    public List<Payroll> getPayrollByStatus(String status) {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT p_id, emp_name, month, h_rate, total_hours, overtime, " +
                "basic_pay, late, absent, sss, pag_ibig, philhealth, tax, " +
                "gross_salary, total_deductions, net_pay, p_status FROM payroll WHERE LOWER(p_status) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payrolls.add(new Payroll(
                            rs.getInt("p_id"),
                            rs.getString("emp_name"),
                            rs.getString("month"),
                            rs.getInt("h_rate"),
                            rs.getInt("total_hours"),
                            rs.getDouble("overtime"),
                            rs.getInt("basic_pay"),
                            rs.getInt("late"),
                            rs.getInt("absent"),
                            rs.getDouble("sss"),
                            rs.getDouble("pag_ibig"),
                            rs.getDouble("philhealth"),
                            rs.getDouble("tax"),
                            rs.getDouble("gross_salary"),
                            rs.getDouble("total_deductions"),
                            rs.getDouble("net_pay"),
                            rs.getString("p_status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to retrieve payrolls by status: " + e.getMessage());
        }
        return payrolls;
    }

    public List<Payroll> getPayrollByPending(String status) {
        return getPayrollByStatus("PENDING");
    }

    public List<Payroll> getApprovedPayroll(String status) {
        return getPayrollByStatus("APPROVED");
    }

    public List<Payroll> getRejectedPayroll(String status) {
        return getPayrollByStatus("REJECTED");
    }

    public boolean approvePayrollEntry(int p_id, String newStatus){
        String sql = "UPDATE payroll SET p_status = ? WHERE p_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, p_id);

            int rowsUpdated = pstmt.executeUpdate();

            if(rowsUpdated > 0 ){
                System.out.println(" -> Payroll ID: " + p_id + " APPROVED SUCCESSFULLY!");
                return true;
            } else {
                System.out.println("ID NOT FOUND.");
            }
        } catch (SQLException e){
            System.out.println("Error Updating Payroll: " + e.getMessage());
        }
        return false;
    }

    public boolean rejectPayrollEntry(int p_id, String newStatus){
        String sql = "UPDATE payroll SET p_status = ? WHERE p_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, p_id);

            int rowsUpdated = pstmt.executeUpdate();

            if(rowsUpdated > 0 ){
                System.out.println(" -> Payroll ID: " + p_id + " REJECTED SUCCESSFULLY!");
                return true;
            } else {
                System.out.println("ID NOT FOUND.");
            }
        } catch (SQLException e){
            System.out.println("Error Updating Payroll: " + e.getMessage());
        }
        return false;
    }

    public List<Payroll> getMyPayrollByMonth(String emp_name, String month) {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE LOWER(emp_name) = LOWER(?) AND LOWER(month) = LOWER(?) AND LOWER(p_status) = 'approved'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, emp_name);
            pstmt.setString(2, month);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payrolls.add(new Payroll(
                            rs.getInt("p_id"),
                            rs.getString("emp_name"),
                            rs.getString("month"),
                            rs.getInt("h_rate"),
                            rs.getInt("total_hours"),
                            rs.getDouble("overtime"),
                            rs.getInt("basic_pay"),
                            rs.getInt("late"),
                            rs.getInt("absent"),
                            rs.getDouble("sss"),
                            rs.getDouble("pag_ibig"),
                            rs.getDouble("philhealth"),
                            rs.getDouble("tax"),
                            rs.getDouble("gross_salary"),
                            rs.getDouble("total_deductions"),
                            rs.getDouble("net_pay"),
                            rs.getString("p_status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to Retrieve Payroll: " + e.getMessage());
        }
        return payrolls;
    }
}