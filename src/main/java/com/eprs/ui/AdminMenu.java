package com.eprs.ui;

import com.eprs.dao.ActivityLogDao;
import com.eprs.dao.UserDao;
import com.eprs.model.ActivityLog;
import com.eprs.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final ActivityLogDao logDao;
    private final Scanner scanner;

    public AdminMenu(UserDao userDao, ActivityLogDao logDao, Scanner scanner) {
        this.userDao = userDao;
        this.logDao = logDao;
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
            System.out.println("7. View System Audit Logs");
            System.out.println("8. Logout");
            System.out.print("Select: ");

            int select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1 -> viewAllUser();
                case 2 -> addUser(admin);
                case 3 -> viewPendingUser();
                case 4 -> approveUser(admin);
                case 5 -> updateUser(admin);
                case 6 -> deleteUser(admin);
                case 7 -> viewAuditLogsMenu(); // Handle audit logs submenu
                case 8 -> {
                    logDao.logActivity(admin.id(), admin.username(), "USER_LOGOUT");
                    loggedIn = false;
                }
                default -> System.out.println("-> [ERROR] Invalid Option.");
            }
        }
    }

    private void viewAuditLogsMenu() {
        boolean viewing = true;
        while (viewing) {
            System.out.println("\n========================================");
            System.out.println("========= SYSTEM AUDIT LOGS ============");
            System.out.println("========================================");
            System.out.println("1. View All System Logs");
            System.out.println("2. Filter Logs by User ID");
            System.out.println("3. Back to Admin Dashboard");
            System.out.print("Select Option: ");

            int choice = readIntInput("");
            switch (choice) {
                case 1 -> displayLogTable(logDao.getAllLogs(), "ALL SYSTEM AUDIT LOGS");
                case 2 -> {
                    int targetUserId = readIntInput("Enter User ID to filter: ");
                    displayLogTable(logDao.getLogsByUserId(targetUserId), "LOGS FOR USER ID: " + targetUserId);
                }
                case 3 -> viewing = false;
                default -> System.out.println("[ERROR] Invalid option.");
            }
        }
    }

    private void displayLogTable(List<ActivityLog> logs, String title) {
        System.out.println("\n==================================================================================================");
        System.out.println("                                   " + title);
        System.out.println("==================================================================================================");
        if (logs.isEmpty()) {
            System.out.println("-> No Audit Logs Found.");
            System.out.println("==================================================================================================");
            return;
        }

        String border = "+-------+----------+--------------------+------------------------------------------+----------------------+";
        System.out.println(border);
        System.out.printf("| %-5s | %-8s | %-18s | %-40s | %-20s |\n", "ID", "User ID", "Username", "Action", "Timestamp");
        System.out.println(border);

        for (ActivityLog log : logs) {
            System.out.printf("| %-5s | %-8s | %-18s | %-40s | %-20s |\n",
                    log.id(), log.user_id(), log.username(), log.action(), log.timestamp());
        }
        System.out.println(border);
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

    private void approveUser(User currentAdmin){
        List<User> pendingUsers = userDao.getPendingUser();
        displayUserTable(pendingUsers, "PENDING APPROVALS");

        if (pendingUsers.isEmpty()){
            return;
        }
        System.out.print("Enter User ID to Approve (or 0 to cancel): ");
        int userID = scanner.nextInt();
        scanner.nextLine();

        if (userID == 0){
            System.out.println("-> Operation Cancelled by the User");
            return;
        }
        boolean exists = pendingUsers.stream().anyMatch(u -> u.id() == userID);
        if (!exists){
            System.out.println("[ERROR] User ID not found.");
            return;
        }

        userDao.approveUser(userID);

        logDao.logActivity(currentAdmin.id(), currentAdmin.username(), "Admin approved account for User ID " + userID);
        System.out.println("-> [SUCCESS] User ID " + userID + " approved successfully.");
    }

    private void updateUser(User currentAdmin){
        List<User> users = userDao.getAllUsers();
        displayUserTable(users, "SELECT USER TO UPDATE");

        if (users.isEmpty()){
            return;
        }

        int userId = readIntInput("Enter Selected ID to Update (or 0 to Cancel): ");
        if(userId == 0){
            System.out.println(" -> Operation Cancelled");
            return;
        }

        if (userId == currentAdmin.id()){
            System.out.println(" -> [WARNING] Modifying current Active Admin is Restricted");
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

        userDao.updateUserAndStatus(userId, newRole, newStatus);

        // Automated Logging for Role/Status Updates
        logDao.logActivity(currentAdmin.id(), currentAdmin.username(), "Admin Changed role of User " + userId + " to " + newRole);
        System.out.println("-> [SUCCESS] User ID " + userId + " updated successfully.");
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
            System.out.print("Enter Status (APPROVED / PENDING): ");
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
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String input = scanner.nextLine().trim();
            try{
                return Integer.parseInt(input);
            }catch (NumberFormatException e){
                System.out.println("[ERROR] Please enter a valid numeric input.");
            }
        }
    }

    private void addUser(User currentAdmin) {
        System.out.println("========== ADD NEW USER ===========");
        System.out.print("Enter Username (or type 'cancel' to abort): ");
        String username = scanner.nextLine().trim();
        if (username.equalsIgnoreCase("cancel") || username.isEmpty()) {
            System.out.println("-> Operation Cancelled.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("-> [ERROR] Password cannot be empty. Operation Cancelled.");
            return;
        }

        String role = readValidRole();
        if (role == null) {
            System.out.println("-> Operation Cancelled.");
            return;
        }

        userDao.registerUser(username, password, role);
        logDao.logActivity(currentAdmin.id(), currentAdmin.username(), "Admin created new user: " + username );
        System.out.println("-> [SUCCESS] User '" + username + "' added successfully.");
    }

    private void deleteUser(User currentAdmin) {
        List<User> users = userDao.getAllUsers();
        displayUserTable(users, "SELECT USER TO DELETE");

        if (users.isEmpty()) {
            return;
        }

        int userId = readIntInput("Enter User ID to Delete (or 0 to Cancel): ");
        if (userId == 0) {
            System.out.println("-> Operation Cancelled.");
            return;
        }

        if (userId == currentAdmin.id()) {
            System.out.println("-> [WARNING] You cannot delete your own currently active Admin account!");
            return;
        }

        boolean exists = users.stream().anyMatch(u -> u.id() == userId);
        if (!exists) {
            System.out.println("-> [ERROR] User ID not found.");
            return;
        }

        userDao.deleteUser(userId);
        logDao.logActivity(currentAdmin.id(), currentAdmin.username(), "Admin deleted User ID " + userId);
        System.out.println("-> [SUCCESS] User ID " + userId + " deleted successfully.");
    }

}