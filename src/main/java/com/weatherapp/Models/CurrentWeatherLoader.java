package com.weatherapp.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.Records.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrentWeatherLoader {
    public final String City;
    public final String ApiKey;
    public Weather weather;
    public double lat;
    public double lon;

    public CurrentWeatherLoader(String city, String apiKey) {
        this.City = city;
        this.ApiKey = apiKey;
    }

    public JsonNode readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }

    public Weather LoadCurrentWeather() {
        int temp = -1;
        double windSpeed = -1;
        int visibility = -1;
        int rain = -1;
        int snow = -1;

        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            JsonNode currentWeatherData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/weather?q=" + City + "&appid=" + ApiKey + "&units=metric");
            lat = currentWeatherData.get("coord").get("lat").asDouble();
            lon = currentWeatherData.get("coord").get("lon").asDouble();

            long sunrise = currentWeatherData.get("sys").get("sunrise").asLong() * 1000;
            long sunset = currentWeatherData.get("sys").get("sunset").asLong() * 1000;
            temp = currentWeatherData.get("main").get("temp").asInt();
            String desc = currentWeatherData.get("weather").get(0).get("description").asText();
            int humidity = currentWeatherData.get("main").get("humidity").asInt();
            int pressure = currentWeatherData.get("main").get("pressure").asInt();
            int tempMax = currentWeatherData.get("main").get("temp_max").asInt();
            int tempMin = currentWeatherData.get("main").get("temp_min").asInt();
            int feelsLike = currentWeatherData.get("main").get("feels_like").asInt();
            windSpeed = currentWeatherData.get("wind").get("speed").asDouble();
            visibility = currentWeatherData.has("visibility") ? currentWeatherData.get("visibility").asInt() : -1;
            rain = currentWeatherData.has("rain") ? currentWeatherData.get("rain").asInt() : -1;
            snow = currentWeatherData.has("snow") ? currentWeatherData.get("snow").asInt() : -1;

            long dt = currentWeatherData.get("dt").asLong() * 1000;
            Date date = new Date(dt);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(date);

            String icon = currentWeatherData.get("weather").get(0).get("icon").asText();

            c.setTimeInMillis(currentWeatherData.get("dt").asLong() * 1000);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            String time = (hour < 10 ? "0" : "") + hour + ":00";
            String day = df2.format(c.getTime());

            weather = new Weather(day, formattedDate, time, temp, desc, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed, sunrise, sunset, icon, visibility, rain, snow);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return weather;
    }
}
