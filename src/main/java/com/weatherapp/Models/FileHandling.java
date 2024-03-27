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
        try {
            File inputFile = new File(CACHE_FILE_PATH);
            File tempFile = new File("src/main/cache/temp_weather_data.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = cityName + ",";
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                // Remove the line if it contains the cityName
                if (currentLine.contains(lineToRemove)) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            // Delete the original file
            if (!inputFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            // Rename the temp file to the original file name
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        FileHandling fileHandling = new FileHandling();
        //fileHandling.insertWeatherData("New York", "Monday", "2024-03-24", "12:00", "11:00", 20, "Sunny", 50, 1013, 25, 18, 22, 5.5, 50, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, "sunny.png");
        //fileHandling.deleteWeatherData("New York");
    }


}