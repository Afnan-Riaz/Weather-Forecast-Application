package com.weatherapp.HelpingClasses;
import java.sql.*;

import java.time.LocalTime;
import com.weatherapp.HelpingClasses.SqlConnection;
public class CacheManagement {

    public static boolean CheckExistance(String cityName, String date, String time) {
        String connectionUrl = SqlConnection.getConnectionUrl();

        String query = "SELECT starting_time FROM Weather WHERE city_name = ? AND date = ?";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String startingTimeStr = resultSet.getString("starting_time");
                LocalTime startingTime = LocalTime.parse(startingTimeStr);
                LocalTime currentTime = LocalTime.parse(time);

                LocalTime endTimeMargin = startingTime.plusHours(3);
                return !currentTime.isAfter(endTimeMargin) && !currentTime.isBefore(startingTime);
            }
        } catch (SQLException e) {
            System.out.println("Failed to check data existence in the database.");
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        // Example usage
        boolean exists = CheckExistance("Lahore", "2024-03-24", "15:00");
        System.out.println("Data exists within 3-hour margin: " + exists);
    }
}
