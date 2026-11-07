package com.eprs;

import com.eprs.dao.ActivityLogDao;
import com.eprs.dao.DatabaseInitializer;
import com.eprs.dao.UserDao;
import com.eprs.ui.Authentication;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();

        Scanner scanner = new Scanner(System.in);
        ActivityLogDao logDao = new  ActivityLogDao();
        UserDao userDao = new UserDao();

        Authentication auth = new Authentication(userDao, logDao, scanner);
        auth.start();
        scanner.close();
    }
}