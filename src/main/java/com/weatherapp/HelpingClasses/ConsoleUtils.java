package com.weatherapp.HelpingClasses;

import com.weatherapp.EmailManager.AutomaticEmailSender;
import com.weatherapp.Models.GeoCoder;
import com.weatherapp.Models.WeatherManager;
import com.weatherapp.Records.ForecastWithPollution;
import com.weatherapp.Records.Weather;
import com.weatherapp.CacheManagement.FileHandling;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.*;
public class ConsoleUtils {
    public static String dbType;
    public static String city;

    public static  WeatherManager weatherManager;
    public static void printOptions(){
        System.out.println("Welcome to WeatherApp Console!");
        System.out.println("Select an option:");
        System.out.println("1. Add Location to check weather with Longitude and Latitude.");
        System.out.println("2. Add Location to check weather with City/Country Name.");
        System.out.println("3. Show Current Weather Conditions.");
        System.out.println("4. Show Weather Forecast for 5 days.");
        System.out.println("5. Show Sunrise and sunset Time.");
        System.out.println("6. Show Air Pollution Data.");
        System.out.println("7. Show Data about Polluting Gases.");
        System.out.println("8. Check Weather of Already Searched City.");
        System.out.println("0. Exit");
        
    }
    public static AutomaticEmailSender handleEmail(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to receive notifications via email? (yes/no)");
        String receiveNotifications = scanner.nextLine().toLowerCase();
        String email = null;
        AutomaticEmailSender emailSender = null;
        if (receiveNotifications.equalsIgnoreCase("yes")) {
            System.out.println("Enter your email address:");
            email = scanner.nextLine();
             emailSender = new AutomaticEmailSender();
            emailSender.setEmailAddress(email);
        }
        return emailSender;
    }
    public static void ConsoleWeatherManager(int choice,String dbtype){
        Scanner scanner = new Scanner(System.in);
        dbType=dbtype;
            if (choice!=1 && choice!=8 && choice!=0){
                System.out.print("Enter the city name: ");
                city = scanner.nextLine();
         weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496", dbType);
            }

        switch (choice) {
            case 1:
                System.out.println("Enter latitude:");
                double latitude = scanner.nextDouble();
                System.out.println("Enter longitude:");
                double longitude = scanner.nextDouble();
                displayWeatherByCoordinates(latitude, longitude);
                break;
            case 2:

                displayWeatherByCity();
                break;
            case 3:
                displayWeatherInformation();
                break;
            case 4:
                displayForecastWithPollution();

                break;
            case 5:
                displaySunriseSunsetTime();
                break;
            case 6:
                displayAirPollutionData();
                break;
            case 7:
                displayPollutingGasesData();
                break;
            case 8:
                displayExistingCityNames();
                break;
            case 0:
                System.out.println("Exiting WeatherApp Console. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                break;
        }
    }
    private static void displayWeatherInformation() {

        List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();
        if (!forecasts.isEmpty()) {
            ForecastWithPollution currentForecast = forecasts.getFirst();


            System.out.println(STR."Day: \{currentForecast.day()}");
            System.out.println(STR."Time: \{currentForecast.time()}");
            System.out.println(STR."Temperature: \{currentForecast.temperature()}°C");
            System.out.println(STR."Weather: \{currentForecast.description()}");
            System.out.println(STR."Humidity: \{currentForecast.humidity()}%");
            System.out.println(STR."Pressure: \{currentForecast.pressure()} hPa");
            System.out.println(STR."Max Temperature: \{currentForecast.tempMax()}°C");
            System.out.println(STR."Min Temperature: \{currentForecast.tempMin()}°C");

            System.out.println(STR."Feels Like: \{currentForecast.feelsLike()}°C");
            System.out.println(STR."Wind Speed: \{currentForecast.windSpeed()} m/s");
        } else {
            System.out.println("Failed to fetch forecast data. Please try again later.");
        }
    }
    private static final FileHandling fileHandling = new FileHandling();
    private static void displayExistingCityNames() {
        Set<String> existingCityNames = fileHandling.getAllCityNames();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Existing Cities:");
        for (String cityName : existingCityNames) {
            System.out.println(cityName);
        }

        System.out.print("Enter the city name you want to search: ");
        String city = scanner.nextLine();

        if (existingCityNames.contains(city)) {
            weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496", dbType);
            displayWeatherByCity();
        } else {
            // City doesn't exist in the database
            System.out.println("Error: The entered city name does not exist in the database.");
        }
    }

    private static void displayWeatherByCity() {
        List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();
        System.out.println(weatherManager);
        if (!forecasts.isEmpty()) {
            ForecastWithPollution currentForecast = forecasts.getFirst();
            System.out.println(STR."\nCurrent Weather for \{city}:\n");
            System.out.println(STR."Day: \{currentForecast.day()}");
            System.out.println(STR."Time: \{currentForecast.time()}");
            System.out.println(STR."Temperature: \{currentForecast.temperature()}°C");
            System.out.println(STR."Weather: \{currentForecast.description()}");
            System.out.println(STR."Humidity: \{currentForecast.humidity()}%");
            System.out.println(STR."Pressure: \{currentForecast.pressure()} hPa");
            System.out.println(STR."Max Temperature: \{currentForecast.tempMax()}°C");
            System.out.println(STR."Min Temperature: \{currentForecast.tempMin()}°C");
            System.out.println(STR."Feels Like: \{currentForecast.feelsLike()}°C");
            System.out.println(STR."Wind Speed: \{currentForecast.windSpeed()} m/s");
        } else {
            System.out.println("Failed to fetch forecast data. Please try again later.");
        }
    }

    private static void displayWeatherByCoordinates(double latitude, double longitude) {
        // Create a GeoCoder object to fetch the city name from latitude and longitude
        GeoCoder geoCoder = new GeoCoder("9804f15edc7893ea4947a7526edfc496", latitude, longitude);
        String Resultcity = geoCoder.getCity();
        if (!Resultcity.isEmpty()) {
            city=Resultcity;
            weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496", dbType);
            displayWeatherByCity();
        }
        else{
            System.out.println("No city exits with these coordinates!.Please try again and enter correct parameters.");
        }
    }



    private static void displayForecastWithPollution() {
      List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();
        System.out.println(STR."\nWeather Forecast for \{city}:\n");
        for (ForecastWithPollution forecast : forecasts) {
            System.out.println(STR."Day: \{forecast.day()}");
            System.out.println(STR."Time: \{forecast.time()}");
            System.out.println(STR."Temperature: \{forecast.temperature()}°C");
            System.out.println(STR."Weather: \{forecast.description()}");
            System.out.println(STR."Humidity: \{forecast.humidity()}%");
            System.out.println(STR."Pressure: \{forecast.pressure()} hPa");
            System.out.println(STR."Max Temperature: \{forecast.tempMax()}°C");
            System.out.println(STR."Min Temperature: \{forecast.tempMin()}°C");
            System.out.println(STR."Feels Like: \{forecast.feelsLike()}°C");
            System.out.println(STR."Wind Speed: \{forecast.windSpeed()} m/s");
            System.out.println(STR."Air Quality Index (AQI): \{forecast.airQualityIndex()}");
            System.out.println();
        }
    }

    private static void displaySunriseSunsetTime() {
        List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();
        Weather currentWeather = weatherManager.current_weather;

        // Display Current Weather
        System.out.println("\nSunrise and Sunset Time for Lahore, PK:\n");
        System.out.println("Sunrise Time: " + formatTime(currentWeather.sunrise()));
        System.out.println("Sunset Time: " + formatTime(currentWeather.sunset()));
    }

    private static void displayAirPollutionData() {
        List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();

        // Check if the list of forecasts is not empty
        if (!forecasts.isEmpty()) {
            // Get the first forecast,
            // which is assumed to be the current forecast
            ForecastWithPollution currentForecast = forecasts.getFirst();

            // Display data about air pollution for the current time
            System.out.println(STR."\nAir Pollution Data for \{city}:\n");
            // Extract data about air pollution from the current forecast
            System.out.println(STR."Air Quality Index (AQI): \{currentForecast.airQualityIndex()}");
            System.out.println(STR."Particulate Matter PM2.5: \{currentForecast.particulateMatterPM25()}");
            System.out.println(STR."Particulate Matter PM10: \{currentForecast.particulateMatterPM10()}");

        } else {
            System.out.println("Failed to fetch forecast data. Please try again later.");
        }
    }

    private static void displayPollutingGasesData() {
        List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();

        // Check if the list of forecasts is not empty
        if (!forecasts.isEmpty()) {
            // Get the first forecast, which is assumed to be the current forecast
            ForecastWithPollution currentForecast = forecasts.getFirst();
            // Display data about polluting gases for the current time
            System.out.println(STR."\nPolluting Gases Data for \{city}:\n");
            // Extract data about polluting gases from the current forecast
            System.out.println(STR."Carbon Monoxide: \{currentForecast.carbonMonoxide()}");
            System.out.println(STR."Nitrogen Monoxide: \{currentForecast.nitrogenMonoxide()}");
            System.out.println(STR."Nitrogen Dioxide: \{currentForecast.nitrogenDioxide()}");
            System.out.println(STR."Ozone: \{currentForecast.ozone()}");
            System.out.println(STR."Sulphur Dioxide: \{currentForecast.sulphurDioxide()}");
            System.out.println(STR."Ammonia: \{currentForecast.ammonia()}");

        } else {
            System.out.println("Failed to fetch forecast data. Please try again later.");
        }
    }

    private static String formatTime(long timeInMillis) {
//        Date date = new Date(timeInMillis); // Convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(timeInMillis);
    }
    
}
