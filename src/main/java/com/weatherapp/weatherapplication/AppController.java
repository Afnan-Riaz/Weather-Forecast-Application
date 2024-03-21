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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.TimeZone;

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
    public VBox bgImage;
    public ImageView prevDay;
    public ImageView nextDay;
    public static int lock = 0;
    @FXML

    public void initialize() {
        WeatherManager weatherManager = new WeatherManager("Lahore", "9804f15edc7893ea4947a7526edfc496");
        List<WeatherManager.WeatherForecast> forecasts = weatherManager.getWeatherForecast();
        forecast = forecasts.getFirst();

        setOtherAttributes(forecast, "Lahore");
        setTempBoxes(forecasts, 0);

        String icon = forecast.icon();
        String imageName = ImageHandler.getImage(icon);
        String baseImagePath = "/styling/";
        String imageUrl = Objects.requireNonNull(getClass().getResource(baseImagePath + imageName)).toExternalForm();
        bgImage.setStyle("-fx-background-image: url('" + imageUrl + "');" +
                "-fx-background-size: cover; ");
        moreIcon.setOnMouseClicked(this::showPollutantInfo);


    }

    private void setTempBoxes(List<WeatherManager.WeatherForecast> forecasts, int startingIndex){
        tBox1.setText(convertTo12HourFormat(forecasts.get(startingIndex).time()));
        b1_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
        b1_feel.setText(forecasts.get(startingIndex).feelsLike() + " °C");
        tBox2.setText(convertTo12HourFormat(forecasts.get(++startingIndex).time()));
        b2_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
        tBox3.setText(convertTo12HourFormat(forecasts.get(++startingIndex).time()));
        b3_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
        tbox4.setText(convertTo12HourFormat(forecasts.get(++startingIndex).time()));
        b4_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
        tBox5.setText(convertTo12HourFormat(forecasts.get(++startingIndex).time()));
        b5_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
        tBox6.setText(convertTo12HourFormat(forecasts.get(++startingIndex).time()));
        b6_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
        tBox7.setText(convertTo12HourFormat(forecasts.get(++startingIndex).time()));
        b7_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
        tBox8.setText(convertTo12HourFormat(forecasts.get(++startingIndex).time()));
        b8_temp.setText(String.valueOf(forecasts.get(startingIndex).temperature() + " °C"));
    }
    private void setOtherAttributes(WeatherManager.WeatherForecast forecast, String current_city){
        city.setText(current_city);
        description.setText(forecast.description());
        humidity.setText(String.valueOf(forecast.humidity() + " %"));
        windspeed.setText(String.valueOf(forecast.windSpeed() + " m/s"));
        temp.setText(String.valueOf(forecast.temperature()) + " °C");
        sunrise.setText(formatTime(forecast.sunrise()));
        sunset.setText(formatTime(forecast.sunset()));
        day.setText(forecast.day());
        pressure.setText(String.valueOf(forecast.pressure()) + " hPa");
        quality.setText(getAQIDescription(forecast.airQualityIndex()));
    }
        private void changeBoxes(List<WeatherManager.WeatherForecast> forecasts, String day) {
            int index = 0;
            for (int i = 0; i < forecasts.size(); i++) {
                if (Objects.equals(day, forecasts.get(i).day())) {
                    index = i;
                    break;
                }
            }
            WeatherManager.WeatherForecast forecast = forecasts.get(index);
            setOtherAttributes(forecast, "Lahore");
            setTempBoxes(forecasts, index);
        }
        public void changeDay(MouseEvent mouseEvent) {
            WeatherManager weatherManager = new WeatherManager("Lahore", "9804f15edc7893ea4947a7526edfc496");
            List<WeatherManager.WeatherForecast> forecasts = weatherManager.getWeatherForecast();
            ArrayList<String> Days = new ArrayList<String>(7);
            Days.add("Monday");
            Days.add("Tuesday");
            Days.add("Wednesday");
            Days.add("Thursday");
            Days.add("Friday");
            Days.add("Saturday");
            Days.add("Sunday");
            forecast = forecasts.getFirst();
            String current_day = day.getText();
            Node source = (Node) mouseEvent.getSource();
            String id = source.getId();

            if (Objects.equals(id, "nextDay")) {
                int currentIndex = 1;
                if (Days.contains(current_day)) {
                    if (lock < 3) {
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

