module com.weatherapp.weatherapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.weatherapp.weatherapplication to javafx.fxml;
    exports com.weatherapp.weatherapplication;
}