package com.weatherapp.Models;

import com.weatherapp.HelpingClasses.CacheManagement;
import com.weatherapp.Models.WeatherForecast;
import java.util.List;
import com.weatherapp.weatherapplication.ConsoleApp;
import com.weatherapp.Models.CurrentWeather;
import com.weatherapp.Models.WeatherForecast;
import com.weatherapp.Models.WeatherManager;
import com.weatherapp.Models.GeoCoder;

import java.io.*;
import java.text.SimpleDateFormat;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.Scanner;
import java.time.LocalDate;

public class FileHandling implements CacheManagement {

    private static final String CACHE_FILE_PATH = "src/main/cache/weather_data.txt";
    @Override
    public void insertWeatherData(String cityName, String day, String formattedDate, String time, String startingTime,
                                  int temperature, String description, int humidity, int pressure, int tempMax, int tempMin,
                                  int feelsLike, double windSpeed, int airQualityIndex, double carbonMonoxide,
                                  double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide,
                                  double ammonia, double particulateMatterPM25, double particulateMatterPM10, String icon) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE_PATH, true))) {
            WeatherManager weatherManager = new WeatherManager(cityName, "9804f15edc7893ea4947a7526edfc496");
            List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();
            CurrentWeather currentWeather = weatherManager.current_weather;

            if (!forecasts.isEmpty()) {
                WeatherForecast currentForecast = forecasts.get(0);

                StringBuilder weatherDataBuilder = new StringBuilder();
                weatherDataBuilder.append(cityName).append(",")
                        .append(day).append(",")
                        .append(formattedDate).append(",")
                        .append(time).append(",")
                        .append(startingTime).append(",")
                        .append(temperature).append(",")
                        .append(description).append(",")
                        .append(humidity).append(",")
                        .append(pressure).append(",")
                        .append(tempMax).append(",")
                        .append(tempMin).append(",")
                        .append(feelsLike).append(",")
                        .append(windSpeed).append(",")
                        .append(airQualityIndex).append(",")
                        .append(carbonMonoxide).append(",")
                        .append(nitrogenMonoxide).append(",")
                        .append(nitrogenDioxide).append(",")
                        .append(ozone).append(",")
                        .append(sulphurDioxide).append(",")
                        .append(ammonia).append(",")
                        .append(particulateMatterPM25).append(",")
                        .append(particulateMatterPM10).append(",")
                        .append(icon).append(",");

                writer.write(weatherDataBuilder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<WeatherForecast> getWeatherFromDb(String cityName, String startDate) {
        // Implementation goes here
        return null; // Placeholder, replace with actual implementation
    }

    @Override
    public boolean CheckExistance(String cityName, String date, String time) {
        // Implementation goes here
        return false; // Placeholder, replace with actual implementation
    }

    @Override
    public void deleteWeatherData(String cityName) {
        // Implementation goes here
    }
}