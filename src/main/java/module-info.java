module com.weatherapp.weatherapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
//    requires org.simplejavamail;
//    requires org.simplejavamail.core;
    requires java.mail;

    opens com.weatherapp.weatherapplication to javafx.fxml;
    exports com.weatherapp.weatherapplication;
}