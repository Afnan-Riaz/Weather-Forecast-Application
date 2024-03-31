package com.weatherapp.weatherapplication;

import javax.mail.MessagingException;


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
                System.err.println("\nError sending email: " + e.getMessage());
            }
       }
    }
}
