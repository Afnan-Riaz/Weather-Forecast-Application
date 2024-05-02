package com.weatherapp.HelpingClasses;


public class SqlConnection {

    private final static String HostName = "DESKTOP-MCD4BLG";

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
        String connectionUrl = "jdbc:sqlserver://" + HostName + ";"
                + "database=WeatherData;"
                + "integratedSecurity=true;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=10;";

        return connectionUrl ;
    }
}
