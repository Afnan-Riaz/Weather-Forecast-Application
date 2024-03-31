package com.weatherapp.Models;

import com.weatherapp.HelpingClasses.CacheManagement;

import java.time.LocalTime;
import java.util.List;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
public class FileHandling implements CacheManagement {

    private static final String CACHE_FILE_PATH = "src/main/cache/weather_data.txt";
    @Override
    public void insertWeatherData(String cityName, String day, String formattedDate, String time, String startingTime,
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
                weatherDataBuilder.append(cityName).append(";")
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
    public List<ForecastWithPollution> getWeatherFromDb(String cityName, String startDate) {
        List<ForecastWithPollution> forecasts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CACHE_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into parts
                String[] parts = line.split(";");
                if (parts.length >= 24) {
                    String fileCityName = parts[0].trim();
                    if (fileCityName.equals(cityName)) {
                        String day = parts[1].trim();
                        String formattedDate = parts[2].trim();
                        String time = parts[3].trim();
                        String startingTime = parts[4].trim();
                        int temperature = Integer.parseInt(parts[5].trim());
                        String description = parts[6].trim();
                        int humidity = Integer.parseInt(parts[7].trim());
                        int pressure = Integer.parseInt(parts[8].trim());
                        int tempMax = Integer.parseInt(parts[9].trim());
                        int tempMin = Integer.parseInt(parts[10].trim());
                        int feelsLike = Integer.parseInt(parts[11].trim());
                        double windSpeed = Double.parseDouble(parts[12].trim());
                        String icon = parts[22].trim();

                        int airQualityIndex = Integer.parseInt(parts[13].trim());
                        double carbonMonoxide = Double.parseDouble(parts[14].trim());
                        double nitrogenMonoxide = Double.parseDouble(parts[15].trim());
                        double nitrogenDioxide = Double.parseDouble(parts[16].trim());
                        double ozone = Double.parseDouble(parts[17].trim());
                        double sulphurDioxide = Double.parseDouble(parts[18].trim());
                        double ammonia = Double.parseDouble(parts[19].trim());
                        double particulateMatterPM25 = Double.parseDouble(parts[20].trim());
                        double particulateMatterPM10 = Double.parseDouble(parts[21].trim());

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
    public boolean CheckExistance(String cityName, String date, String time) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE_PATH))) {
            String line;
            System.out.println("\nChecking data in file.");
            if ((line = reader.readLine()) != null) {
                System.out.println("\nFile foumd.");
                String[] parts = line.split(";");
                System.out.print(parts[0]+parts[1]+cityName);
                if (parts.length >= 4 && parts[0].equals(cityName)) {
                    String existingDate = parts[2];
                    String existingTime = parts[3];
                    System.out.println("\nNow comparing date and time.\nExisting date/time: "+existingDate+date+time+existingTime);
                    if (!dateAndTimeMatches(LocalTime.parse(existingTime), time, existingDate, date)) {
                        deleteWeatherData(cityName);
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
                    cityNames.add(parts[0]); // Assuming city name is the first element in each line
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityNames;
    }


    @Override
    public void deleteWeatherData(String cityName) {
        try {
            File inputFile = new File(CACHE_FILE_PATH);
            File tempFile = new File("src/main/cache/temp_weather_data.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = cityName;

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                // Remove the line if it matches the criteria
                if (!trimmedLine.startsWith(lineToRemove)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
            writer.close();
            reader.close();

            // Rename the temporary file to the original one
            if (!inputFile.delete()) {
                System.out.println("Could not delete file");
               return;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename file");
             return;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private static boolean dateAndTimeMatches(LocalTime startingTime, String time, String existingDate, String date) {
        if (!Objects.equals(existingDate, date)){
            LocalDate existingLocalDate = LocalDate.parse(existingDate);
            LocalDate localDate = LocalDate.parse(date);
            System.out.print(existingLocalDate);
            System.out.print(localDate);
            if (!Objects.equals(localDate.plusDays(1), existingLocalDate)){
                return false;
            }
        }
        System.out.println("Date matched.");
        LocalTime currentTime = LocalTime.parse(time);
        LocalTime twoHoursAhead = currentTime.plusHours(3); // Calculate time 2 hours ahead
        System.out.print(currentTime);
        System.out.print(twoHoursAhead);

        return !startingTime.isAfter(twoHoursAhead);
    }

    public static void main(String[] args) {
        FileHandling fileHandling = new FileHandling();
        fileHandling.deleteWeatherData("Lahore");
    }

}