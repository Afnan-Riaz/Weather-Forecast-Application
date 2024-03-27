package com.weatherapp.weatherapplication;

import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Show notification prompt
new Thread(() -> {
    boolean enableNotifications = showNotificationPrompt();

    // Proceed accordingly based on user's choice
    if (enableNotifications) {
        String email = getEmailForNotifications();
        if (email != null) {
            AutomaticEmailSender automaticEmailSender = new AutomaticEmailSender();
            automaticEmailSender.setEmailAddress(email);
        }
    }
}).start();
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
        FutureTask<Boolean> task = new FutureTask<>(()-> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Notification Preference");
            alert.setHeaderText("Do you want to receive notifications?");
            alert.setContentText("Click 'Yes' if you want to receive notifications about weather alerts.");

            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        });

        Platform.runLater(task); // This line is added to execute the FutureTask

        try {
            return task.get(); // This will block until the FutureTask is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getEmailForNotifications() {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Email Input");
            dialog.setHeaderText("Please enter your email address for notifications:");
            dialog.setContentText("Email:");

            Optional<String> result = dialog.showAndWait();
            return result.orElse(null);
        });

        Platform.runLater(futureTask);

        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();

    }
}
