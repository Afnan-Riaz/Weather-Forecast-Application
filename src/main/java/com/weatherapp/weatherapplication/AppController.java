package com.weatherapp.weatherapplication;

import com.weatherapp.Models.WeatherManager;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    @FXML
<<<<<<< Updated upstream
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
=======
    private Label label;

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
        moreIcon.setOnMouseClicked(this::showPollutantInfo);
        }
        //}

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
        switch (aqi) {
            case 1:
                return "Good";
            case 2:
                return "Fair";
            case 3:
                return "Moderate";
            case 4:
                return "Poor";
            case 5:
                return "Very Poor";
            default:
                return "Unknown";
        }}

}
>>>>>>> Stashed changes
