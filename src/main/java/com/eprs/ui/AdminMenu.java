package com.eprs.ui;
import java.util.Scanner;

public class AdminMenu {
    public static final Scanner sc = new Scanner(System.in);

    public void adminDashboard(String name, String role) {
        boolean running = true;

        while (running) {
            System.out.println("\n============================");
            System.out.println("====== ADMIN DASHBOARD =====");
            System.out.println("============================");
            System.out.println("Welcome, "+ name + "!" + " Role: " + role);
            System.out.println("1. Add User");
            System.out.println("2. View User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
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