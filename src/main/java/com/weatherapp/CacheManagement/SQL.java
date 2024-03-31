package com.weatherapp.CacheManagement;

import com.weatherapp.CacheManagement.CacheManagement;
import com.weatherapp.HelpingClasses.SqlConnection;
import com.weatherapp.Records.ForecastWithPollution;
import com.weatherapp.Records.Pollution;
import com.weatherapp.Records.Weather;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public  class SQL implements CacheManagement  {
@Override
    public  void insertWeatherData(String cityName, String day, String formattedDate, String time, String startingTime, int temperature, String description, int humidity, int pressure, int tempMax, int tempMin, int feelsLike, double windSpeed, int airQualityIndex, double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide, double ammonia, double particulateMatterPM25, double particulateMatterPM10, String icon) {
        String connectionUrl = SqlConnection.getConnectionUrl();

        String query = "INSERT INTO Weather (city_name, date, curr_day, time, starting_time, temperature, description, humidity, pressure, temp_max, temp_min, feels_like, wind_speed, air_quality_index, carbon_monoxide, nitrogen_monoxide, nitrogen_dioxide, ozone, sulphur_dioxide, ammonia, particulate_matter_pm25, particulate_matter_pm10, icon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, formattedDate);
            preparedStatement.setString(3, day);
            preparedStatement.setString(4, time);
            preparedStatement.setString(5, startingTime);
            preparedStatement.setInt(6, temperature);
            preparedStatement.setString(7, description);
            preparedStatement.setInt(8, humidity);
            preparedStatement.setInt(9, pressure);
            preparedStatement.setInt(10, tempMax);
            preparedStatement.setInt(11, tempMin);
            preparedStatement.setInt(12, feelsLike);
            preparedStatement.setDouble(13, windSpeed);
            preparedStatement.setInt(14, airQualityIndex);
            preparedStatement.setDouble(15, carbonMonoxide);
            preparedStatement.setDouble(16, nitrogenMonoxide);
            preparedStatement.setDouble(17, nitrogenDioxide);
            preparedStatement.setDouble(18, ozone);
            preparedStatement.setDouble(19, sulphurDioxide);
            preparedStatement.setDouble(20, ammonia);
            preparedStatement.setDouble(21, particulateMatterPM25);
            preparedStatement.setDouble(22, particulateMatterPM10);
            preparedStatement.setString(23, icon);

            int rowsInserted = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to insert weather data.");
            e.printStackTrace();
        }
    }
