package com.eprs.ui;
import com.eprs.model.User;

import java.util.Scanner;

public class PayrollSupervisorMenu {
   private  final  Scanner scanner;

   public PayrollSupervisorMenu(Scanner scanner){
       this.scanner = scanner;
   }

    public void display (User PayrollSupervisor) {
       boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n==============================");
            System.out.println(" PAYROLL SUPER VISOR DASHBOARD ");
            System.out.println("==============================");
            System.out.println("1. View Payroll Entries");
            System.out.println("2. Approve Payroll ");
            System.out.println("3. Reject Payroll");
            System.out.println("4. View Account Details");
            System.out.println("5. Logout");
            System.out.print("Select: ");
            int select = scanner.nextInt();

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
                    loggedIn = false;
                    break;
            }
        }
    }
}
