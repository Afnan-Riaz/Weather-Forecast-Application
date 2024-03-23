package com.weatherapp.weatherapplication;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.Notifications;

import java.util.*;
import java.util.stream.Collectors;

public class LocationController {

    AppController appController;
    public VBox locationList;
    @FXML
    public TextField locationField;

    private List<String> cityNames = new ArrayList<>();
    @FXML
    public void initialize() {
        try {
            parseCityDataInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseCityDataInBackground() {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return parseCityData();
            }
        };

        task.setOnSucceeded(event -> {
            cityNames = task.getValue();
            TextFields.bindAutoCompletion(locationField, param -> getSortedCityNames(param.getUserText()));
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        new Thread(task).start();
    }

    private List<String> parseCityData() throws Exception {
        CityDataParser parser = new CityDataParser();
        return parser.parseCityData();
    }
    public void search(MouseEvent event) {
        String location = locationField.getText();
        if (isLocationValid(location)){
            createListItem(location);
        } else {
            Notifications.create()
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.CENTER)
                    .owner(locationField.getScene().getWindow())
                    .graphic(createNotificationContent("Location already exists or is invalid."))
                    .show();
        }
    }
    private VBox createNotificationContent(String content) {
        VBox notificationContent = new VBox();
        notificationContent.getChildren().add(  new Label(content));
        return notificationContent;
    }
    private void createListItem(String location) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("location-item");
        hbox.setOnMouseClicked(event -> {
            Stage stage = (Stage) hbox.getScene().getWindow();
            locationField.setText(((Text) hbox.getChildren().getFirst()).getText());
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        Text text = new Text(location);
        text.getStyleClass().add("location-text");
        text.setWrappingWidth(210);

        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("delete-icon");
        imageView.setFitHeight(28);
        imageView.setFitWidth(28);
        imageView.setPickOnBounds(true);
        imageView.setOnMouseClicked(event -> {
            locationList.getChildren().remove(hbox);
        });
        hbox.getChildren().addAll(text,imageView);
        locationList.getChildren().add(hbox);
    }
    private boolean isLocationValid(String location) {
        return cityNames.contains(location)&&locationList.getChildren().stream().noneMatch(node -> ((Text)((HBox)node).getChildren().getFirst()).getText().equals(location));
    }
//    private void passDataToMainController(String location) {
//        appController.getWeatherData(location);
//
//    }
    private Collection<String> getSortedCityNames(String userInput) {
        String lowerCaseUserInput = userInput.toLowerCase();
        return cityNames.stream()
                .filter(city -> city.toLowerCase().startsWith(lowerCaseUserInput))
                .sorted(Comparator.comparing(city -> city.toLowerCase().startsWith(lowerCaseUserInput) ? 0 : 1))
                .collect(Collectors.toList());
    }

}