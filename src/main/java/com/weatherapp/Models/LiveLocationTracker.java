package com.weatherapp.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class LiveLocationTracker {
    public static String City;
    public static String Country;
    public static String CountryCode;
    public static JsonNode readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }
    public static String getLiveLocation() {
        String ipAddress = getIPAddress();

        if (Objects.equals(ipAddress, "Connection Error")){
            return "Connection Error";
        }

        String apiUrl = "http://ip-api.com/json/" + ipAddress;

        try {
            JsonNode jsonResponse = readJsonFromUrl(apiUrl);
            City = jsonResponse.get("city").asText();
            Country = jsonResponse.get("country").asText();
            CountryCode = jsonResponse.get("countryCode").asText();
            return City + ", " + CountryCode;

        } catch (IOException e) {
            e.printStackTrace();
            return "Connection Error";
        }


    }
    public static String getIPAddress() {
        try {
            URL url = new URL("https://api.ipify.org/?format=text");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ipAddress = reader.readLine();
            reader.close();
            return ipAddress;

        } catch (IOException e) {
            e.printStackTrace();
            return "Connection Error";
        }
    }
}
