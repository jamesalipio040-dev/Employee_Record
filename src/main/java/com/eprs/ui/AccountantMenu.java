package com.eprs.ui;

import com.eprs.calculator.PayrollCalculator;
import com.eprs.dao.PayrollDao;
import com.eprs.model.Payroll;
import com.eprs.model.User;

import java.util.Scanner;

public class AccountantMenu {
    private final Scanner scanner;
    private final PayrollDao payrollDao = new PayrollDao();

    public AccountantMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void display(User accountant) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n============================");
            System.out.println("==== ACCOUNTANT DASHBOARD ====");
            System.out.println("============================");
            System.out.println("1. Add Payroll Entry");
            System.out.println("2. View Payroll Entries");
            System.out.println("3. Update Payroll ");
            System.out.println("4. Delete Payroll");
            System.out.println("5. View Account Details");
            System.out.println("6. Logout");

            int select = readInt("Select: ");

            switch (select) {
                case 1:
                    addPayroll();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addPayroll() {
        System.out.println("\n--- ADD PAYROLL ENTRY ---");

        String empName = readText("Employee name: ");
        String month = readText("Month: ");
        int hRate = readInt("Hourly rate: ");
        int totalHours = readInt("Total hours worked: ");
        int late = readInt("Minutes late: ");
        int absent = readInt("Days absent: ");

        if (hRate < 0 || totalHours < 0 || late < 0 || absent < 0) {
            System.out.println("[ERROR] Values cannot be negative.");
            return;
        }

        Payroll payroll = PayrollCalculator.compute(
                0, empName, month, hRate, totalHours, late, absent);

        System.out.println("\n--- PAYROLL PREVIEW ---");
        System.out.println(payroll);

        String confirm = readText("\nSave this entry? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }

        if (payrollDao.addPayroll(payroll)) {
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
}