module com.weatherapp.weatherapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    requires java.mail;

    requires org.controlsfx.controls;
    requires json.simple;
    requires java.sql;
    opens com.weatherapp.weatherapplication to javafx.fxml;
    exports com.weatherapp.weatherapplication;
}