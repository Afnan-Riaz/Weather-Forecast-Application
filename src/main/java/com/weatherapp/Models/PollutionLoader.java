package com.weatherapp.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollutionLoader {
    public final String ApiKey;
    public List<Pollution> pollution_data;
    public double lat;
    public double lon;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public PollutionLoader(String apiKey, double lat, double lon) {
        this.ApiKey = apiKey;
        this.lat = lat;
        this.lon = lon;
    }

    public JsonNode readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }

    public void checkAQIAndSendEmail(int aqi) {
        if (aqi >= 4) {
            String body = "The Air Quality Index (AQI) is currently " + aqi + ", which means air quality is very poor. Please take necessary precautions.";
            // System.out.print(aqi);
            executorService.submit(new EmailTask(body));
        }
    }

    public List<Pollution> LoadPollutionData() {
        pollution_data = new ArrayList<>();
        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        try {
            JsonNode airPollutionData = readJsonFromUrl("https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + ApiKey);
            JsonNode airPollutionList = airPollutionData.get("list");

//            System.out.print("\nPolution List:");
//            System.out.print(airPollutionList);

            for (JsonNode pollution : airPollutionList) {
                c.setTimeInMillis(pollution.get("dt").asLong() * 1000);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                String time = (hour < 10 ? "0" : "") + hour + ":00";
                String day = df2.format(c.getTime());
                double carbonMonoxide = pollution.get("components").get("co").asDouble();
                double nitrogenMonoxide = pollution.get("components").get("no").asDouble();
                double nitrogenDioxide = pollution.get("components").get("no2").asDouble();
                double ozone = pollution.get("components").get("o3").asDouble();
                double sulphurDioxide = pollution.get("components").get("so2").asDouble();
                double ammonia = pollution.get("components").get("nh3").asDouble();
                double particulateMatterPM25 = pollution.get("components").get("pm2_5").asDouble();
                double particulateMatterPM10 = pollution.get("components").get("pm10").asDouble();
                int airQualityIndex = pollution.get("main").get("aqi").asInt();

                pollution_data.add(new Pollution(day, time, airQualityIndex, carbonMonoxide, nitrogenMonoxide, nitrogenDioxide, ozone, sulphurDioxide, ammonia,
                        particulateMatterPM25, particulateMatterPM10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int AQI = pollution_data.getFirst().airQualityIndex();
        checkAQIAndSendEmail(AQI);
        
        return pollution_data;
    }
}
