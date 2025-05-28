package com.example.row.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalTime;
import java.util.Map;

public class TestingDriver {
    public static void main(String[] args) throws JsonProcessingException {
        String where = "Cambridge";
        String api = "d8f21a5e71ff4fb99bc131927252005";
        double lat = 52.2098389;
        double lon = 0.1164972;
        Weather current = new CombinedWeatherSmile(api, where, lat, lon);
        Map<LocalTime, Double> Temp = current.getExternalTemperatureData();
        Map<LocalTime, Double> UV = current.getUVData();
        Map<LocalTime, String> Conditions = current.getGeneralWeatherState(api,where);
        double waterTemp = current.getWaterTemperatureData();
        int cRain = current.getChanceOfRain();
        String sunrise = current.getSunrise();
        String sunset = current.getSunset();
        //System.out.println("Conditions map size: " + Conditions.size());
        System.out.println("The Water temperature is: " + waterTemp);
        System.out.println("The chance of rain is: " + String.valueOf(cRain));
        System.out.println("Sunrise is at: " + sunrise);
        System.out.println("Sunset is at: " + sunset);
        if (Temp.isEmpty() || UV.isEmpty() || Conditions.isEmpty()) {
            System.out.println("No Temp or UV data available.");
        } else {
            for (Map.Entry<LocalTime, Double> entry : Temp.entrySet()) {
                System.out.println("Time: " + entry.getKey() + " -> Temp: " + entry.getValue());
            }
            for (Map.Entry<LocalTime, Double> entry : UV.entrySet()) {
                System.out.println("Time: " + entry.getKey() + " -> UV: " + entry.getValue());
            }
            for (Map.Entry<LocalTime, String> entry : Conditions.entrySet()) {
                System.out.println("Time: " + entry.getKey() + " -> Conditions: " + entry.getValue());
            }
        }
    }
}