@Override
    public  List<ForecastWithPollution> getWeatherFromDb(String cityName, String startDate) {
        List<ForecastWithPollution> forecasts = new ArrayList<>();
        System.out.println("Getting Data from SQL.");
        String connectionUrl = SqlConnection.getConnectionUrl();
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            for (int i = 0; i < 5; i++) {
                String query = "SELECT * FROM Weather WHERE city_name = ? AND date = ?";
                String currentDate = addDays(startDate, i);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, cityName);
                    preparedStatement.setString(2, currentDate);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String day = resultSet.getString("curr_day");
                        String formattedDate = resultSet.getString("date");
                        String time = resultSet.getString("time");
                        int temperature = resultSet.getInt("temperature");
                        String description = resultSet.getString("description");
                        int humidity = resultSet.getInt("humidity");
                        int pressure = resultSet.getInt("pressure");
                        int tempMax = resultSet.getInt("temp_max");
                        int tempMin = resultSet.getInt("temp_min");
                        int feelsLike = resultSet.getInt("feels_like");
                        double windSpeed = resultSet.getDouble("wind_speed");
                        int airQualityIndex = resultSet.getInt("air_quality_index");
                        double carbonMonoxide = resultSet.getDouble("carbon_monoxide");
                        double nitrogenMonoxide = resultSet.getDouble("nitrogen_monoxide");
                        double nitrogenDioxide = resultSet.getDouble("nitrogen_dioxide");
                        double ozone = resultSet.getDouble("ozone");
                        double sulphurDioxide = resultSet.getDouble("sulphur_dioxide");
                        double ammonia = resultSet.getDouble("ammonia");
                        double particulateMatterPM25 = resultSet.getDouble("particulate_matter_pm25");
                        double particulateMatterPM10 = resultSet.getDouble("particulate_matter_pm10");
                        String icon = resultSet.getString("icon");

                        Weather weather = new Weather(day, formattedDate, time, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed, 0, 0, icon, 10, 0, 0);
                        Pollution pollution = new Pollution(day, time, airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
                                particulateMatterPM25, particulateMatterPM10);

                        forecasts.add(new ForecastWithPollution(weather, pollution));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch forecasts from the database.");
            e.printStackTrace();
        }

        return forecasts;
    }

    private static String addDays(String startDate, int days) {
        LocalDate date = LocalDate.parse(startDate);
        date = date.plusDays(days);
        return date.toString();
    }
    @Override
    public boolean CheckExistance(String cityName, String date, String time) {
        String connectionUrl = SqlConnection.getConnectionUrl();
        String query = "SELECT * FROM Weather WHERE city_name = ?";
        boolean dataExists = false;

        try (Connection connection = DriverManager.getConnection(connectionUrl);

            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cityName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String existingDate = resultSet.getString("date");
                String startingTimeStr = resultSet.getString("starting_time");
                LocalTime startingTime = LocalTime.parse(startingTimeStr);
                if (!dateAndTimeMatches(startingTime, time, existingDate, date)) {
                    deleteWeatherData(cityName);
                    return false;
                }
                else {
                    dataExists = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to check data existence in the database.");
            e.printStackTrace();
        }

        return dataExists;
    }

    @Override
    public  void deleteWeatherData(String cityName) {
        String connectionUrl = SqlConnection.getConnectionUrl();

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                String query = "DELETE FROM Weather WHERE city_name = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, cityName);
                    int rowsDeleted = preparedStatement.executeUpdate();
//                    if (rowsDeleted > 0) {
//                        System.out.println(rowsDeleted+" rows deleted " );
//                    }
                }

        } catch (SQLException e) {
            System.out.println("Failed to delete weather data.");
            e.printStackTrace();
        }
    }
    public Set<String> getAllCityNames() {
        Set<String> cityNames = new HashSet<>();
        String connectionUrl = SqlConnection.getConnectionUrl();
        String query = "SELECT DISTINCT city_name FROM Weather";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String cityName = resultSet.getString("city_name");
                cityNames.add(cityName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch city names from the database.");
            e.printStackTrace();
        }

        return cityNames;
    }

//    @Override
//    public void cleanDatabase() {
//        String currentDate = getCurrentDate();
//        String connectionUrl = SqlConnection.getConnectionUrl();
//
//        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
//            String query = "DELETE FROM Weather WHERE date <> ?;";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//                preparedStatement.setString(1, currentDate);
//                int rowsDeleted = preparedStatement.executeUpdate();
//                if (rowsDeleted > 0) {
//                    System.out.println(rowsDeleted+" rows deleted " );
//                }
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Failed to clean weather data.");
//            e.printStackTrace();
//        }
//    }

    private static boolean dateAndTimeMatches(LocalTime startingTime, String time, String existingDate, String date) {
        if (!Objects.equals(existingDate, date)){
            LocalDate existingLocalDate = LocalDate.parse(existingDate);
            LocalDate localDate = LocalDate.parse(date);
//            System.out.print(existingLocalDate);
//            System.out.print(localDate);
            if (!Objects.equals(localDate.plusDays(1), existingLocalDate)){
                return false;
            }
        }
//        System.out.println("Date matched.");
        LocalTime currentTime = LocalTime.parse(time);
        LocalTime threeHoursAhead = currentTime.plusHours(3);

        return !startingTime.isAfter(threeHoursAhead);
    }
    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }
}
