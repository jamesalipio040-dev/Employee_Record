package com.eprs.ui;
import com.eprs.model.User;

import  java.util.Scanner;

public class EmployeeMenu {
    public final Scanner scanner;

    public EmployeeMenu (Scanner scanner){
        this.scanner = scanner;
    }

    public void display(User Employee) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n============================");
            System.out.println("==== EMPLOYEE DASHBOARD ====");
            System.out.println("============================");
            System.out.println("1. view my Payroll");
            System.out.println("2. view Account details");
            System.out.println("3. Logout");
            System.out.print("Select: ");
            int select = scanner.nextInt();

            switch (select) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    loggedIn = false;
                    break;
            }
        }
    }
}
