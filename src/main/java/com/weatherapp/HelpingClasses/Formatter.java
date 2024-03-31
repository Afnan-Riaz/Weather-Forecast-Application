package com.weatherapp.HelpingClasses;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Formatter {


    public static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String convertTo12HourFormat(String time24Hour) {
        // Split the input time string into hours and minutes
        String[] parts = time24Hour.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        // Determine whether it's am or pm
        String period = (hours >= 12) ? "pm" : "am";

        // Convert hours to 12-hour format
        if (hours > 12) {
            hours -= 12;
        } else if (hours == 0) {
            hours = 12; // 0 hour in 24-hour format is 12 am in 12-hour format
        }

        // Construct the string in 12-hour format
        String time12Hour = String.format("%d:%02d %s", hours, minutes, period);

        return time12Hour;
    }
    public static void formatText(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (!change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return null;
            } else {
                return change;
            }
        });
        textField.setTextFormatter(formatter);
    }

    public static String formatTime(long timeInSeconds) {
        Date date = new Date(timeInSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Karachi"));
        return sdf.format(date);
    }
}
