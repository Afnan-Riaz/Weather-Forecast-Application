package com.weatherapp.CacheManagement;

import javafx.scene.chart.PieChart;

import java.sql.DatabaseMetaData;


// Uses Singleton Design Pattern:
public class DatabaseType {
//    public static DatabaseType instance;
    private static String dbType;

//    public static DatabaseType getInstance(String type){
//        if (instance == null){
//            instance = new DatabaseType(type);
//        }
//        return instance;
//    }

    public static void setDbType(String type){
        if (type.equalsIgnoreCase("sql")) {
            dbType = "sql";
        }
        else {
            dbType = "file";
        }
    }

    public static String getDbType(){
        return dbType;
    }
    private DatabaseType(String type){
        dbType = type;
    }
}
