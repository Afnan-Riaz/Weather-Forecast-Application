package com.weatherapp.Models;

public record Weather(String day, String time, int temperature, String description, int humidity, int pressure,
                      int tempMax, int tempMin, int feelsLike, double windSpeed, long sunrise, long sunset, String icon) {
}
