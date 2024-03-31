package com.weatherapp.HelpingClasses;

public class ImageHandler {

    public static String getImage(String icon) {
        return switch (icon) {
            case "01n"->"images/clear-night.jpg";
            case "01d"->"images/clear-day.jpg";
            case "02d","03d","04d" -> "images/cloudy-day.jpg";
            case "02n", "03n","04n" -> "images/cloudy-night.jpg";
            case "09d", "09n","10n","10d","50d","50n" -> "images/rain.jpg";
            case "11d", "11n" -> "images/thunder.jpg";
            case "13d", "13n" -> "images/snow.jpeg";
            default -> "images/default.jpg";
        };
    }
    public static String getIcon(String icon) {
        return switch (icon) {
            case "01n" -> "icons/moon.png";
            case "01d"  -> "icons/sun.png";
            case "02d", "02n","03d", "03n", "04d", "04n" -> "icons/clouds.png";
            case "09d", "09n","10n","10d","50d","50n" -> "icons/rain.png";
            case "11d","11n" -> "icons/lightning.png";
            case "13d","13n" -> "icons/snow.png";
            default -> "icons/fog.png";
        };
    }
}