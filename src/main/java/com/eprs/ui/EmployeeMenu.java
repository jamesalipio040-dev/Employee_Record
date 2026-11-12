package com.eprs.ui;

import com.eprs.dao.ActivityLogDao;
import com.eprs.dao.PayrollDao;
import com.eprs.model.Payroll;
import com.eprs.model.User;

import java.util.List;
import java.util.Scanner;

public class EmployeeMenu {
    private final Scanner scanner;
    private final PayrollDao payrollDao = new PayrollDao();
    private final ActivityLogDao logDao = new ActivityLogDao();
    private User employee;

    public EmployeeMenu (Scanner scanner){
        this.scanner = scanner;
    }

    public void display(User Employee) {
        this.employee = Employee;
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n============================");
            System.out.println("==== EMPLOYEE DASHBOARD ====");
            System.out.println("============================");
            System.out.println("1. View My Payroll");
            System.out.println("2. Logout");
            System.out.print("Select: ");
            int select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1:
                    viewMyPayroll();
                    break;
                case 2:
                    loggedIn = false;
                    logDao.logActivity(Employee.id(), Employee.username(), "USER_LOGOUT");
                    break;
            }
        }
    }

    private void viewMyPayroll(){
        System.out.print("Enter Month to View (e.g., January): ");
        String targetMonth = scanner.nextLine().trim();

        List<Payroll> myPayroll = payrollDao.getMyPayrollByMonth(employee.username(), targetMonth);

        System.out.println("\n=======================================");
        System.out.println("    -> PAYROLL FOR " + targetMonth.toUpperCase() + " <-    ");
        System.out.println("=======================================");

        if (myPayroll == null || myPayroll.isEmpty()){
            System.out.println("[ERROR] No Payroll Found for " + employee.username() + " in " + targetMonth);
            return;
        }

        for (Payroll p : myPayroll) {
            System.out.println("=======================================");
            System.out.println("Payroll ID    : " + p.p_id());
            System.out.println("Month         : " + p.month());
            System.out.println("Status        : " + p.p_status());
            System.out.println("=======================================");
            System.out.println("Hourly Rate   : " + p.h_rate());
            System.out.println("Total Hours   : " + p.total_hours());
            System.out.println("Overtime      : " + p.overtime());
            System.out.println("Basic Pay     : " + p.basic_pay());
            System.out.println("Late (mins)   : " + p.late());
            System.out.println("Days Absent   : " + p.absent());
            System.out.println("=======================================");
            System.out.println("SSS           : " + p.sss());
            System.out.println("Pag-IBIG      : " + p.pag_ibig());
            System.out.println("PhilHealth    : " + p.philhealth());
            System.out.println("Tax           : " + p.tax());
            System.out.println("Total Deductions: " + p.total_deductions());
            System.out.println("=======================================");
            System.out.println("Gross Salary  : " + p.gross_salary());
            System.out.println("NET PAY       : " + p.net_pay());
            System.out.println("=======================================\n");
        }
    }
}