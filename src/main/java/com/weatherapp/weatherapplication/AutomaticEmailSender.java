package com.weatherapp.weatherapplication;

import com.weatherapp.Models.WeatherForecast;
import com.weatherapp.Models.WeatherManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Timer;

public class AutomaticEmailSender {

    public void sendNotificationEmail(String body) {
        String username = "hussnainasim69@gmail.com";
        String password = "dyws xevv cngh kxst";
        String to = "hussnainasim9@gmail.com";
        String subject = "Weather Alert!";

        SendEmail emailSender = new SendEmail(username, password);

        try {
            emailSender.sendEmail(to, subject, body);
            System.out.println("Notification email sent successfully.");
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
