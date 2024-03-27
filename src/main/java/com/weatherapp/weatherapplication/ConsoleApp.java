package com.weatherapp.weatherapplication;

import com.weatherapp.Models.CurrentWeather;
import com.weatherapp.Models.WeatherForecast;
import com.weatherapp.Models.WeatherManager;
import com.weatherapp.Models.GeoCoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class ConsoleApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to WeatherApp Console!");
        System.out.println("Select an option:");
        System.out.println("1. Add Location to check weather with Longitude and Latitude.");
        System.out.println("2. Add Location to check weather with City/Country Name.");
        System.out.println("3. Show Current Weather Conditions.");
        System.out.println("4. Show Weather Forecast for 5 days.");
        System.out.println("5. Show Sunrise and sunset Time.");
        System.out.println("6. Show Air Pollution Data.");
        System.out.println("7. Show Data about Polluting Gases.");
        System.out.println("0. Exit");
        int choice;
        do {
            System.out.print("\nEnter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.println("Enter latitude:");
                    double latitude = scanner.nextDouble();
                    System.out.println("Enter longitude:");
                    double longitude = scanner.nextDouble();
                    displayWeatherByCoordinates(latitude, longitude);
                    break;
                case 2:
                    System.out.print("Enter the city name: ");
                    String city2 = scanner.nextLine();
                    displayWeatherByCity(city2);
                    break;
                case 3:
                    displayWeatherInformation();
                    break;
                case 4:
                    System.out.print("Enter the city name: ");
                    String city4 = scanner.nextLine();
                    displayWeatherForecast(city4);
                    break;
                case 5:
                    displaySunriseSunsetTime();
                    break;
                case 6:
                    System.out.print("Enter the city name: ");
                    String city6 = scanner.nextLine();
                    displayAirPollutionData(city6);
                    break;
                case 7:
                    System.out.print("Enter the city name: ");
                    String city7 = scanner.nextLine();
                    displayPollutingGasesData(city7);
                    break;
                case 0:
                    System.out.println("Exiting WeatherApp Console. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        } while (choice != 0);
    }

    private static void displayWeatherByCoordinates(double latitude, double longitude) {
        // Create a GeoCoder object to fetch the city name from latitude and longitude
        GeoCoder geoCoder = new GeoCoder("9804f15edc7893ea4947a7526edfc496", latitude, longitude);
        String city = geoCoder.getCity();

        if (!city.isEmpty()) {
            // Fetch weather data using the obtained city name
            WeatherManager weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496");
            List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();

            // Check if the list of forecasts is not empty
            if (!forecasts.isEmpty()) {
                // Get the first forecast, assumed to be the current forecast
                WeatherForecast currentForecast = forecasts.get(0);

                System.out.println("\nCurrent Weather for " + city + ":\n");
                System.out.println("Day: " + currentForecast.day());
                System.out.println("Temperature: " + currentForecast.temperature() + "°C");
                System.out.println("Weather: " + currentForecast.description());
                System.out.println("Humidity: " + currentForecast.humidity() + "%");
                System.out.println("Pressure: " + currentForecast.pressure() + " hPa");
                System.out.println("Max Temperature: " + currentForecast.tempMax() + "°C");
                System.out.println("Min Temperature: " + currentForecast.tempMin() + "°C");
                System.out.println("Feels Like: " + currentForecast.feelsLike() + "°C");
                System.out.println("Wind Speed: " + currentForecast.windSpeed() + " m/s");
            } else {
                System.out.println("Failed to fetch forecast data. Please try again later.");
            }
        } else {
            System.out.println("Failed to fetch city data for the provided coordinates. Please try again.");
        }
    }


    private static void displayWeatherByCity(String city) {
        WeatherManager weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496");
        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();

        // Check if the list of forecasts is not empty
        if (!forecasts.isEmpty()) {
            // Get the first forecast,
            // which is assumed to be the current forecast
            WeatherForecast currentForecast = forecasts.get(0);

            System.out.println("\nCurrent Weather for " + city + ":\n");
            System.out.println("Day: " + currentForecast.day());
            System.out.println("Temperature: " + currentForecast.temperature() + "°C");
            System.out.println("Weather: " + currentForecast.description());
            System.out.println("Humidity: " + currentForecast.humidity() + "%");
            System.out.println("Pressure: " + currentForecast.pressure() + " hPa");
            System.out.println("Max Temperature: " + currentForecast.tempMax() + "°C");
            System.out.println("Min Temperature: " + currentForecast.tempMin() + "°C");
            System.out.println("Feels Like: " + currentForecast.feelsLike() + "°C");
            System.out.println("Wind Speed: " + currentForecast.windSpeed() + " m/s");
        }
        else
        {
            System.out.println("Failed to fetch forecast data. Please try again later.");
        }
    }

    private static void displayWeatherInformation() {
        System.out.println("Displaying Weather Information:");

        // Fetch weather data for a default location
        WeatherManager weatherManager = new WeatherManager("Lahore,PK", "9804f15edc7893ea4947a7526edfc496");
        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();
        if (weatherManager.current_weather != null) {
            CurrentWeather currentWeather = weatherManager.current_weather;

            // Display Current Weather
            System.out.println("\nCurrent Weather:\n");
            System.out.println("Day: " + currentWeather.day());
            System.out.println("Time: " + currentWeather.time());
            System.out.println("Temperature: " + currentWeather.temperature() + "°C");
            System.out.println("Weather: " + currentWeather.description());
            System.out.println("Humidity: " + currentWeather.humidity() + "%");
            System.out.println("Pressure: " + currentWeather.pressure() + " hPa");
            System.out.println("Max Temperature: " + currentWeather.tempMax() + "°C");
            System.out.println("Min Temperature: " + currentWeather.tempMin() + "°C");
            System.out.println("Feels Like: " + currentWeather.feelsLike() + "°C");
            System.out.println("Wind Speed: " + currentWeather.windSpeed() + " m/s");
        } else {
            System.out.println("Failed to fetch current weather data. Please try again later.");
        }
    }


    private static void displayWeatherForecast(String city) {
        WeatherManager weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496");
        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();
        System.out.println("\nWeather Forecast for " + city + ":\n");
        for (WeatherForecast forecast : forecasts) {
            System.out.println("Day: " + forecast.day());
            System.out.println("Time: " + forecast.time());
            System.out.println("Temperature: " + forecast.temperature() + "°C");
            System.out.println("Weather: " + forecast.description());
            System.out.println("Humidity: " + forecast.humidity() + "%");
            System.out.println("Pressure: " + forecast.pressure() + " hPa");
            System.out.println("Max Temperature: " + forecast.tempMax() + "°C");
            System.out.println("Min Temperature: " + forecast.tempMin() + "°C");
            System.out.println("Feels Like: " + forecast.feelsLike() + "°C");
            System.out.println("Wind Speed: " + forecast.windSpeed() + " m/s");
            System.out.println("Air Quality Index (AQI): " + forecast.airQualityIndex());
            System.out.println();
        }
    }

    private static void displaySunriseSunsetTime() {
        WeatherManager weatherManager = new WeatherManager("Lahore,PK", "9804f15edc7893ea4947a7526edfc496");
        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();
        CurrentWeather currentWeather = weatherManager.current_weather;

        // Display Current Weather
        System.out.println("\nSunrise and Sunset Time for Lahore, PK:\n");
        System.out.println("Sunrise Time: " + formatTime(currentWeather.sunrise()));
        System.out.println("Sunset Time: " + formatTime(currentWeather.sunset()));
    }

    private static void displayAirPollutionData(String city) {
        WeatherManager weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496");
        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();

        // Check if the list of forecasts is not empty
        if (!forecasts.isEmpty()) {
            // Get the first forecast,
            // which is assumed to be the current forecast
            WeatherForecast currentForecast = forecasts.get(0);

            // Display data about air pollution for the current time
            System.out.println("\nAir Pollution Data for " + city + ":\n");
            // Extract data about air pollution from the current forecast
            System.out.println("Air Quality Index (AQI): " + currentForecast.airQualityIndex());
            System.out.println("Particulate Matter PM2.5: " + currentForecast.particulateMatterPM25());
            System.out.println("Particulate Matter PM10: " + currentForecast.particulateMatterPM10());

        } else {
            System.out.println("Failed to fetch forecast data. Please try again later.");
        }
    }

    private static void displayPollutingGasesData(String city) {
        WeatherManager weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496");
        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();

        // Check if the list of forecasts is not empty
        if (!forecasts.isEmpty()) {
            // Get the first forecast, which is assumed to be the current forecast
            WeatherForecast currentForecast = forecasts.get(0);
            // Display data about polluting gases for the current time
            System.out.println("\nPolluting Gases Data for " + city + ":\n");
            // Extract data about polluting gases from the current forecast
            System.out.println("Carbon Monoxide: " + currentForecast.carbonMonoxide());
            System.out.println("Nitrogen Monoxide: " + currentForecast.nitrogenMonoxide());
            System.out.println("Nitrogen Dioxide: " + currentForecast.nitrogenDioxide());
            System.out.println("Ozone: " + currentForecast.ozone());
            System.out.println("Sulphur Dioxide: " + currentForecast.sulphurDioxide());
            System.out.println("Ammonia: " + currentForecast.ammonia());

        } else {
            System.out.println("Failed to fetch forecast data. Please try again later.");
        }
    }

    private static String formatTime(long timeInMillis) {
        // Convert milliseconds since epoch to date and time format
        Date date = new Date(timeInMillis); // Convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(timeInMillis);
    }

}
