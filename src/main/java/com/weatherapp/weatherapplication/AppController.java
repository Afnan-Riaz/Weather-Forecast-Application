package com.weatherapp.weatherapplication;

import com.weatherapp.Models.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
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

public class AppController {
    public String current_city = "Lahore";
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
    public ImageView moreIcon;
    public VBox bgImage;
    public ImageView prevDay;
    public ImageView nextDay;
    public HBox temperatureBoxes;
    WeatherService weatherService;

    @FXML

    public void initialize() {
        current_city = LiveLocationTracker.getLiveLocation();
        weatherService = new WeatherService(this);
        weatherService.hydrateUI(current_city);
        moreIcon.setOnMouseClicked(this::showPollutantDialog);
    }

    public void changeDay(MouseEvent mouseEvent) {
        String current_day = day.getText();
        Node source = (Node) mouseEvent.getSource();
        String id = source.getId();
        weatherService.changeDay(current_day, id);
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
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                current_city = controller.locationField.getText();
                weatherService.hydrateUI(current_city);
            }
        });
    }

    private void showPollutantDialog(MouseEvent event) {
        Dialog<Void> pollutantInfo = weatherService.createPollutantInfoDialog();
        pollutantInfo.show();
    }
}

