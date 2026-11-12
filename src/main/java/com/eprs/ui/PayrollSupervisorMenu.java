package com.eprs.ui;
import com.eprs.dao.ActivityLogDao;
import com.eprs.dao.PayrollDao;
import com.eprs.dao.UserDao;
import com.eprs.model.Payroll;
import com.eprs.model.User;

import java.util.List;
import java.util.Scanner;

public class PayrollSupervisorMenu {
   private  final  Scanner scanner;
   private final UserDao userDao = new UserDao();
    private final ActivityLogDao logDao = new ActivityLogDao();
    private final PayrollDao payrollDao = new PayrollDao();
    private  User PayrollSuperVisor;

   public PayrollSupervisorMenu(Scanner scanner){
       this.scanner = scanner;
   }

    public void display (User PayrollSupervisor) {
       this.PayrollSuperVisor = PayrollSupervisor;
       boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n==============================");
            System.out.println(" PAYROLL SUPER VISOR DASHBOARD ");
            System.out.println("==============================");
            System.out.println("1. View Payroll Entries");
            System.out.println("2. Filter Payroll");
            System.out.println("3. Approve Payroll ");
            System.out.println("4. Reject Payroll");
            System.out.println("5. Logout");
            System.out.print("Select: ");
            int select = scanner.nextInt();
            scanner.nextLine();


            switch (select) {
                case 1:
                    viewPayroll();
                    break;
                case 2:
                    System.out.println("1. View Payroll by Month");
                    System.out.println("2. View Pending Payroll");
                    System.out.println("3. View Approved Payrolls");
                    System.out.println("4. View Rejected Payrolls");
                    System.out.print("Choose: ");
                    int choose = scanner.nextInt();

                    switch (choose){
                     case 1:
                             String tMonth = readText("Enter Month to Filter Payroll: ");
                             displayPayrolltable(payrollDao.getPayrollByMonth(tMonth), "Payroll for the Month of " + tMonth);
                             break;
                             case 2:
                                 viewPendingPayroll();
                                 break;
                             case 3:
                                 viewAppovedPayroll();
                                 break;
                             case 4:
                                 viewRejectedPayroll();
                                 break;
                    }
                    break;
                case 3:
                    approvePayroll();
                    break;
                case 4:
                    rejectPayroll();
                    break;
                case 5:
                    loggedIn = false;
                    logDao.logActivity(PayrollSupervisor.id(), PayrollSupervisor.username(), "USER_LOGOUT");
                    break;
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
    private void viewPendingPayroll(){
       displayPayrolltable(payrollDao.getPayrollByPending("PENDING"),"PENDING PAYROLL ENTRIES");
    }
    private void viewAppovedPayroll(){
        displayPayrolltable(payrollDao.getApprovedPayroll("APPROVED"),"PENDING PAYROLL ENTRIES");
    }private void viewRejectedPayroll(){
        displayPayrolltable(payrollDao.getRejectedPayroll("REJECTED"),"PENDING PAYROLL ENTRIES");
    }


    private void approvePayroll(){
        List<Payroll> pendingPayroll = payrollDao.getPayrollByStatus("PENDING");
        displayPayrolltable(pendingPayroll, "PENDING APPROVALS");

        if (pendingPayroll.isEmpty()){
            return;
        }
        System.out.print("Enter Payroll ID to Approve (or 0 to cancel): ");
        int PayrollID = scanner.nextInt();
        scanner.nextLine();

        if (PayrollID == 0){
            System.out.println("-> Operation Cancelled by the User");
            return;
        }
        boolean exists = pendingPayroll.stream().anyMatch(payroll -> payroll.p_id() == PayrollID);
        if (!exists){
            System.out.println("[ERROR] Payroll ID not found.");
            return;
        }

        payrollDao.approvePayrollEntry(PayrollID, "APPROVED");

        logDao.logActivity(PayrollSuperVisor.id(), PayrollSuperVisor.username(), "Payroll Supervisor approved Payroll Entry " + PayrollID);
        System.out.println("-> [SUCCESS] Payroll ID " + PayrollID + " approved successfully.");
    }

    private void rejectPayroll(){
        List<Payroll> pendingPayroll = payrollDao.getPayrollByStatus("PENDING");
        displayPayrolltable(pendingPayroll, "PENDING APPROVALS");

        if (pendingPayroll.isEmpty()){
            return;
        }
        System.out.print("Enter Payroll ID to Reject (or 0 to cancel): ");
        int PayrollID = scanner.nextInt();
        scanner.nextLine();

        if (PayrollID == 0){
            System.out.println("-> Operation Cancelled by the User");
            return;
        }
        boolean exists = pendingPayroll.stream().anyMatch(payroll -> payroll.p_id() == PayrollID);
        if (!exists){
            System.out.println("[ERROR] Payroll ID not found.");
            return;
        }

        payrollDao.rejectPayrollEntry(PayrollID, "REJECTED");

        logDao.logActivity(PayrollSuperVisor.id(), PayrollSuperVisor.username(), "Payroll Supervisor Rejected Payroll Entry " + PayrollID);
        System.out.println("-> [SUCCESS] Payroll ID " + PayrollID + " Rejected successfully.");
    }

}
