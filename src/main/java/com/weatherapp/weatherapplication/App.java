package com.weatherapp.weatherapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Show notification prompt
        boolean enableNotifications = showNotificationPrompt();

        // Proceed accordingly based on user's choice
        if (enableNotifications) {
            String email = getEmailForNotifications();
          //  System.out.print(email);
            if (email != null) {
                AutomaticEmailSender automaticEmailSender = new AutomaticEmailSender();
                automaticEmailSender.setEmailAddress(email);
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 550);
        Image icon = new Image("/favicon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Weather Application");
        stage.setResizable(false);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styling/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private boolean showNotificationPrompt() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notification Preference");
        alert.setHeaderText("Do you want to receive notifications?");
        alert.setContentText("Click 'Yes' if you want to receive notifications about weather alerts.");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private String getEmailForNotifications() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Email Input");
        dialog.setHeaderText("Please enter your email address for notifications:");
        dialog.setContentText("Email:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static void main(String[] args) {
        launch();

    }
}
