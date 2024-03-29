package com.weatherapp.Models;

import com.weatherapp.HelpingClasses.CacheManagement;

import java.util.List;
import java.io.*;
import java.util.*;

public class FileHandling implements CacheManagement {

    private static final String CACHE_FILE_PATH = "src/main/cache/weather_data.txt";
    @Override
    public void insertWeatherData(String ipAddress ,String cityName, String day, String formattedDate, String time, String startingTime,
                                  int temperature, String description, int humidity, int pressure, int tempMax, int tempMin,
                                  int feelsLike, double windSpeed, int airQualityIndex, double carbonMonoxide,
                                  double nitrogenMonoxide, double nitrogenDioxide, double ozone, double sulphurDioxide,
                                  double ammonia, double particulateMatterPM25, double particulateMatterPM10, String icon) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE_PATH, true))) {

//            WeatherManager weatherManager = new WeatherManager(cityName, "9804f15edc7893ea4947a7526edfc496");
//            List<ForecastWithPollution> forecasts = weatherManager.getWeatherForecast();
//            Weather currentWeather = weatherManager.current_weather;

//            if (!forecasts.isEmpty()) {
//                ForecastWithPollution currentForecast = forecasts.get(0);

                StringBuilder weatherDataBuilder = new StringBuilder();
                weatherDataBuilder.append(cityName).append(",")
                        .append(day).append(",")
                        .append(formattedDate).append(",")
                        .append(time).append(",")
                        .append(startingTime).append(",")
                        .append(temperature).append(",")
                        .append(description).append(",")
                        .append(humidity).append(",")
                        .append(pressure).append(",")
                        .append(tempMax).append(",")
                        .append(tempMin).append(",")
                        .append(feelsLike).append(",")
                        .append(windSpeed).append(",")
                        .append(airQualityIndex).append(",")
                        .append(carbonMonoxide).append(",")
                        .append(nitrogenMonoxide).append(",")
                        .append(nitrogenDioxide).append(",")
                        .append(ozone).append(",")
                        .append(sulphurDioxide).append(",")
                        .append(ammonia).append(",")
                        .append(particulateMatterPM25).append(",")
                        .append(particulateMatterPM10).append(",")
                        .append(icon).append(",");

                writer.write(weatherDataBuilder.toString());
                writer.newLine();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ForecastWithPollution> getWeatherFromDb(String ipAddress, String cityName, String startDate) {
        List<ForecastWithPollution> forecasts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(cityName)) {
                    String day = parts[1];
                    String formattedDate = parts[2];
                    String time = parts[3];
                    int temperature = Integer.parseInt(parts[5]);
                    String description = parts[6];
                    int humidity = Integer.parseInt(parts[7]);
                    int pressure = Integer.parseInt(parts[8]);
                    int tempMax = Integer.parseInt(parts[9]);
                    int tempMin = Integer.parseInt(parts[10]);
                    int feelsLike = Integer.parseInt(parts[11]);
                    double windSpeed = Double.parseDouble(parts[12]);
                    int airQualityIndex = Integer.parseInt(parts[13]);
                    double carbonMonoxide = Double.parseDouble(parts[14]);
                    double nitrogenMonoxide = Double.parseDouble(parts[15]);
                    double nitrogenDioxide = Double.parseDouble(parts[16]);
                    double ozone = Double.parseDouble(parts[17]);
                    double sulphurDioxide = Double.parseDouble(parts[18]);
                    double ammonia = Double.parseDouble(parts[19]);
                    double particulateMatterPM25 = Double.parseDouble(parts[20]);
                    double particulateMatterPM10 = Double.parseDouble(parts[21]);
                    String icon = parts[22];

                    Weather weather = new Weather(day, formattedDate, time, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed, 0, 0, icon, 10, 0, 0);
                    Pollution pollution = new Pollution(day, time, airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
                            particulateMatterPM25, particulateMatterPM10);

                    forecasts.add(new ForecastWithPollution(weather, pollution));
                }
            }
            System.out.println("Got data from file.");
        } catch (IOException e) {
            System.out.println("Failed to read forecasts from the file.");
            e.printStackTrace();
        }

        return forecasts;
    }

    @Override
    public boolean CheckExistance(String ipAddress, String cityName, String date, String time) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE_PATH))) {
            String line;
            System.out.println(date);
            System.out.println(time);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(cityName) && parts[2].equals(date) && parts[3].equals(time)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Set<String> getAllCityNames() {
        Set<String> cityNames = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    cityNames.add(parts[0]); // Assuming city name is the first element in each line
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityNames;
    }

    @Override
    public void deleteWeatherData(String cityName, String ipAddress) {
        try {
            File inputFile = new File(CACHE_FILE_PATH);
            File tempFile = new File("src/main/cache/temp_weather_data.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = cityName + ",";
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                // Remove the line if it contains the cityName
                if (currentLine.contains(lineToRemove)) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            // Delete the original file
            if (!inputFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            // Rename the temp file to the original file name
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public static void main(String[] args) {
        FileHandling fileHandling = new FileHandling();
        fileHandling.insertWeatherData("New York", "Monday", "2024-03-24", "15:00", "11:00", 20, "Sunny", 50, 1013, 25, 18, 22, 5.5, 50, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, "sunny.png");
        fileHandling.insertWeatherData("New York", "Monday", "2024-03-24", "18:00", "11:00", 20, "Sunny", 50, 1013, 25, 18, 22, 5.5, 50, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, "sunny.png");
        fileHandling.insertWeatherData("Miami", "Monday", "2024-03-24", "21:00", "11:00", 20, "Sunny", 50, 1013, 25, 18, 22, 5.5, 50, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, "sunny.png");
        fileHandling.insertWeatherData("Lahore", "Monday", "2024-03-24", "24:00", "11:00", 20, "Sunny", 50, 1013, 25, 18, 22, 5.5, 50, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, "sunny.png");
        fileHandling.deleteWeatherData("New York");
        List<WeatherForecast> forecasts = fileHandling.getWeatherFromDb("New York", getCurrentDate());

        Set<String> distinctCityNames = getAllCityNames();
        System.out.println("Distinct City Names:");
        for (String cityName : distinctCityNames) {
            System.out.println(cityName);
        }
        System.out.println(fileHandling.CheckExistance("New York",getCurrentDate(),getCurrentTime()));
    }
    */

}