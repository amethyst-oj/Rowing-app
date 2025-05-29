package com.example.row.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public interface Weather {
    HashMap<LocalTime, Double> getExternalTemperatureData();
    HashMap<LocalTime, Double> getUVData();
    double getWaterTemperatureData();
    double getWaterPerformanceImpact();
    HashMap<LocalTime, String> getGeneralWeatherState(String apiKey, String location) throws JsonProcessingException;

    int getChanceOfRain();
    String getSunrise();
    String getSunset();
}

/*public interface Weather {
    Map<LocalTime, Double> getUVData(); // 0 - 11
    Map<LocalTime, Double> getExternalTemperatureData();
    Double getWaterTemperatureData(); // > 0;
    Double getWaterPerformanceImpact(); // dV = 0.59 * T^0.5, between 0 and 100 but typically less than 5
    Map<LocalTime, String> getGeneralWeatherState();
    int getChanceOfRain(); // between 0 and 100
    LocalTime getSunrise();
    LocalTime getSunset();
 }
 */


