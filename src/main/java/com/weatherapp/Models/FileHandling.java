package com.weatherapp.Models;

import com.weatherapp.HelpingClasses.CacheManagement;
import com.weatherapp.Models.WeatherForecast;
import java.util.List;

public class FileHandling implements CacheManagement {

    @Override
    public void insertWeatherData(String cityName, String day, String formattedDate, String time, String startingTime,
                                  int temperature, String description, int humidity, int pressure, int tempMax, int tempMin,
                                  int feelsLike, double windSpeed, int airQualityIndex, double carbonMonoxide,
                                  double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide,
                                  double ammonia, double particulateMatterPM25, double particulateMatterPM10, String icon) {
        // Implementation goes here
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
