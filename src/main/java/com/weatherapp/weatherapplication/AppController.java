package com.weatherapp.weatherapplication;

import com.weatherapp.Models.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.TimeZone;

public class AppController {
    public String current_city="Lahore";
    public Text description;
    public Text city;
    public Text humidity;
    public Text windspeed;
    public Text temp;
    public Text sunrise;
    public Text sunset;
    public Text day;
    public Text pressure;
    public Text quality;
    public ImageView weatherIcon;
    private List<ForecastWithPollution> forecasts;
    private ForecastWithPollution forecast;
    public ImageView moreIcon;
    public VBox bgImage;
    public ImageView prevDay;
    public ImageView nextDay;
    public static int lock = 0;
    public HBox temperatureBoxes;

    @FXML

    public void initialize() {
        current_city = LiveLocationTracker.getLiveLocation();
        getWeatherData(current_city);
        setImages();

        moreIcon.setOnMouseClicked(this::showPollutantInfo);
    }
    void setImages(){
        String icon = forecast.icon();
        String imageName = ImageHandler.getImage(icon);
        String iconName=ImageHandler.getIcon(icon);
        String baseImagePath = "/styling/";
        String imageUrl = Objects.requireNonNull(getClass().getResource(baseImagePath + imageName)).toExternalForm();
        String iconUrl = Objects.requireNonNull(getClass().getResource(baseImagePath + iconName)).toExternalForm();
        bgImage.setStyle("-fx-background-image: url('" + imageUrl + "');" +
                "-fx-background-size: cover; ");
        weatherIcon.setStyle("-fx-image:  url('" + iconUrl + "');" +
                "-fx-background-size: cover; ");
    }
    public void getWeatherData(String city) {
        WeatherManager weatherManager = new WeatherManager(city, "9804f15edc7893ea4947a7526edfc496");
        forecasts = weatherManager.getWeatherForecast();
        Weather current_weather = weatherManager.current_weather;

        forecast = forecasts.getFirst();

        setTempBoxes(forecasts, 0, temperatureBoxes);
        setOtherAttributes(forecast, city);
        sunrise.setText(formatTime(current_weather.sunrise()));
        sunset.setText(formatTime(current_weather.sunset()));
    }
    private void setTempBoxes(List<ForecastWithPollution> forecasts, int startingIndex, HBox parent){
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
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
    private void setOtherAttributes(ForecastWithPollution forecast, String current_city){
        city.setText(current_city);
        description.setText(capitalizeFirstLetter(forecast.description()));
        humidity.setText(forecast.humidity() + " %");
        windspeed.setText(forecast.windSpeed() + " m/s");
        temp.setText(forecast.temperature() + " °C");
        day.setText(forecast.day());
        pressure.setText(forecast.pressure() + " hPa");
        quality.setText(getAQIDescription(forecast.airQualityIndex()));
    }
        private void changeBoxes(List<ForecastWithPollution> forecasts, String day) {
            int index = 0;
            for (int i = 0; i < forecasts.size(); i++) {
                if (Objects.equals(day, forecasts.get(i).day())) {
                    index = i;
                    break;
                }
            }
            ForecastWithPollution forecast = forecasts.get(index);
            setOtherAttributes(forecast, current_city);
            setTempBoxes(forecasts, index, temperatureBoxes);
        }
        public void changeDay(MouseEvent mouseEvent) {
            ArrayList<String> Days = new ArrayList<>(7);
            Days.addAll(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
            forecast = forecasts.getFirst();
            String current_day = day.getText();
            Node source = (Node) mouseEvent.getSource();
            String id = source.getId();

            if (Objects.equals(id, "nextDay")) {
                int currentIndex = 1;
                if (Days.contains(current_day)) {
                    if (lock < 4) {
                        currentIndex = Days.indexOf(current_day);
                        currentIndex++;
                        currentIndex %= 7;
                        lock++;
                        day.setText(Days.get(currentIndex));
                        changeBoxes(forecasts, Days.get(currentIndex));
                    }
                }
            }
            else if (Objects.equals(id, "prevDay")){
                int currentIndex = 1;
                if (Days.contains(current_day)) {
                    if (lock > 0) {
                        currentIndex = Days.indexOf(current_day);
                        currentIndex--;
                        if (currentIndex == -1)
                            currentIndex = 6;
                        lock--;
                        day.setText(Days.get(currentIndex));
                        changeBoxes(forecasts, Days.get(currentIndex));
                    }
                }
            }

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
    public String convertTo12HourFormat(String time24Hour) {
        // Split the input time string into hours and minutes
        String[] parts = time24Hour.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        // Determine whether it's am or pm
        String period = (hours >= 12) ? "pm" : "am";

        // Convert hours to 12-hour format
        if (hours > 12) {
            hours -= 12;
        } else if (hours == 0) {
            hours = 12; // 0 hour in 24-hour format is 12 am in 12-hour format
        }

        // Construct the string in 12-hour format
        String time12Hour = String.format("%d:%02d %s", hours, minutes, period);

        return time12Hour;
    }


    public void openLocationView(MouseEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("location-view.fxml"));
            Parent root = fxmlLoader.load();
            LocationController controller = fxmlLoader.getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 300, 400);
            stage.setScene(scene);

            stage.initStyle(StageStyle.UNDECORATED);
            scene.setFill(Color.TRANSPARENT);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    stage.hide();
                }
            });
            stage.show();
            stage.setOnCloseRequest(new EventHandler< WindowEvent >(){
                public void handle(WindowEvent event) {
                    current_city=controller.locationField.getText();
                    getWeatherData(current_city);
                    setImages();
                }
            });
        }
    private void showPollutantInfo(MouseEvent event) {
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
        dialog.show();
    }


    private String formatTime(long timeInSeconds) {
        Date date = new Date(timeInSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Karachi"));
        return sdf.format(date);
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

