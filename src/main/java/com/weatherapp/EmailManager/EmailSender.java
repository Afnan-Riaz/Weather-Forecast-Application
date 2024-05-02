package com.weatherapp.EmailManager;

import com.weatherapp.HelpingClasses.EmailTask;
import com.weatherapp.Records.ForecastWithPollution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSender {
    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void checkAndSendEmail(ForecastWithPollution forecast, String city) {

        if (forecast.windSpeed() > 10) {
            String message = "There is high wind speed in " + city + ". please take necessary precautions.";
            executorService.submit(new EmailTask(message));
        }

        if (forecast.temperature() > 37 || forecast.temperature() < 0) {
            if (forecast.temperature() > 37) {
                String message = "It is too much hot in " + city + " as temperature is " + forecast.temperature() + "°C. please take necessary precautions.";
                executorService.submit(new EmailTask(message));
            }
            if (forecast.temperature() < 0) {
                String message = "It is too much cold in " + city + " as temperature is " + forecast.temperature() + "°C. please take necessary precautions.";
                executorService.submit(new EmailTask(message));
            }
        }
        if (forecast.airQualityIndex() >= 4) {
            String body = "The Air Quality Index (AQI) in " + city + " is currently " + forecast.airQualityIndex() + ", which means air quality is very poor. Please take necessary precautions.";
            // System.out.print(aqi);
            executorService.submit(new EmailTask(body));
        }
    }
}
