package com.weatherapp.HelpingClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Db_actions {
    public static void insertWeatherData(String cityName, String date, String currDay, String time, int temperature, String description, int humidity, int pressure, int tempMax, int tempMin, int feelsLike, double windSpeed, long sunrise, long sunset, String icon, int airQualityIndex, double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide, double ammonia, double particulateMatterPM25, double particulateMatterPM10) {
        String connectionUrl = SqlConnection.getConnectionUrl();

        String query = "INSERT INTO Weather (city_name, date, curr_day, time, temperature, description, humidity, pressure, temp_max, temp_min, feels_like, wind_speed, sunrise, sunset, icon, air_quality_index, carbon_monoxide, nitrogen_monoxide, nitrogen_dioxide, ozone, sulphur_dioxide, ammonia, particulate_matter_pm25, particulate_matter_pm10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, currDay);
            preparedStatement.setString(4, time);
            preparedStatement.setInt(5, temperature);
            preparedStatement.setString(6, description);
            preparedStatement.setInt(7, humidity);
            preparedStatement.setInt(8, pressure);
            preparedStatement.setInt(9, tempMax);
            preparedStatement.setInt(10, tempMin);
            preparedStatement.setInt(11, feelsLike);
            preparedStatement.setDouble(12, windSpeed);
            preparedStatement.setLong(13, sunrise);
            preparedStatement.setLong(14, sunset);
            preparedStatement.setString(15, icon);
            preparedStatement.setInt(16, airQualityIndex);
            preparedStatement.setDouble(17, carbonMonoxide);
            preparedStatement.setDouble(18, nitrogenMonoxide);
            preparedStatement.setDouble(19, nitrogenDioxide);
            preparedStatement.setDouble(20, ozone);
            preparedStatement.setDouble(21, sulphurDioxide);
            preparedStatement.setDouble(22, ammonia);
            preparedStatement.setDouble(23, particulateMatterPM25);
            preparedStatement.setDouble(24, particulateMatterPM10);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Weather data inserted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert weather data.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage
        insertWeatherData("New York", "2024-03-24", "Monday", "12:00", 20, "Sunny", 50, 1013, 25, 18, 22, 5.5, 1616544000, 1616587200, "sunny.png", 50, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9);
    }
}
