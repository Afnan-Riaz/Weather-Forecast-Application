package com.weatherapp.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.HelpingClasses.JSONReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GeoCoder {
    private final String ApiKey;
    private double lat;
    private double lon;

    public GeoCoder(String apiKey, double lat, double lon) {
        ApiKey = apiKey;
        this.lat = lat;
        this.lon = lon;
    }
    public String getCity() {
        try
        {
            JsonNode response = JSONReader.readJsonFromUrl("https://api.openweathermap.org/geo/1.0/reverse?lat="+lat+"&lon="+lon+"&limit=1&appid="+ApiKey);
            if (!response.isEmpty()) {
                return response.get(0).get("name").asText();
                //+ ", " + response.get(0).get("country").asText();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
