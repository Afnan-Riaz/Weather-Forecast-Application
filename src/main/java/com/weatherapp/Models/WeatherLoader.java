package com.weatherapp.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.weatherapplication.AutomaticEmailSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherLoader {
    public final String ApiKey;
    public List<Weather> forecasts;
    public double lat;
    public double lon;
    public Weather current_weather;
    public WeatherLoader(String apiKey, double lat, double lon, Weather current_weather) {
        this.ApiKey = apiKey;
        this.lat = lat;
        this.lon = lon;
        this.current_weather = current_weather;
    }

    public JsonNode readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }
    public void checkAQIAndSendEmail(int aqi) {
        if (aqi >= 3) {
            String body = "The Air Quality Index (AQI) is currently " + aqi + ", which means air quality is very poor. Please take necessary precautions.";
           // System.out.print(aqi);
            AutomaticEmailSender emailSender = new AutomaticEmailSender();
            emailSender.sendNotificationEmail(body);
        }
    }
    public List<Weather> LoadWeatherData(){
        forecasts = new ArrayList<>();
        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        int airQualityIndex =-1;

        try {
            JsonNode forecastData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + ApiKey + "&units=metric");
            JsonNode forecastList = forecastData.get("list");

            for (int i = 0; i < forecastList.size(); i++) {
                JsonNode forecast = forecastList.get(i);

                c.setTimeInMillis(forecast.get("dt").asLong() * 1000);

                int hour = c.get(Calendar.HOUR_OF_DAY);
                String time = (hour < 10 ? "0" : "") + hour + ":00";
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

                forecasts.add(new Weather(day, time, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed, current_weather.sunrise(), current_weather.sunset(), icon));
            }


        }
        catch (IOException e) {
            e.printStackTrace();
        }

        checkAQIAndSendEmail(airQualityIndex);
        return forecasts;
    }
}
