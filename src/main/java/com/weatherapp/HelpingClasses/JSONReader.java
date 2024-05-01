package com.weatherapp.HelpingClasses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JSONReader {
    public static JsonNode readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
        catch (IOException e) {
            System.err.println("Please Check Your Internet Connection and Try Again!");
            System.exit(504);
            return null;
        }
    }
}
