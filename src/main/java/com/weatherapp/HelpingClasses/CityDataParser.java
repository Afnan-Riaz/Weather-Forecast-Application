package com.weatherapp.HelpingClasses;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CityDataParser {
// This class is showing single responsibility principle because it is only responsible for parsing city data.
    public List<String> parseCityData() throws Exception {
        List<String> cityNames = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("src/main/resources/utils/city.list.json"));

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            String cityName = (String) jsonObject.get("name");
            String countryName = (String) jsonObject.get("country");
            cityNames.add(cityName + ", " + countryName);
        }

        return cityNames;
    }
}