package com;

import com.weatherapp.weatherapplication.App;
import com.weatherapp.weatherapplication.ConsoleApp;

public class MainDriver {
    public static void main(String[] args) {
        if (args.length < 2){
            System.err.println("Please enter all parameters first.");
            System.exit(1);
        }
        if (args[0].equalsIgnoreCase("console")){
            ConsoleApp.main(args);
        }
        else{
            App.main(args);
        }
    }
}
