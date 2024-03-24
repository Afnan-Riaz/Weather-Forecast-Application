package com.weatherapp.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;
import com.weatherapp.Models.Db_actions;
import com.weatherapp.HelpingClasses.CacheManagement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ForecastsLoader {
    public final String ApiKey;
    public List<WeatherForecast> forecasts;
    public double lat;
    public double lon;
    public String StartingTime;
    public String cityName;
    public ForecastsLoader(String apiKey, double lat, double lon) {
        this.ApiKey = apiKey;
        this.lat = lat;
        this.lon = lon;

    }
    public ForecastsLoader(String apiKey, double lat, double lon, String cityName) {
        this.ApiKey = apiKey;
        this.lat = lat;
        this.lon = lon;
        this.cityName = cityName != null ? cityName : null;
    }
    public JsonNode readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }
    public List<WeatherForecast> LoadForecasts(){
        forecasts = new ArrayList<>();
        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        try {
            System.out.println(cityName);
            System.out.println(getCurrentDate());
            System.out.println(getCurrentTime());
            if (cityName!=null&&(CacheManagement.CheckExistance(cityName, getCurrentDate(),getCurrentTime()))) {
                // Starting time exists in the database, fetch forecasts from the database
              forecasts=Db_actions.getWeatherFromDb(cityName,getCurrentDate());

            }
            else{
            JsonNode forecastData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + ApiKey + "&units=metric");
            JsonNode forecastList = forecastData.get("list");

            JsonNode airPollutionData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + ApiKey);
            JsonNode airPollutionList = airPollutionData.get("list");

            for (int i = 0; i < forecastList.size(); i++) {
                JsonNode forecast = forecastList.get(i);

                c.setTimeInMillis(forecast.get("dt").asLong() * 1000);

                int hour = c.get(Calendar.HOUR_OF_DAY);
                String time = (hour < 10 ? "0" : "") + hour + ":00";
                if(i==0){
                    StartingTime=time;
                }
                String day = df2.format(c.getTime());
                int temperature = forecast.get("main").get("temp").asInt();
                int humidity = forecast.get("main").get("humidity").asInt();
                int pressure = forecast.get("main").get("pressure").asInt();
                int tempMax = forecast.get("main").get("temp_max").asInt();
                int tempMin = forecast.get("main").get("temp_min").asInt();
                int feelsLike = forecast.get("main").get("feels_like").asInt();
                double windSpeed = forecast.get("wind").get("speed").asDouble();
                String description = forecast.get("weather").get(0).get("description").asText();
                String icon = forecast.get("weather").get(0).get("icon").asText();
                long dt = forecast.get("dt").asLong() * 1000;
                Date date = new Date(dt);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(date);

                JsonNode matchingAirPollutionData = null;
                for (JsonNode pollution : airPollutionList) {
                    if (pollution.get("dt").asLong() == forecast.get("dt").asLong()) {
                        matchingAirPollutionData = pollution;
                        break;
                    }
                }

                if (matchingAirPollutionData != null) {
                    double carbonMonoxide = matchingAirPollutionData.get("components").get("co").asDouble();
                    double nitrogenMonoxide = matchingAirPollutionData.get("components").get("no").asDouble();
                    double nitrogenDioxide = matchingAirPollutionData.get("components").get("no2").asDouble();
                    double ozone = matchingAirPollutionData.get("components").get("o3").asDouble();
                    double sulphurDioxide = matchingAirPollutionData.get("components").get("so2").asDouble();
                    double ammonia = matchingAirPollutionData.get("components").get("nh3").asDouble();
                    double particulateMatterPM25 = matchingAirPollutionData.get("components").get("pm2_5").asDouble();
                    double particulateMatterPM10 = matchingAirPollutionData.get("components").get("pm10").asDouble();
                    int airQualityIndex = matchingAirPollutionData.get("main").get("aqi").asInt();
                    //Insert data to db for first time in 3 hours
          Db_actions.insertWeatherData("Lahore",day,formattedDate, time,StartingTime, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed,
        airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
        particulateMatterPM25, particulateMatterPM10, icon);
                    forecasts.add(new WeatherForecast(day,formattedDate, time, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed,
                            airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
                            particulateMatterPM25, particulateMatterPM10, icon));
                }
            }}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return forecasts;
    }
    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        return String.format("%02d:00", hour);
    }
    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

}


