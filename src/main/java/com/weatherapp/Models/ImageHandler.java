package com.weatherapp.Models;

public class ImageHandler {

    public static String getImage(String icon) {
        return switch (icon) {
            case "01n","01d","02d","02n"  -> "images/img_cloudy.jpg";
            case "03d", "03n","04d", "04n" -> "images/img_sunset.jpg";

            case "09d", "09n","10n","10d" -> "images/rain-bg1.jpg";


            default -> "images/img_sunset.jpg";
        };
    }

}