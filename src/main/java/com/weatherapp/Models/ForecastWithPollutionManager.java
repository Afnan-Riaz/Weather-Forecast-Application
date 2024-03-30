package com.weatherapp.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForecastWithPollutionManager {

    private final String city;
    private final String apiKey;
    private final String IPAddress;
    public Weather current_weather;
    public List<Weather> weather_data;
    public List<Pollution> pollution_data;
    public List<ForecastWithPollution> forecasts;

    public ForecastWithPollutionManager(String city, String apiKey, String ipAddress){
        this.city = city;
        this.apiKey = apiKey;
        this.IPAddress = ipAddress;
    }

    public List<ForecastWithPollution> ReturnWeatherForecasts() {
        forecasts = new ArrayList<>();
        CurrentWeatherLoader currentWeatherLoader = new CurrentWeatherLoader(city, apiKey);
        current_weather = currentWeatherLoader.LoadCurrentWeather();

//        try {
//            if (city == null) {
//                JsonNode response = readJsonFromUrl("https://api.openweathermap.org/geo/1.0/reverse?lat=" + lat + "&lon=" + lon + "&limit=1&appid=" + ApiKey);
//                cityName = response.get("name").asText();
//            }
            if (checkExistance_inDb(city, IPAddress, "file")) {
                System.out.println("\nData found in database.");
                FileHandling fileHandling = new FileHandling();
                // Starting time exists in the database, fetch forecasts from the database

                List<ForecastWithPollution> forecasts2 = fileHandling.getWeatherFromDb(IPAddress, city, getCurrentDate());
                ForecastWithPollution firstForecast = forecasts2.getFirst();

                if (Objects.equals(firstForecast.time(), current_weather.time()))
                    forecasts = forecasts2;
                else {
                    forecasts.add(new ForecastWithPollution(current_weather, new Pollution(current_weather.day(), current_weather.time(), firstForecast.airQualityIndex(), firstForecast.carbonMonoxide(), firstForecast.nitrogenMonoxide(), firstForecast.nitrogenDioxide(), firstForecast.ozone(), firstForecast.sulphurDioxide(), firstForecast.ammonia(), firstForecast.particulateMatterPM25(), firstForecast.particulateMatterPM10())));
                    forecasts.addAll(forecasts2);
                }

                System.out.println("\nData Loaded: ");
                System.out.print(forecasts);
            }
            else {
                System.out.println("\nGetting data from API.");

                String icon = current_weather.icon();
                double lat = currentWeatherLoader.lat;
                double lon = currentWeatherLoader.lon;

                WeatherLoader weatherLoader = new WeatherLoader(apiKey, lat, lon, current_weather, city);
                weather_data = weatherLoader.LoadWeatherData();

                PollutionLoader pollutionLoader = new PollutionLoader(apiKey, lat, lon);
                pollution_data = pollutionLoader.LoadPollutionData();

                ForecastWithPollution firstForecast = new ForecastWithPollution(current_weather, pollution_data.getFirst());
                forecasts.add(firstForecast);

                String StartingTime = null;
                int weatherArraySize = weather_data.size();

                for (int i=0; i < weatherArraySize; i++) {
                    Weather weather = weather_data.get(i);
                    if (i == 0) {
                        StartingTime = weather.time();
                    }
                    Pollution matchingAirPollutionData = null;
                    for (Pollution pollution : pollution_data) {
                        if (Objects.equals(pollution.day(), weather.day())) {
                            if (Objects.equals(pollution.time(), weather.time())) {
                                matchingAirPollutionData = pollution;
                                break;
                            }
                        }
                    }

//                     Currently the Data will go into both, SQL DB and File. Fix it accordingly:


                    if (matchingAirPollutionData != null) {
                        forecasts.add(new ForecastWithPollution(weather, matchingAirPollutionData));

                        // Storing in File:


                        // Storing in Database:
                        Insert_intoDb("file", IPAddress, city, weather.day(), weather.date(), weather.time(), StartingTime, weather.temperature(), weather.description(), weather.humidity(), weather.pressure(), weather.tempMax(), weather.tempMin(), weather.feelsLike(), weather.windSpeed(),
                                matchingAirPollutionData.airQualityIndex(), matchingAirPollutionData.carbonMonoxide(), matchingAirPollutionData.nitrogenMonoxide(), matchingAirPollutionData.nitrogenDioxide(), matchingAirPollutionData.ozone(), matchingAirPollutionData.sulphurDioxide(), matchingAirPollutionData.ammonia(),
                                matchingAirPollutionData.particulateMatterPM25(), matchingAirPollutionData.particulateMatterPM10(), icon);
                    }
                }

            }
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }

//        System.out.print("\nCurrent Weather:");
//        System.out.print(current_weather);
//        System.out.print("\nForecasts:");
//        System.out.print(forecasts);
//        System.out.print("\nPolution Data:");
//        System.out.print(pollution_data);

        return forecasts;
    }

    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        return (hour < 10 ? "0" : "") + hour + ":00";
    }

    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }
    public static boolean checkExistance_inDb(String cityName, String ipAddress, String dbType) {
        if (Objects.equals(dbType, "sql")) {
            SQL sql = new SQL();
            return sql.CheckExistance(ipAddress, cityName, getCurrentDate(), getCurrentTime());
        }
        else if(Objects.equals(dbType, "file")){
            FileHandling fileHandling = new FileHandling();
            return fileHandling.CheckExistance(ipAddress, cityName, getCurrentDate(), getCurrentTime());
        }
        return false;
    }

    public static void Insert_intoDb(String dbType, String ipAddress, String cityName, String day, String formattedDate, String time, String startingTime, int temperature, String description, int humidity, int pressure, int tempMax, int tempMin, int feelsLike, double windSpeed, int airQualityIndex, double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide, double ammonia, double particulateMatterPM25, double particulateMatterPM10, String icon) {
        if (Objects.equals(dbType, "sql")) {
            SQL sql = new SQL();
            sql.insertWeatherData(ipAddress, cityName, day, formattedDate, time, startingTime, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed,
                    airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
                    particulateMatterPM25, particulateMatterPM10, icon);
        }
        else if (Objects.equals(dbType, "file")){
            FileHandling fileHandling = new FileHandling();
            fileHandling.insertWeatherData(ipAddress, cityName, day, formattedDate, time, startingTime, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed,
                    airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
                    particulateMatterPM25, particulateMatterPM10, icon);
        }

    }
}
