module com.weatherapp.weatherapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.weatherapp.weatherapplication to javafx.fxml;
    exports com.weatherapp.weatherapplication;
}