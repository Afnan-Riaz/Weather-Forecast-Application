package com.weatherapp.CacheManagement;

import java.util.Objects;

public class GetDbTypeFactory {
    public static CacheManagement getDbType(String type){
        if (Objects.equals(type, "sql")){
            return new SQL();
        }
        else if (Objects.equals(type, "file")){
            return new FileHandling();
        }
        else
        {
            return null;
        }
    }
}
