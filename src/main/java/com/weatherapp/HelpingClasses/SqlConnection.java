package com.weatherapp.HelpingClasses;


public class SqlConnection {
    public static String getConnectionUrl() {
        // Register the JDBC driver
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Unable to load SQL Server JDBC driver. Make sure the driver JAR file is in your classpath.");
            e.printStackTrace();
            return null;
        }

        // Connection string
        String connectionUrl = "jdbc:sqlserver://DESKTOP-MCD4BLG;"
                + "database=WeatherData;"
                + "integratedSecurity=true;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=30;";
        System.out.println("connection made");
        return connectionUrl ;
    }
}
