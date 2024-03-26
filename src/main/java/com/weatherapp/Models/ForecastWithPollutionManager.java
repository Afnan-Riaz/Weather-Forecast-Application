package com.weatherapp.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForecastWithPollutionManager {

    private final String city;
    private final String apiKey;
    public Weather current_weather;
    public List<Weather> weather_data;
    public List<Pollution> pollution_data;
    public List<ForecastWithPollution> forecasts;

    public ForecastWithPollutionManager(String city, String apiKey){
        this.city = city;
        this.apiKey = apiKey;
    }

    public List<ForecastWithPollution> ReturnWeatherForecasts() {
        forecasts = new ArrayList<>();

        CurrentWeatherLoader currentWeatherLoader = new CurrentWeatherLoader(city, apiKey);
        current_weather = currentWeatherLoader.LoadCurrentWeather();

        String icon = current_weather.icon();
        double lat = currentWeatherLoader.lat;
        double lon = currentWeatherLoader.lon;

        WeatherLoader weatherLoader = new WeatherLoader(apiKey, lat, lon, current_weather);
        weather_data = weatherLoader.LoadWeatherData();

        PollutionLoader pollutionLoader = new PollutionLoader(apiKey, lat, lon);
        pollution_data = pollutionLoader.LoadPollutionData();

        ForecastWithPollution firstForecast = new ForecastWithPollution(current_weather, pollution_data.getFirst());
        forecasts.add(firstForecast);

        for (Weather weather: weather_data) {
            Pollution matchingAirPollutionData = null;
            for (Pollution pollution : pollution_data) {
                if (Objects.equals(pollution.day(), weather.day())) {
                    if (Objects.equals(pollution.time(), weather.time())) {
                        matchingAirPollutionData = pollution;
                        break;
                    }
                }
            }
            if (matchingAirPollutionData != null) {
                forecasts.add(new ForecastWithPollution(weather, matchingAirPollutionData));
            }
        }

        System.out.print("\nCurrent Weather:");
        System.out.print(current_weather);
        System.out.print("\nForecasts:");
        System.out.print(forecasts);
        System.out.print("\nPolution Data:");
        System.out.print(pollution_data);

        return forecasts;
    }
}
