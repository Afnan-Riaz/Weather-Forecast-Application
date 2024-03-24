package com.weatherapp.Models;

public record ForecastWithPollution(String day, String time, int temperature, String description, int humidity, int pressure,
                                    int tempMax, int tempMin, int feelsLike, double windSpeed, int airQualityIndex,
                                    double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide,
                                    double ozone, double sulphurDioxide, double ammonia,
                                    double particulateMatterPM25, double particulateMatterPM10, String icon){
    public ForecastWithPollution(Weather weather, Pollution pollution) {
        this(weather.day(), weather.time(), weather.temperature(), weather.description(), weather.humidity(),
                weather.pressure(), weather.tempMax(), weather.tempMin(), weather.feelsLike(), weather.windSpeed(),
                pollution.airQualityIndex(), pollution.carbonMonoxide(), pollution.nitrogenMonoxide(),
                pollution.nitrogenDioxide(), pollution.ozone(), pollution.sulphurDioxide(), pollution.ammonia(),
                pollution.particulateMatterPM25(), pollution.particulateMatterPM10(), weather.icon());
    }
    public static ForecastWithPollution setPollutionData(Weather weather,
                                                         int airQualityIndex,
                                                         double carbonMonoxide,
                                                         double nitrogenMonoxide,
                                                         double nitrogenDioxide,
                                                         double ozone,
                                                         double sulphurDioxide,
                                                         double ammonia,
                                                         double particulateMatterPM25,
                                                         double particulateMatterPM10) {
        return new ForecastWithPollution(weather.day(), weather.time(), weather.temperature(), weather.description(), weather.humidity(),
                weather.pressure(), weather.tempMax(), weather.tempMin(), weather.feelsLike(),
                weather.windSpeed(), airQualityIndex, carbonMonoxide, nitrogenMonoxide,
                nitrogenDioxide, ozone, sulphurDioxide, ammonia, particulateMatterPM25,
                particulateMatterPM10, weather.icon());
    }
}
