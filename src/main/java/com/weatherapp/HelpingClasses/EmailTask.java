package com.weatherapp.HelpingClasses;

import com.weatherapp.EmailManager.AutomaticEmailSender;

import java.util.concurrent.Callable;

public class EmailTask implements Callable<Void> {
    private final String message;

    public EmailTask(String message) {
        this.message = message;
    }

    @Override
    public Void call() {
        AutomaticEmailSender emailSender = new AutomaticEmailSender();
        emailSender.sendNotificationEmail(message);
        return null;
    }
}