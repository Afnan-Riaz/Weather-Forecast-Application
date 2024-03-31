package com.weatherapp.Records;

public record Pollution(String day, String time,
                        int airQualityIndex,
                        double carbonMonoxide,
                        double nitrogenMonoxide,
                        double nitrogenDioxide,
                        double ozone,
                        double sulphurDioxide,
                        double ammonia,
                        double particulateMatterPM25,
                        double particulateMatterPM10) {
}
