package com.weatherapp.weatherapplication;

import com.weatherapp.Models.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.weatherapp.weatherapplication.Formatter.*;

public class WeatherService {
    public static int lock = 0;
    private final String apiKey = "9804f15edc7893ea4947a7526edfc496";
    private List<ForecastWithPollution> forecasts;
    private AppController appController;
    private WeatherManager weatherManager;
    private ForecastWithPollution forecast;
    private String current_city;

    public WeatherService(AppController appController) {
        this.appController = appController;
    }
    public void hydrateUI(String current_city) {
        this.current_city = current_city;
        this.weatherManager = new WeatherManager(current_city, apiKey);
        this.forecasts = weatherManager.getWeatherForecast();
        Weather current_weather = weatherManager.current_weather;

        forecast = forecasts.getFirst();
        setImages();
        setTempBoxes(forecasts, 0, appController.temperatureBoxes);
        setOtherAttributes(forecast);
        appController.sunrise.setText(formatTime(current_weather.sunrise()));
        appController.sunset.setText(formatTime(current_weather.sunset()));
    }

    public void setTempBoxes(List<ForecastWithPollution> forecasts, int startingIndex, HBox parent){
        parent.getChildren().clear();
        int boxCount = 0;
        for (int i = startingIndex; i < forecasts.size(); i++) {
            if (boxCount >= 8) {
                break;
            }
            VBox box = createWeatherBox(forecasts.get(i));
            if(i == 0){
                box.getStyleClass().clear();
                box.getStyleClass().add("current-temp");
                HBox feelsLikeBox = new HBox();
                Text feelsLikeLabel = new Text("Feels Like ");
                feelsLikeLabel.getStyleClass().add("feels-like-label");
                Text feelsLikeText = new Text(forecasts.get(i).feelsLike() + " °C");
                feelsLikeText.getStyleClass().add("feels-like-text");
                feelsLikeBox.getChildren().addAll(feelsLikeLabel, feelsLikeText);
                box.getChildren().add(feelsLikeBox);
            }
            parent.getChildren().add(box);
            boxCount++;
        }
    }
    private void setOtherAttributes(ForecastWithPollution forecast){
        appController.city.setText(current_city);
        appController.description.setText(capitalizeFirstLetter(forecast.description()));
        appController.humidity.setText(forecast.humidity() + " %");
        appController.windspeed.setText(forecast.windSpeed() + " m/s");
        appController.temp.setText(forecast.temperature() + " °C");
        appController.day.setText(forecast.day());
        appController.pressure.setText(forecast.pressure() + " hPa");
        appController.quality.setText(getAQIDescription(forecast.airQualityIndex()));
    }
    void setImages(){
        String icon = forecast.icon();
        String imageName = ImageHandler.getImage(icon);
        String iconName=ImageHandler.getIcon(icon);
        String baseImagePath = "/styling/";
        String imageUrl = Objects.requireNonNull(getClass().getResource(baseImagePath + imageName)).toExternalForm();
        String iconUrl = Objects.requireNonNull(getClass().getResource(baseImagePath + iconName)).toExternalForm();
        appController.bgImage.setStyle("-fx-background-image: url('" + imageUrl + "');" +
                "-fx-background-size: cover; ");
        appController.weatherIcon.setStyle("-fx-image:  url('" + iconUrl + "');" +
                "-fx-background-size: cover; ");
    }
    public Dialog<Void> createPollutantInfoDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Pollutant Information");
//      dialog.setResultConverter(dialogButton -> null);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        textArea.setText(
                "CO: " + forecast.carbonMonoxide() + "%\n" +
                        "NO: " + forecast.nitrogenMonoxide() + "%\n" +
                        "NO2: " + forecast.nitrogenDioxide() + "%\n" +
                        "O3: " + forecast.ozone() + "%\n" +
                        "SO2: " + forecast.sulphurDioxide() + "%\n" +
                        "NH3: " + forecast.ammonia() + "%\n" +
                        "PM10: " + forecast.particulateMatterPM10() + "%"
        );
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> dialog.close());

        dialog.getDialogPane().setContent(textArea);
        return dialog;
    }
    private VBox createWeatherBox(ForecastWithPollution forecast) {
        VBox box = new VBox();
        box.getStyleClass().add("temp-box");
        Text timeText = new Text(convertTo12HourFormat(forecast.time()));
        timeText.getStyleClass().add("time-text");

        Text tempText = new Text(forecast.temperature() + " °C");
        tempText.getStyleClass().add("temp-text");
        box.getChildren().addAll(timeText, tempText);

        return box;
    }
    public void changeBoxes(List<ForecastWithPollution> forecasts, String day) {
        int index = 0;
        for (int i = 0; i < forecasts.size(); i++) {
            if (Objects.equals(day, forecasts.get(i).day())) {
                index = i;
                break;
            }
        }
        ForecastWithPollution forecast = forecasts.get(index);
        setOtherAttributes(forecast);
        setTempBoxes(forecasts, index, appController.temperatureBoxes);
    }
    public void changeDay(String current_day,String direction) {
        ArrayList<String> Days = new ArrayList<>(7);
        Days.addAll(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
        forecast = forecasts.getFirst();

        if (Objects.equals(direction, "nextDay")) {
            int currentIndex = 1;
            if (Days.contains(current_day)) {
                if (lock < 4) {
                    currentIndex = Days.indexOf(current_day);
                    currentIndex++;
                    currentIndex %= 7;
                    lock++;
                    appController.day.setText(Days.get(currentIndex));
                    changeBoxes(forecasts, Days.get(currentIndex));
                }
            }
        }
        else if (Objects.equals(direction, "prevDay")){
            int currentIndex = 1;
            if (Days.contains(current_day)) {
                if (lock > 0) {
                    currentIndex = Days.indexOf(current_day);
                    currentIndex--;
                    if (currentIndex == -1)
                        currentIndex = 6;
                    lock--;
                    appController.day.setText(Days.get(currentIndex));
                    changeBoxes(forecasts, Days.get(currentIndex));
                }
            }
        }

    }
    private String getAQIDescription(int aqi) {
        return switch (aqi) {
            case 1 -> "Good";
            case 2 -> "Fair";
            case 3 -> "Moderate";
            case 4 -> "Poor";
            case 5 -> "Very Poor";
            default -> "Unknown";
        };
    }
}