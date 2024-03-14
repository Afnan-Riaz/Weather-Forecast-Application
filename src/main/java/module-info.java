module com.weatherapp.weatherapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.weatherapp.weatherapplication to javafx.fxml;
    exports com.weatherapp.weatherapplication;
}