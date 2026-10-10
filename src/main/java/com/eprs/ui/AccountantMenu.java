package com.eprs.ui;
import  java.util.Scanner;

public class AccountantMenu {
    public static final Scanner sc = new Scanner(System.in);
    boolean running = true;

    public void accountantDashbaord(String name, String role) {
        while (running) {
            System.out.println("\n============================");
            System.out.println("==== ACCOUNTANT DASHBOARD ====");
            System.out.println("============================");
            System.out.println("Welcome, "+ name + "!" + " Role: " + role);
            System.out.println("1. Add Payroll Entry");
            System.out.println("2. View Payroll Entries");
            System.out.println("3. Update Payroll ");
            System.out.println("4. Delete Payroll");
            System.out.println("5. View Account Details");
            System.out.println("6. Logout");
            System.out.print("Select: ");
            int select = sc.nextInt();

            switch (select) {
                case 1:
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
                    running = false;
                    break;
            }
        }
    }
}
