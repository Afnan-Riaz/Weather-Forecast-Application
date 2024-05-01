package com.weatherapp.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.HelpingClasses.JSONReader;
import com.weatherapp.Records.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.*;

public class WeatherLoader {
    public final String ApiKey;
    public List<Weather> forecasts;
    public double lat;
    public double lon;
    public Weather current_weather;
    public String StartingTime;
    public String cityName;

    public WeatherLoader(String apiKey, double lat, double lon, Weather current_weather, String cityName) {
        this.ApiKey = apiKey;
        this.lat = lat;
        this.lon = lon;
        this.current_weather = current_weather;
        this.cityName = cityName != null ? cityName : null;
    }

    public List<Weather> LoadWeatherData() {
        forecasts = new ArrayList<>();
        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        try {
            JsonNode forecastData = JSONReader.readJsonFromUrl("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + ApiKey + "&units=metric");
            JsonNode forecastList = forecastData.get("list");

            for (int i = 0; i < forecastList.size(); i++) {
                JsonNode forecast = forecastList.get(i);

                c.setTimeInMillis(forecast.get("dt").asLong() * 1000);

                int hour = c.get(Calendar.HOUR_OF_DAY);
                String time = (hour < 10 ? "0" : "") + hour + ":00";
                if (i == 0) {
                    StartingTime = time;
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
                int visibility = forecast.has("visibility") ? forecast.get("visibility").asInt() : -1;
                int rain = forecast.has("rain") ? forecast.get("rain").asInt() : -1;
                int snow = forecast.has("snow") ? forecast.get("snow").asInt() : -1;

                long dt = forecast.get("dt").asLong() * 1000;
                Date date = new Date(dt);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(date);

                forecasts.add(new Weather(day, formattedDate, time, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed, current_weather.sunrise(), current_weather.sunset(), icon, visibility, rain, snow));
            }
        }
        catch (UnknownHostException e){
            System.err.println("Please Check Your Internet Connection and Try Again!");
            System.exit(504);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        return forecasts;
    }
}





