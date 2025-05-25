package com.example.row.implementation;

import java.time.LocalTime;
import java.util.Map;

public interface Weather {
    Map<LocalTime, Double> getExternalTemperatureData();
    Map<LocalTime, Double> getUVData();
    double getWaterTemperatureData();
    double getWaterPerformanceImpact();
    Map<LocalTime, String> getGeneralWeatherState();
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


