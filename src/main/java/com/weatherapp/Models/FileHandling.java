package com.weatherapp.Models;

import com.weatherapp.HelpingClasses.CacheManagement;

import java.time.LocalTime;
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
                weatherDataBuilder.append(ipAddress).append(";")
                        .append(cityName).append(";")
                        .append(day).append(";")
                        .append(formattedDate).append(";")
                        .append(time).append(";")
                        .append(startingTime).append(";")
                        .append(temperature).append(";")
                        .append(description).append(";")
                        .append(humidity).append(";")
                        .append(pressure).append(";")
                        .append(tempMax).append(";")
                        .append(tempMin).append(";")
                        .append(feelsLike).append(";")
                        .append(windSpeed).append(";")
                        .append(airQualityIndex).append(";")
                        .append(carbonMonoxide).append(";")
                        .append(nitrogenMonoxide).append(";")
                        .append(nitrogenDioxide).append(";")
                        .append(ozone).append(";")
                        .append(sulphurDioxide).append(";")
                        .append(ammonia).append(";")
                        .append(particulateMatterPM25).append(";")
                        .append(particulateMatterPM10).append(";")
                        .append(icon).append(";");

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

        try (BufferedReader br = new BufferedReader(new FileReader(CACHE_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into parts
                String[] parts = line.split(";");
                if (parts.length >= 24) {
                    String fileIpAddress = parts[0].trim();
                    String fileCityName = parts[1].trim();
                    if (fileIpAddress.equals(ipAddress) && fileCityName.equals(cityName)) {
                        // Extract weather data
                        String day = parts[2].trim();
                        String formattedDate = parts[3].trim();
                        String time = parts[4].trim();
                        String startingTime = parts[5].trim(); // Adding startingTime
                        int temperature = Integer.parseInt(parts[6].trim());
                        String description = parts[7].trim();
                        int humidity = Integer.parseInt(parts[8].trim());
                        int pressure = Integer.parseInt(parts[9].trim());
                        int tempMax = Integer.parseInt(parts[10].trim());
                        int tempMin = Integer.parseInt(parts[11].trim());
                        int feelsLike = Integer.parseInt(parts[12].trim());
                        double windSpeed = Double.parseDouble(parts[13].trim());
                        String icon = parts[23].trim();

                        // Extract pollution data
                        int airQualityIndex = Integer.parseInt(parts[14].trim());
                        double carbonMonoxide = Double.parseDouble(parts[15].trim());
                        double nitrogenMonoxide = Double.parseDouble(parts[16].trim());
                        double nitrogenDioxide = Double.parseDouble(parts[17].trim());
                        double ozone = Double.parseDouble(parts[18].trim());
                        double sulphurDioxide = Double.parseDouble(parts[19].trim());
                        double ammonia = Double.parseDouble(parts[20].trim());
                        double particulateMatterPM25 = Double.parseDouble(parts[21].trim());
                        double particulateMatterPM10 = Double.parseDouble(parts[22].trim());

                        // Create Weather and Pollution objects
                        Weather weather = new Weather(day, formattedDate, time, temperature, description, humidity, pressure, tempMax, tempMin, feelsLike, windSpeed, 0, 0, icon, 10, 0, 0);
                        Pollution pollution = new Pollution(day, time, airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia, particulateMatterPM25, particulateMatterPM10);

                        // Add ForecastWithPollution object to the list
                        forecasts.add(new ForecastWithPollution(weather, pollution));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nForecasts from file: ");
        System.out.print(forecasts);
        return forecasts;
    }

    @Override
    public boolean CheckExistance(String ipAddress, String cityName, String date, String time) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE_PATH))) {
            String line;
            System.out.println("\nChecking data in file.");
            if ((line = reader.readLine()) != null) {
                System.out.println("\nFile foumd.");
                String[] parts = line.split(";");
                System.out.print(parts[0]+parts[1]+cityName);
                if (parts.length >= 5 && parts[1].equals(cityName) && parts[0].equals(ipAddress)) {
                    String existingDate = parts[3];
                    String existingTime = parts[4];
                    System.out.println("\nNow comparing date and time.\nExisting date/time: "+existingDate+date+time+existingTime);
                    if (!date.equals(existingDate) || !timeMatches(LocalTime.parse(existingTime), time)) {
                        deleteWeatherData(cityName,ipAddress);
                        System.out.println("inside conditon");
                        return false;

                    }
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
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    cityNames.add(parts[1]); // Assuming city name is the first element in each line
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
            String sec_lineToRemove = ipAddress + ",";
            String currentLine;
            System.out.println("deleted");
            while ((currentLine = reader.readLine()) != null) {
                // Remove the line if it contains the cityName
                if (currentLine.contains(lineToRemove)&&currentLine.contains(sec_lineToRemove)) {
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
    private static boolean timeMatches(LocalTime startingTime, String time) {
        LocalTime currentTime = LocalTime.parse(time);
        LocalTime twoHoursAhead = currentTime.plusHours(2); // Calculate time 2 hours ahead

        return !startingTime.isAfter(twoHoursAhead);
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