package com.weatherapp.Models;

import com.weatherapp.HelpingClasses.SqlConnection;
import com.weatherapp.Models.WeatherForecast;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.weatherapp.HelpingClasses.CacheManagement;
public  class SQL implements CacheManagement  {
@Override
    public  void insertWeatherData(String ipAddress,String cityName, String day, String formattedDate, String time, String startingTime, int temperature, String description, int humidity, int pressure, int tempMax, int tempMin, int feelsLike, double windSpeed, int airQualityIndex, double carbonMonoxide, double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide, double ammonia, double particulateMatterPM25, double particulateMatterPM10, String icon) {
        String connectionUrl = SqlConnection.getConnectionUrl();

        String query = "INSERT INTO Weather (city_name, date, curr_day, time, starting_time, temperature, description, humidity, pressure, temp_max, temp_min, feels_like, wind_speed, air_quality_index, carbon_monoxide, nitrogen_monoxide, nitrogen_dioxide, ozone, sulphur_dioxide, ammonia, particulate_matter_pm25, particulate_matter_pm10, icon,IP_Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

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
            preparedStatement.setString(24, ipAddress);
            int rowsInserted = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to insert weather data.");
            e.printStackTrace();
        }
    }
@Override
    public  List<ForecastWithPollution> getWeatherFromDb(String ipAddress,String cityName, String startDate) {
        List<ForecastWithPollution> forecasts = new ArrayList<>();
        String connectionUrl = SqlConnection.getConnectionUrl();

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            for (int i = 0; i < 5; i++) {
                String query = "SELECT * FROM Weather WHERE city_name = ? AND IP_Address=? AND date = ?";
                String currentDate = addDays(startDate, i);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, cityName);
                    preparedStatement.setString(2, ipAddress);
                    preparedStatement.setString(3, currentDate);

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
                System.out.println("Got data from database.");
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
    public boolean CheckExistance(String ipAddress,String cityName, String date, String time) {
        String connectionUrl = SqlConnection.getConnectionUrl();
        String query = "SELECT * FROM Weather WHERE city_name = ? AND IP_Address=?";
        boolean dataExists = false;

        try (Connection connection = DriverManager.getConnection(connectionUrl);

            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, ipAddress);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String existingDate = resultSet.getString("date");
                String startingTimeStr = resultSet.getString("starting_time");
                LocalTime startingTime = LocalTime.parse(startingTimeStr);
//                System.out.print(existingDate);
//                System.out.print(startingTime);
//                System.out.print(time);
//                System.out.print(date);
                if (!date.equals(existingDate)) {
                    deleteWeatherData(cityName,ipAddress);
                    dataExists = false;

                }
                else if (!timeMatches(startingTime, time)) {
                    deleteWeatherData(cityName,ipAddress);
                    System.out.print("Time doesnot match.");
                    dataExists = false;
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
    public  void deleteWeatherData(String cityName,String ipAddress) {
        String connectionUrl = SqlConnection.getConnectionUrl();

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                String query = "DELETE FROM Weather WHERE city_name = ? AND IP_Address=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, cityName);
                    preparedStatement.setString(2, ipAddress);
                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println(rowsDeleted+" rows deleted " );
                    }
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

    private static boolean timeMatches(LocalTime startingTime, String time) {

        LocalTime currentTime = LocalTime.parse(time);
        LocalTime endTimeMargin = startingTime;
        startingTime=startingTime.minusHours(3);
        System.out.println(startingTime+time+endTimeMargin);
        return !currentTime.isAfter(endTimeMargin) && !currentTime.isBefore(startingTime);
    }


    public static void main(String[] args) {
        // Example usage
        SQL sql = new SQL();
        boolean exists = sql.CheckExistance("1234","Lahore", "2024-03-28", "02:01");
        System.out.println("Data exists within 3-hour margin: " + exists);
    }


//    public static void main(String[] args) {
//        // Example usage
//        insertWeatherData("New York", "Monday", "2024-03-24", "12:00", "11:00", 20, "Sunny", 50, 1013, 25, 18, 22, 5.5, 50, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, "sunny.png");
//    }
}
