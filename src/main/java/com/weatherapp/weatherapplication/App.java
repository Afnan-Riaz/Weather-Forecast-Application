package com.weatherapp.weatherapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
//import org.simplejavamail.api.email.Email;
//import org.simplejavamail.api.mailer.Mailer;
//import org.simplejavamail.email.EmailBuilder;
//import org.simplejavamail.mailer.MailerBuilder;


public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 550);
        Image icon=new Image("/favicon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Weather Application");
        stage.setResizable(false);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styling/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}