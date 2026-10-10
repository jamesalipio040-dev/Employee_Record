package com.eprs;

import com.eprs.dao.UserDao;
import com.eprs.model.User;
import com.eprs.ui.AccountantMenu;
import com.eprs.ui.AdminMenu;
import com.eprs.ui.EmployeeMenu;
import com.eprs.ui.PayrollSupervisorMenu;


import java.util.Scanner;

public class Main {
    public static final Scanner sc = new Scanner(System.in);
    public static final UserDao userDao = new UserDao();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n====================================");
            System.out.println("Employee Payroll Record System");
            System.out.println("====================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an Option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Username: ");
                    String username = sc.nextLine();
                    System.out.print("Password: ");
                    String password = sc.nextLine();

                    User user = UserDao.login(username, password);

                    if (user == null) {
                        System.out.println("Account does not exist or Account does not approve. \nRequest Admin's Approval to succesfully login.");
                    } else if (!"Approved".equalsIgnoreCase(user.status())) {
                        System.out.println("your Account Status is: " + user.status() + "!" + " \nPlease wait for Admin's Approval");
                    } else {

                        switch (user.roles()) {
                            case "ADMIN":
                                AdminMenu admin = new AdminMenu();
                                admin.adminDashboard(user.username(), user.roles());
                                break;
                            case "ACCOUNTANT":
                                AccountantMenu accountant = new AccountantMenu();
                                accountant.accountantDashbaord(user.username(), user.roles());
                                break;
                            case "PAYROLL SUPER VISOR":
                                PayrollSupervisorMenu psvMenu = new PayrollSupervisorMenu();
                                psvMenu.payrollsupervisorMenu(user.username(), user.roles());
                                break;
                            case "EMPLOYEE":
                                EmployeeMenu employeeMenu = new EmployeeMenu();
                                employeeMenu.employeeDashbaord(user.username(), user.roles());
                                break;
                        }
                    }
                    break;
                case "2":
                    System.out.print("Enter new Username: ");
                    String newUser = sc.nextLine().trim();
                    System.out.print("Enter Password: ");
                    String newPass = sc.nextLine();

                    System.out.print("Select Role: [1] Admin [2] Accountant [3] Payroll Supervisor [4] Employee: ");
                    String roleChoice = sc.nextLine().trim();
                    String role;

                    switch (roleChoice) {
                        case "1":
                            role = "Admin";
                            break;
                        case "2":
                            role = "Accountant";
                            break;
                        case "3":
                            role = "Payroll Supervisor";
                        case "4":
                            role = "Employee";
                            break;
                        default:
                            role = null;
                            break;
                    }
                    if (role != null) {
                        userDao.registerUser(newUser, newPass, role);
                    } else {
                        System.out.println("Invalid Role Selection!");
                    }
                    break;
                case "3":
                    System.out.printf("Exiting Program. THANK YOU FOR USING THE SYSTEM!");
                    break;
            }
        }
    }
}