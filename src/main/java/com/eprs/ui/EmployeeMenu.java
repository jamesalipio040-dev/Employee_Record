package com.eprs.ui;
import  java.util.Scanner;

public class EmployeeMenu {
    public static final Scanner sc = new Scanner(System.in);
    boolean running = true;

    public void employeeDashbaord(String name, String role) {
        while (running) {
            System.out.println("\n============================");
            System.out.println("==== EMPLOYEE DASHBOARD ====");
            System.out.println("============================");
            System.out.println("Welcome, "+ name + "!" + " Role: " + role);
            System.out.println("1. view my Payroll");
            System.out.println("2. view Account details");
            System.out.println("3. Logout");
            System.out.print("Select: ");
            int select = sc.nextInt();

            switch (select) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    running = false;
                    break;
            }
        }
    }
}
