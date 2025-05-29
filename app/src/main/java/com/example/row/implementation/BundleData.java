package com.example.row.implementation;

// WeatherBundle.java
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class BundleData {
    private String flagColor;
    private double windDirection;
    private double windSpeed;

    private LocalTime key;
    private HashMap<LocalTime, String> weatherState;
    private HashMap<LocalTime,Double> uvValues;
    private HashMap<LocalTime, Double> temperatureMap;
    private String sunrise;
    private String sunset;
    private int chanceOfRain;
    public BundleData(String flagColor, double windDirection, double windSpeed,
                      HashMap<LocalTime, String> weatherState, HashMap<LocalTime, Double> uvValues,
                      HashMap<LocalTime, Double> temperatureMap, LocalTime key, String sunrise, String sunset,
                      int chanceOfRain) {
        this.flagColor = flagColor;
        this.windDirection = windDirection;
        this.key = key;
        this.windSpeed = windSpeed;
        this.weatherState = weatherState;
        this.uvValues = uvValues;
        this.temperatureMap = temperatureMap;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.chanceOfRain = chanceOfRain;
    }

    public LocalTime getKey() {
        return key;
    }

    public String getUVInfo(double UVval){
        Map<String,String> UVInfo= new HashMap<>();
        UVInfo.put("Low Risk","Low risk for sunburn and damage. SPF 30 recommended");
        UVInfo.put("Moderate Risk", "Moderate risk for sunburn and damage. At least SPF 30 and UV-blocking sunglasses recommended.");
        UVInfo.put("High Risk","High risk of sunburn and damage. At least SPF 30, protective clothing and UV-blocking sunglasses recommended");
        if (UVval < 3.0 ){
            return UVInfo.get("Low Risk");
        } else if (UVval < 6.0){
            return UVInfo.get("Moderate Risk");
        } else{
            return UVInfo.get("High Risk");
        }

    }
    public String getFlagColor() {
        return flagColor;
    }
    public double getWindDirection() {
        return windDirection;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public Map<LocalTime, String> getWeatherState() {
        return weatherState;
    }
    public Map<LocalTime, Double> getUVValues() {
        return uvValues;
    }
    public Map<LocalTime, Double> getTemperatureMap() {
        return temperatureMap;
    }
    public String getSunrise() {
        return sunrise;
    }
    public String getSunset() {
        return sunset;
    }
    public int getChanceOfRain() {
        return chanceOfRain;
    }


}