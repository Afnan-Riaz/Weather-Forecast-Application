package com.weatherapp.weatherapplication;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LocationController {

    @FXML
    private TextField locationField;
    private final List<String> cityNames = new ArrayList<>();
    @FXML
    public void initialize() {
        try {
            parseCityData();
            TextFields.bindAutoCompletion(locationField, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
                @Override
                public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                    String userInput = param.getUserText().toLowerCase();
                    List<String> sortedCityNames = cityNames.stream()
                            .filter(city -> city.toLowerCase().startsWith(userInput))
                            .sorted(Comparator.comparing(city -> city.toLowerCase().startsWith(userInput) ? 0 : 1))
                            .collect(Collectors.toList());
                    return sortedCityNames;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseCityData() throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("src/main/resources/utils/city.list.json"));

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            String cityName = (String) jsonObject.get("name");
            String countryName = (String) jsonObject.get("country");
            cityNames.add(cityName + ", " + countryName);
        }
    }
}