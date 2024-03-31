package com.weatherapp.weatherapplication;

import com.weatherapp.Models.WeatherManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Timer;


public class AutomaticEmailSender {

 static private String emailAddress ;

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void sendNotificationEmail(String body) {
       if (emailAddress != null) {
            String username = "hussnainasim69@gmail.com";
            String password = "dyws xevv cngh kxst";
            String to = emailAddress;
            String subject = "Weather Alert!";

            SendEmail emailSender = new SendEmail(username, password);
            try {
                emailSender.sendEmail(to, subject, body);
            } catch (MessagingException e) {
                System.err.println("Error sending email: " + e.getMessage());
            }
       }
    }
}
