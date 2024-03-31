package com.weatherapp.HelpingClasses;

import com.weatherapp.Models.ForecastWithPollution;
import com.weatherapp.Models.Weather;
import java.util.List;
import java.util.Set;

public interface CacheManagement {
    void insertWeatherData(String cityName, String day, String formattedDate, String time, String startingTime, int temperature, String description, int humidity, int pressure, int tempMax, int tempMin, int feelsLike, double windSpeed, int airQualityIndex, double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide, double ammonia, double particulateMatterPM25, double particulateMatterPM10, String icon);

    List<ForecastWithPollution> getWeatherFromDb(String cityName, String startDate);

    boolean CheckExistance(String cityName, String date, String time);

    void deleteWeatherData(String cityName);

    public Set<String> getAllCityNames();
}

