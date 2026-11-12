package com.eprs.ui;

import com.eprs.calculator.PayrollCalculator;
import com.eprs.dao.ActivityLogDao;
import com.eprs.dao.PayrollDao;
import com.eprs.model.Payroll;
import com.eprs.model.User;

import java.util.List;
import java.util.Scanner;

public class AccountantMenu {
    private final Scanner scanner;
    private final ActivityLogDao logDao = new ActivityLogDao();
    private final PayrollDao payrollDao = new PayrollDao();
    private User accountant;

    public AccountantMenu( Scanner scanner) {
        this.scanner = scanner;
    }

    public void display(User accountant) {
        this.accountant = accountant;
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("==============================");
            System.out.println("==== ACCOUNTANT DASHBOARD ====");
            System.out.println("==============================");
            System.out.println("1. Add Payroll Entry");
            System.out.println("2. View Payroll Entries");
            System.out.println("3. Filter Payroll");
            System.out.println("4. Update Payroll ");
            System.out.println("5. Delete Payroll");
            System.out.println("6.  View Employees");
            System.out.println("7. Logout");

            int select = readInt("Select: ");

            switch (select) {
                case 1:
                    addPayroll();
                    break;
                case 2:
                    viewPayroll();
                    break;
                case 3:
                    String targetMonth = readText("Enter Month to Filter Payroll: ");
                    displayPayrolltable(payrollDao.getPayrollByMonth(targetMonth), "Payroll for the Month of " +  targetMonth);
                    break;
                case 4:
                    updatePayroll();
                    break;
                case 5:
                    deletePayroll();
                    break;
                case 6:
                    viewEmployees();
                    break;
                case 7: {
                    logDao.logActivity(accountant.id(), accountant.username(), "USER_LOGOUT");
                    loggedIn = false;
                }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addPayroll() {

        System.out.println("\n--- ADD PAYROLL ENTRY ---");
        List<User> Employee = payrollDao.getEmployee();
        displayEmployeetable(Employee, "LIST OF EMPLOYEES");

        String empName;
        while (true) {
            empName = readText("Employee name: ");
            if (empName.matches("\\d+")) {
                System.out.println("[ERROR] Employee name cannot be just numbers. Please enter a valid name.");
                continue;
            }
            break;
        }

        String month = readText("Month: ");
        int hRate = readInt("Hourly rate: ");
        int totalHours = readInt("Total hours worked: ");
        int overtime = readInt("Overtime Worked: ");
        int late = readInt("Late: ");
        int absent = readInt("Days absent: ");

        if (hRate < 0 || totalHours < 0 || late < 0 || absent < 0) {
            System.out.println("[ERROR] Values cannot be negative.");
            return;
        }

        Payroll payroll = PayrollCalculator.compute(
                0, empName, month, hRate, totalHours, overtime, late, absent);

        System.out.println("\n--- PAYROLL PREVIEW ---");
        System.out.println(payroll);

        String confirm = readText("\nSave this entry? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }

        if (payrollDao.addPayroll(payroll)) {
            logDao.logActivity(accountant.id(), accountant.username(), "ACCOUNTANT created payroll entry for employee: " + empName);
            System.out.println("Payroll entry saved.");
        } else {
            System.out.println("[ERROR] Could not save payroll entry.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }



    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field is required.");
        }
    }

    private void displayEmployeetable(List<User> users, String title) {
        System.out.println("===================================" + title + "===================================");
        if (users.isEmpty()) {
            System.out.println("-> No Employee's Found");
            return;
        }
        String border = "+-----+-----------------+---------------------------+------------+";
        System.out.println(border);
        System.out.printf("| %-3s | %-15s | %-25s | %-10s |\n", "ID", "Username", "Role", "Status");
        System.out.println(border);
        for (User u : users) {
            System.out.printf("| %-3s | %-15s | %-25s | %-10s |\n",
                    u.id(), u.username(), u.roles(), u.status());
        }
        System.out.println(border);
    }

    private void viewEmployees() {
        displayEmployeetable(payrollDao.getEmployee(), "EMPLOYEES");
    }

    private void displayPayrolltable(List<Payroll> payrolls, String title) {
        System.out.println("=========================================================================================================================================================================");
        System.out.println("                                                                         " + title);
        System.out.println("=========================================================================================================================================================================");
        if (payrolls.isEmpty()) {
            System.out.println("-> No Payrolls Found");
            return;
        }

        String border = "+-----+-----------------+---------+-------+-------+----------+-----------+------+--------+---------+----------+------------+----------+-------------------+------------+------------+";
        System.out.println(border);
        System.out.printf("| %-3s | %-15s | %-7s | %-5s | %-5s | %-8s | %-9s | %-4s | %-6s | %-7s | %-8s | %-10s | %-8s | %-17s | %-10s | %-10s |\n",
                "ID", "Employee", "Month", "Rate", "Hours", "Overtime", "Basic Pay", "Late", "Absent", "SSS", "Pag-IBIG", "PhilHealth", "Tax", "Gross", "Net Pay", "Status");
        System.out.println(border);

        for (Payroll p : payrolls) {
            System.out.printf("| %-3d | %-15s | %-7s | %-5d | %-5d | %-8.1f | %-9.2f | %-4d | %-6d | %-7.2f | %-8.2f | %-10.2f | %-8.2f | %-17.2f | %-10.2f | %-10s |\n",
                    p.p_id(), p.emp_name(), p.month(), p.h_rate(), p.total_hours(), p.overtime(), (double) p.basic_pay(),
                    p.late(), p.absent(), p.sss(), p.pag_ibig(), p.philhealth(), p.tax(), p.gross_salary(), p.net_pay(), p.p_status());
        }
        System.out.println(border);
    }

    private void viewPayroll() {
        displayPayrolltable(payrollDao.getAllPayroll(), "PAYROLL ENTRIES");
    }

    private void updatePayroll() {
        List<Payroll> payrolls = payrollDao.getAllPayroll();
        displayPayrolltable(payrolls, "SELECT PAYROLL TO UPDATE");

        if (payrolls.isEmpty()) {
            return;
        }

        String targetName = readText("Enter Employee Name to Update: ");
        Payroll existing = payrolls.stream().filter(p -> p.emp_name().equalsIgnoreCase(targetName))
                .findFirst().orElse(null);

        if (existing == null) {
            System.out.println("[ERROR] Payroll entry for Employee " + targetName + " not Found.");
        }

        System.out.println("Updating record for: " + existing.emp_name() + "(" + existing.month() + ")");
        int hRate = readInt("New Hourly rate [Current Input: " + existing.h_rate() + "]: ");
        int totalHours = readInt("New Total hours worked [Current Input: " + existing.total_hours() + "]: ");
        int overtime = readInt("New Overtime Worked [Current Input: " + existing.overtime() + "]: ");
        int late = readInt("New Late [Current Input: " + existing.late() + "]: ");
        int absent = readInt("New Days absent [Current Input: " + existing.absent() + "]: ");

        if (hRate < 0 || totalHours < 0 || late < 0 || absent < 0 || overtime < 0) {
            System.out.println("[ERROR] Values cannot be negative.");
            return;
        }

        Payroll updatedPayroll = PayrollCalculator.compute(
                existing.p_id(), existing.emp_name(), existing.month(), hRate, totalHours, overtime, late, absent);

        System.out.println("\n--- UPDATED PAYROLL PREVIEW ---");
        System.out.println(updatedPayroll);

        String confirm = readText("\nSave changes? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }
        boolean success = payrollDao.updatePayroll(
                updatedPayroll.p_id(),
                updatedPayroll.emp_name(),
                updatedPayroll.month(),
                updatedPayroll.h_rate(),
                updatedPayroll.total_hours(),
                updatedPayroll.overtime(),
                updatedPayroll.basic_pay(),
                updatedPayroll.late(),
                updatedPayroll.absent(),
                updatedPayroll.sss(),
                updatedPayroll.pag_ibig(),
                updatedPayroll.philhealth(),
                updatedPayroll.tax(),
                updatedPayroll.gross_salary(),
                updatedPayroll.total_deductions(),
                updatedPayroll.net_pay()
        );
        if (success) {
            logDao.logActivity(accountant.id(), accountant.username(), "ACCOUNTANT Updated Payroll for Employee:  " + updatedPayroll.emp_name());
        } else {
            System.out.println("[ERROR] Could not Update Payroll Entry.");
        }

    }

    private void deletePayroll() {
        List<Payroll> payroll = payrollDao.getAllPayroll();
        displayPayrolltable(payroll, "SELECT PAYROLL TO DELETE");

        if (payroll.isEmpty()) {
            return;
        }

        int payrollId = readInt("Enter User ID to Delete (or 0 to Cancel): ");
        if (payrollId == 0) {
            System.out.println("-> Operation Cancelled.");
            return;
        }

        boolean exists = payroll.stream().anyMatch(payroll1 -> payroll1.p_id() == payrollId);
        if (!exists) {
            System.out.println("-> [ERROR] User ID not found.");
            return;
        }

        payrollDao.deletePayroll(payrollId);
        logDao.logActivity(accountant.id(), accountant.username(), "Admin deleted User ID " + payrollId);
        System.out.println("-> [SUCCESS] User ID " + payrollId + " deleted successfully.");
    }

}