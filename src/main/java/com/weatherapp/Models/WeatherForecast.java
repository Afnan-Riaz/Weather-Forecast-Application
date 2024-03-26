package com.weatherapp.Models;

public record WeatherForecast(String day,String date, String time, int temperature, String description, int humidity, int pressure,
                              int tempMax, int tempMin, int feelsLike, double windSpeed, int airQualityIndex,
                              double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide,
                              double ozone, double sulphurDioxide, double ammonia,
                              double particulateMatterPM25, double particulateMatterPM10, String icon){}
