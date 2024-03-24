package com.weatherapp.weatherapplication;

import com.weatherapp.Models.GeoCoder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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

    public TextField longitudeField;
    public TextField latitudeField;
    public VBox locationList;
    @FXML
    public TextField locationField;

    private List<String> cityNames = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            parseCityDataInBackground();
            addEventFilters();
            formatText(latitudeField);
            formatText(longitudeField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addEventFilters() {
        locationField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchCity(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null));
            }
        });
        longitudeField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchCoordinates(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null));
            }
        });
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

    public void searchCity(MouseEvent event) {
        String location = locationField.getText();
        if (isLocationValid(location)) {
            createListItem(location);
        } else {
            showNotification("Location already exists or is invalid.");
        }
    }
    private void formatText(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (!change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return null;
            } else {
                return change;
            }
        });
        textField.setTextFormatter(formatter);
    }
    public void searchCoordinates(MouseEvent event) {
        String latitude = latitudeField.getText();
        String longitude = longitudeField.getText();
        if(latitude.isEmpty() || longitude.isEmpty()) {
            showNotification("Please enter both latitude and longitude.");
            return;
        }
        GeoCoder geoCoder = new GeoCoder("9804f15edc7893ea4947a7526edfc496", Double.parseDouble(latitude), Double.parseDouble(longitude));
        String location = geoCoder.getCity();
        if (!Objects.equals(location, "") && isLocationValid(location)) {
            createListItem(location);
        } else {
            showNotification("Location already exists or is invalid.");
        }
    }

    private VBox createNotificationContent(String content) {
        VBox notificationContent = new VBox();
        notificationContent.getChildren().add(new Label(content));
        return notificationContent;
    }
    public void showNotification(String content) {
        Notifications.create()
                .hideAfter(Duration.seconds(3))
                .position(Pos.CENTER)
                .owner(locationField.getScene().getWindow())
                .graphic(createNotificationContent(content))
                .show();
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
            event.consume();
        });
        hbox.getChildren().addAll(text, imageView);
        locationList.getChildren().add(hbox);
    }

    private boolean isLocationValid(String location) {
        return cityNames.contains(location) && locationList.getChildren().stream().noneMatch(node -> ((Text) ((HBox) node).getChildren().getFirst()).getText().equals(location));
    }

    private Collection<String> getSortedCityNames(String userInput) {
        String lowerCaseUserInput = userInput.toLowerCase();
        return cityNames.stream()
                .filter(city -> city.toLowerCase().startsWith(lowerCaseUserInput))
                .sorted(Comparator.comparing(city -> city.toLowerCase().startsWith(lowerCaseUserInput) ? 0 : 1))
                .collect(Collectors.toList());
    }

}