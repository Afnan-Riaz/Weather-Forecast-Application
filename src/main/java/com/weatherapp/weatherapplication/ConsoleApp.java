package com.weatherapp.weatherapplication;

import com.weatherapp.CacheManagement.DatabaseType;
import com.weatherapp.EmailManager.AutomaticEmailSender;
import com.weatherapp.HelpingClasses.ConsoleUtils;

import java.util.Scanner;


public class ConsoleApp {
    public static String dbType;

    public static void main(String[] args) {
        try {
            dbType = args[1];
        }
        catch (ArrayIndexOutOfBoundsException e){
            dbType = "sql";
        }
        DatabaseType.setDbType(dbType);

        Scanner scanner = new Scanner(System.in);

        // email handling
        AutomaticEmailSender emailSender = ConsoleUtils.handleEmail();

        // print options
        ConsoleUtils.printOptions();
        int choice;
        do {
            System.out.print("\nEnter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            ConsoleUtils.ConsoleWeatherManager(choice,dbType);

        } while (choice != 0);
    }
}
