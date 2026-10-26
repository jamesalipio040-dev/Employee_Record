package com.eprs.ui;

import com.eprs.dao.UserDao;
import com.eprs.model.User;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public AdminMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void display(User admin) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("============================");
            System.out.println("====== ADMIN DASHBOARD =====");
            System.out.println("============================");
            System.out.println("1. View All User");
            System.out.println("2. Add User");
            System.out.println("3. View Pending User Approvals");
            System.out.println("4. Approve User Registration");
            System.out.println("5. Update User role / Status");
            System.out.println("6. Delete User");
            System.out.println("7. Logout");
            System.out.print("Select: ");

            int select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1 -> viewAllUser();
                //case 2
                case  3 -> viewPendingUser();
                case 4 -> approveUser();
                case 5  -> updateUser(admin);
                case 7 -> loggedIn = false;
                default -> System.out.println("-> [ERROR] Invalid Option.");
            }
        }
    }

    private void displayUserTable(List<User> users, String title) {
        System.out.println("===================================" + title + "===================================");
        if (users.isEmpty()) {
            System.out.println("-> No Records Found");
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

    private void viewAllUser() {
        displayUserTable(userDao.getAllUsers(), "ALL REGISTERED USER");
    }

    private void viewPendingUser(){
        displayUserTable(userDao.getPendingUser(), "PENDING APPROVALS");
    }

    private void approveUser(){
        List<User> pendingUsers = userDao.getPendingUser();
        displayUserTable(pendingUsers, "PENDING APPROVALS");

        if (pendingUsers.isEmpty()){
            return;
        }
         System.out.print("Enter User ID to Approve (or 0 to cancel): ");
                int userID = scanner.nextInt();
        if (userID == 0){
            System.out.println("-> Operation Cancelled by the User");
        }
        boolean exits = pendingUsers.stream().anyMatch(u -> u.id() ==userID );
        if (!exits){
            System.out.println("[ERROR] User ID not found.");
            return;
        }
        userDao.approveUser(userID);
    }

    private void updateUser(User currentAdmin){
        List<User> users = userDao.getPendingUser();
        displayUserTable(users, "SELECT USER TO UPDATE");

        if (users.isEmpty()){
            return;
        }


        int userId = readIntInput("Enter Selected ID to Update (or 0 to Cancel): ");
        if(userId ==0){
            System.out.println(" -> Operation Cancelled");
            return;
        }

        if (userId == currentAdmin.id()){
            System.out.println(" -> [WARNING] Modifying current Active is Restricted");
        }
       String newRole = readValidRole();
        if(newRole == null){
            System.out.println(" >> Operation Cancelled <<");
            return;
        }

        String newStatus = readValidStatus();
        if(newStatus == null){
            System.out.println(" >> Operation Cancelled. <<");
            return;
        }

        userDao.updateUserAndStatus(userId,newRole, newStatus);
    }

    private String readValidRole(){
        while (true){
            System.out.print("Enter Role (ADMIN/ACCOUNTANT/PAYROLL SUPERVISOR/EMPLOYEE): ");
            String role = scanner.nextLine().trim().toUpperCase();
            if(role.equals("0") || role.equals("CANCEL")){
                return null;
            }
            if (role.equals("EMPLOYEE") || role.equals("PAYROLL SUPERVISOR") || role.equals("ACCOUNTANT") || role.equals("ADMIN")){
                return role;
            }
            System.out.println("[ERROR] Invalid Role. ");
        }
    }

    private String readValidStatus(){
        while (true) {
            System.out.print("Enter Status (APPROVED / CANCELLED): ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("0") || status.equals("CANCEL")) {
                return null;
            }
            if (status.equals("APPROVED") || status.equals("PENDING")) {
                return status;
            }
            System.out.println("[ERROR] Invalid Status. Must be APPROVED or PENDING. ");
        }
      }

    private int readIntInput(String prompt){
        while (true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            try{
                return Integer.parseInt(input);
            }catch (NumberFormatException e){
                System.out.println("[ERROR] Please enter a valid numeric ID. ");
            }
        }
    }
}