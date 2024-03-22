package com.weatherapp.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeatherManager {
    private final String city;
    private final String apiKey;
    public CurrentWeather current_weather;
    public List<WeatherForecast> forecasts;
    public WeatherManager(String city, String apiKey) {
        this.city = city;
        this.apiKey = apiKey;
    }

    public List<WeatherForecast> getWeatherForecast() {

        CurrentWeatherLoader currentWeatherLoader = new CurrentWeatherLoader(city, apiKey);
        current_weather = currentWeatherLoader.LoadCurrentWeather();

        String icon = current_weather.icon();
        double lat = currentWeatherLoader.lat;
        double lon = currentWeatherLoader.lon;

        ForecastsLoader forecastsLoader = new ForecastsLoader(city, apiKey, lat, lon);
        forecasts = forecastsLoader.LoadForecasts();

        return forecasts;
    }

    public static void main(String[] args) {
        WeatherManager weatherManager = new WeatherManager("Lahore,PK", "9804f15edc7893ea4947a7526edfc496");
        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();

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
            System.out.println("Air Pollution:");
            System.out.println("  - Carbon monoxide (CO): " + forecast.carbonMonoxide());
            System.out.println("  - Nitrogen monoxide (NO): " + forecast.nitrogenMonoxide());
            System.out.println("  - Nitrogen dioxide (NO2): " + forecast.nitrogenDioxide());
            System.out.println("  - Ozone (O3): " + forecast.ozone());
            System.out.println("  - Sulphur dioxide (SO2): " + forecast.sulphurDioxide());
            System.out.println("  - Ammonia (NH3): " + forecast.ammonia());
            System.out.println("  - Particulate Matter (PM2.5): " + forecast.particulateMatterPM25());
            System.out.println("  - Particulate Matter (PM10): " + forecast.particulateMatterPM10());
            System.out.println();
        }
    }
}
