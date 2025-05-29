package com.example.row.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class CombinedWeatherSmile implements Weather{
    private final CurrentWeatherApi CurrentApi;
    private final FutureWeatherResponse FutureApi;
    private final double lat;
    private final double lon;


    public CombinedWeatherSmile (String apiKey, String Location, double lat, double lon) {
        this.CurrentApi = new CurrentWeatherApi(apiKey, Location);
        this.lat = lat;
        this.lon = lon;
        this.FutureApi = new FutureWeatherResponse(apiKey, Location);
        //WaterTemp = 18.0; Didn't know what to put here, was used for testing
    }
    public CombinedWeatherSmile() {  //TODO added for default, cambridge city centre coordinates, is this ok?
        double lat = 52.205051;
        double lon = 0.122162;
        String apiKey = "c283fac38f0347adb3b154902252705";
        String location = "Cambridge";
        this.CurrentApi = new CurrentWeatherApi(apiKey, location);
        this.lat = lat;
        this.lon = lon;
        this.FutureApi = new FutureWeatherResponse(apiKey, location);
    }
    @Override
    public HashMap<LocalTime, Double> getUVData() {
        return CurrentApi.getUVData();
    }

    @Override
    public HashMap<LocalTime, Double> getExternalTemperatureData() {
        return CurrentApi.getExternalTempData();
    }

    @Override
    public double getWaterTemperatureData() {
        return StormGlassApi.getWaterTemperatureData(lat, lon);
    }

    @Override
    //Uses Temperature
    public double getWaterPerformanceImpact() {
        return StormGlassApi.getWaterPerformanceImpact(/*WaterTemp should go here */(StormGlassApi.getWaterTemperatureData(lat,lon)));
    }

    @Override
    public HashMap<LocalTime, String> getGeneralWeatherState(String apiKey, String location) throws JsonProcessingException {
        return CurrentWeatherApi.getGeneralConditions( apiKey,  location);
    }

    @Override
    public int getChanceOfRain(){
        return FutureApi.getChanceOfRain();
    }

    @Override
    public String getSunrise() {
        return FutureApi.getSunrise();
    } //TODO string? is it a time?

    @Override
    public String getSunset() {
        return FutureApi.getSunset();
    } //TODO same question as above
}
