package com.eprs.ui;

import com.eprs.dao.ActivityLogDao;
import com.eprs.dao.UserDao;
import com.eprs.model.User;

import java.util.Scanner;

public class Authentication {

    private final UserDao userDao;
    private final ActivityLogDao logDao;
    private final Scanner sc;

    public Authentication(UserDao userDao, ActivityLogDao logDao, Scanner scanner){
        this.userDao = userDao;
        this.logDao = logDao;
        this.sc = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("====================================");
            System.out.println("Employee Payroll Record System");
            System.out.println("====================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an Option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
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
                            break;
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
                    running = false;
                    System.out.printf("Exiting Program. THANK YOU FOR USING THE SYSTEM!");
                    break;
            }
        }
    }
    private void handleLogin(){
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        UserDao userDao = new UserDao();

        User user = userDao.login(username, password);

        if (user != null){
            logDao.logActivity(user.id(), user.username(), "USER_LOGIN");
            System.out.println(" > Welcome " + user.username() + " | ROLE: " + user.roles());

            switch (user.roles()){
                case "ADMIN" -> new AdminMenu(userDao, logDao, sc).display(user);
                case "ACCOUNTANT" -> new AccountantMenu(sc).display(user);
                case "PAYROLL SUPERVISOR" -> new PayrollSupervisorMenu(sc).display(user);
                case "EMPLOYEE" -> new EmployeeMenu(sc).display(user);
                default -> System.out.println("Account roles not found!");
            }
        }
    }
}
