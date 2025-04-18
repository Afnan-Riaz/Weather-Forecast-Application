package com.weatherapp.Models;

import com.weatherapp.EmailManager.EmailSender;
import com.weatherapp.Records.ForecastWithPollution;
import com.weatherapp.Records.Weather;

import java.util.*;

public class WeatherManager {
    private final String city;
    private final String apiKey;
    private String DbType;
    public String IPAddress;
    public Weather current_weather;
    public List<ForecastWithPollution> forecasts;
    public WeatherManager(String city, String apiKey, String db) {
        this.city = city;
        this.apiKey = apiKey;
        DbType = db;
    }

    public List<ForecastWithPollution> getWeatherForecast() {
        IPAddress = LiveLocationTracker.getIPAddress();

        ForecastWithPollutionManager forecastsManager = new ForecastWithPollutionManager(city, apiKey, IPAddress, DbType);
        forecasts = forecastsManager.ReturnWeatherForecasts();

        current_weather = forecastsManager.current_weather;

        EmailSender.checkAndSendEmail(forecasts.getFirst(), city);
        return forecasts;
    }

}