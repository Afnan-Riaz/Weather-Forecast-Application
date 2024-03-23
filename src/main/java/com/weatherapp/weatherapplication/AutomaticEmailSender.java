package com.weatherapp.weatherapplication;

import com.weatherapp.Models.WeatherManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Timer;

public class AutomaticEmailSender extends Application {

    private Timer timer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Automatic Email Sender");

        Button triggerButton = new Button("Trigger AQI Check & Send Email");
        triggerButton.setOnAction(event -> {
            checkAQIAndSendEmail();
        });

        StackPane root = new StackPane();
        root.getChildren().add(triggerButton);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private void checkAQIAndSendEmail() {
        try {
            WeatherManager weatherManager = new WeatherManager("Lahore,PK", "9804f15edc7893ea4947a7526edfc496");
            List<WeatherManager.WeatherForecast> forecasts = weatherManager.getWeatherForecast();

            for (WeatherManager.WeatherForecast forecast : forecasts) {
                if (forecast.airQualityIndex() > 3) {
                    sendNotificationEmail(forecast.airQualityIndex());
                    return; // Exit the method after sending the email
                }
            }
            System.out.println("No need to send email. AQI is within acceptable range.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotificationEmail(int aqi) {
        String username = "hussnainasim69@gmail.com";
        String password = "dyws xevv cngh kxst";
        String to = "hussnainasim9@gmail.com";
        String subject = "Air Quality Alert!";
        String body = "The Air Quality Index (AQI) is currently " + aqi + ", which means air quality is very poor. Please take necessary precautions.";

        SendEmail emailSender = new SendEmail(username, password);

        try {
            emailSender.sendEmail(to, subject, body);
            System.out.println("Notification email sent successfully.");
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
