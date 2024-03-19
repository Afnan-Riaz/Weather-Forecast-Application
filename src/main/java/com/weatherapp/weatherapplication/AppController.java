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
    public Text tBox1;
    public Text b1_temp;
    public Text b1_feel;
    public Text tBox2;
    public Text b2_temp;
    public Text tBox3;
    public Text b3_temp;
    public Text tbox4;
    public Text b4_temp;
    public Text tBox5;
    public Text b5_temp;
    public Text tBox6;
    public Text b6_temp;
    public Text tBox7;
    public Text b7_temp;
    public Text tBox8;
    public Text b8_temp;

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

        tBox1.setText(forecasts.get(0).time());
        b1_temp.setText(String.valueOf(forecasts.get(0).temperature() + " °C"));
        b1_feel.setText(forecasts.get(0).feelsLike() + " °C");

        tBox2.setText(forecasts.get(1).time());
        b2_temp.setText(String.valueOf(forecasts.get(1).temperature() + " °C"));
        tBox3.setText(forecasts.get(2).time());
        b3_temp.setText(String.valueOf(forecasts.get(2).temperature() + " °C"));
        tbox4.setText(forecasts.get(3).time());
        b4_temp.setText(String.valueOf(forecasts.get(3).temperature() + " °C"));
        tBox5.setText(forecasts.get(4).time());
        b5_temp.setText(String.valueOf(forecasts.get(4).temperature() + " °C"));
        tBox6.setText(forecasts.get(5).time());
        b6_temp.setText(String.valueOf(forecasts.get(5).temperature() + " °C"));
        tBox7.setText(forecasts.get(6).time());
        b7_temp.setText(String.valueOf(forecasts.get(6).temperature() + " °C"));
        tBox8.setText(forecasts.get(7).time());
        b8_temp.setText(String.valueOf(forecasts.get(7).temperature() + " °C"));


        }
        //}

    private void showPollutantInfo(MouseEvent event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Pollutant Information");
//
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

