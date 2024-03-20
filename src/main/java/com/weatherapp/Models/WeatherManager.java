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

    public WeatherManager(String city, String apiKey) {
        this.city = city;
        this.apiKey = apiKey;
    }

    public JsonNode readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }

    public List<WeatherForecast> getWeatherForecast() {
        List<WeatherForecast> forecasts = new ArrayList<>();

        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        try {
            JsonNode currentWeatherData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey+"&units=metric");
            double lat = currentWeatherData.get("coord").get("lat").asDouble();
            double lon = currentWeatherData.get("coord").get("lon").asDouble();
            long sunrise = currentWeatherData.get("sys").get("sunrise").asLong() * 1000;
            long sunset = currentWeatherData.get("sys").get("sunset").asLong() * 1000;
            String icon = currentWeatherData.get("weather").get(0).get("icon").asText();

            JsonNode forecastData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+apiKey+"&units=metric");
            JsonNode forecastList = forecastData.get("list");

            JsonNode airPollutionData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat="+lat+"&lon="+lon+"&appid="+apiKey);
            JsonNode airPollutionList = airPollutionData.get("list");

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
                    forecasts.add(new WeatherForecast(day, time, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed,
                            airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
                            particulateMatterPM25, particulateMatterPM10,sunrise,sunset,icon));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return forecasts;
    }

//    public static void main(String[] args) {
//        WeatherManager weatherManager = new WeatherManager("Lahore,PK", "9804f15edc7893ea4947a7526edfc496");
//        List<WeatherForecast> forecasts = weatherManager.getWeatherForecast();
//
//        for (WeatherForecast forecast : forecasts) {
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
//            System.out.println("Sunrise: " + new SimpleDateFormat("HH:mm").format(new Date(forecast.sunrise * 1000)));
//            System.out.println("Sunset: " + new SimpleDateFormat("HH:mm").format(new Date(forecast.sunset * 1000)));
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

    public record WeatherForecast(String day, String time, int temperature, String description, int humidity, int pressure,
                                  int tempMax, int tempMin, int feelsLike, double windSpeed, int airQualityIndex,
                                  double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide,
                                  double ozone, double sulphurDioxide, double ammonia,
                                  double particulateMatterPM25, double particulateMatterPM10,long sunrise, long sunset,String icon) {}
}
