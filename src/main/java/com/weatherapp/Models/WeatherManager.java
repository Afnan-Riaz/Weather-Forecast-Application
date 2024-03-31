package com.weatherapp.Models;

import java.util.*;

public class WeatherManager {
    private final String city;
    private final String apiKey;
    private final String DbType = "sql";
    public String IPAddress;
    public Weather current_weather;
    public List<ForecastWithPollution> forecasts;
    public WeatherManager(String city, String apiKey) {
        this.city = city;
        this.apiKey = apiKey;
    }

    public List<ForecastWithPollution> getWeatherForecast() {
        IPAddress = LiveLocationTracker.getIPAddress();

        ForecastWithPollutionManager forecastsManager = new ForecastWithPollutionManager(city, apiKey, IPAddress, DbType);
        forecasts = forecastsManager.ReturnWeatherForecasts();

        current_weather = forecastsManager.current_weather;

        return forecasts;
    }

//    public static void main(String[] args) {
//        WeatherManager weatherManager = new WeatherManager("Lahore,PK", "9804f15edc7893ea4947a7526edfc496");
//        List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();
//
//        for (ForecastWithPollution forecast : forecasts) {
//            System.out.println("Day: " + forecast.day());
//            System.out.println("Time: " + forecast.time());
//            System.out.println("Temperature: " + forecast.temperature() + "°C");
//            System.out.println("Weather: " + forecast.description());
//            System.out.println("Humidity: " + forecast.humidity() + "%");
//            System.out.println("Pressure: " + forecast.pressure() + " hPa");
//            System.out.println("Max Temperature: " + forecast.tempMax() + "°C");
//            System.out.println("Min Temperature: " + forecast.tempMin() + "°C");
//            System.out.println("Feels Like: " + forecast.feelsLike() + "°C");
//            System.out.println("Wind Speed: " + forecast.windSpeed() + " m/s");
//            System.out.println("Air Quality Index (AQI): " + forecast.airQualityIndex());
//            System.out.println("Air Pollution:");
//            System.out.println("  - Carbon monoxide (CO): " + forecast.carbonMonoxide());
//            System.out.println("  - Nitrogen monoxide (NO): " + forecast.nitrogenMonoxide());
//            System.out.println("  - Nitrogen dioxide (NO2): " + forecast.nitrogenDioxide());
//            System.out.println("  - Ozone (O3): " + forecast.ozone());
//            System.out.println("  - Sulphur dioxide (SO2): " + forecast.sulphurDioxide());
//            System.out.println("  - Ammonia (NH3): " + forecast.ammonia());
//            System.out.println("  - Particulate Matter (PM2.5): " + forecast.particulateMatterPM25());
//            System.out.println("  - Particulate Matter (PM10): " + forecast.particulateMatterPM10());
//            System.out.println();
//        }
//    }
}