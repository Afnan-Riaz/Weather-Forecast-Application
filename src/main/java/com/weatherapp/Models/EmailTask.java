package com.weatherapp.Models;

import com.weatherapp.weatherapplication.AutomaticEmailSender;

import java.util.concurrent.Callable;

class EmailTask implements Callable<Void> {
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