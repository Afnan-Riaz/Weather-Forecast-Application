package com.weatherapp.weatherapplication;

import com.weatherapp.Models.ImageHandler;
import com.weatherapp.Models.WeatherManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AppController {
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
    public WeatherManager.WeatherForecast forecast;
    public ImageView moreIcon;
    public VBox bgImage;
    public HBox locationBtn;
    @FXML
    public void initialize() {
        WeatherManager weatherManager = new WeatherManager("Lahore", "9804f15edc7893ea4947a7526edfc496");
        List<WeatherManager.WeatherForecast> forecasts = weatherManager.getWeatherForecast();
         forecast = forecasts.getFirst();
//        for (WeatherManager.WeatherForecast forecast : forecasts) {
            city.setText("Lahore");
            description.setText(forecast.description());
            humidity.setText(String.valueOf(forecast.humidity()+" %"));
            windspeed.setText(String.valueOf(forecast.windSpeed()+" m/s"));
        temp.setText(String.valueOf(forecast.temperature()) + " °C");
        sunrise.setText(formatTime(forecast.sunrise()));
        sunset.setText(formatTime(forecast.sunset()));
        day.setText(forecast.day());
        pressure.setText(String.valueOf(forecast.pressure()) + " hPa");
        quality.setText(getAQIDescription(forecast.airQualityIndex()));
        String icon = forecast.icon();
        String imageName = ImageHandler.getImage(icon);
        String baseImagePath = "/styling/";
        String imageUrl = Objects.requireNonNull(getClass().getResource(baseImagePath + imageName)).toExternalForm();
        bgImage.setStyle("-fx-background-image: url('" + imageUrl + "');" +
                "-fx-background-size: cover; ");
        moreIcon.setOnMouseClicked(this::showPollutantInfo);
        }
        //}
        public void openLocationView(MouseEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("location-view.fxml"));
            LocationController controller = fxmlLoader.getController();
            // Call any methods on controller if needed

            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(), 300, 400);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            scene.setFill(Color.TRANSPARENT);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        }
    private void showPollutantInfo(MouseEvent event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Pollutant Information");
//        dialog.setResultConverter(dialogButton -> null);

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
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> {
            dialog.close();
        });

        dialog.getDialogPane().setContent(textArea);
        dialog.show();
    }


    private String formatTime(long timeInSeconds) {
        Date date = new Date(timeInSeconds * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
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