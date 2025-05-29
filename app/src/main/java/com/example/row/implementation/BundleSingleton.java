package com.example.row.implementation;

import java.time.LocalTime;
import java.util.HashMap;

public class BundleSingleton {
    private static BundleData bundleData;

    private BundleSingleton() {}
    public static BundleData getInstance(String flagColor, double windDirection, double windSpeed,
                                         HashMap<LocalTime, String> weatherState, HashMap<LocalTime, Double> uvValues,
                                         HashMap<LocalTime, Double> temperatureMap, LocalTime key, String sunrise, String sunset,
                                         int chanceOfRain, String flagInfo) {
        if (bundleData == null) {
            bundleData = new BundleData( flagColor,  windDirection,  windSpeed,
             weatherState, uvValues,
                    temperatureMap,  key,  sunrise,  sunset, chanceOfRain,flagInfo);
        }
        return bundleData;
    }
    public static BundleData getInstance() {
        if (bundleData == null) {
            throw new IllegalStateException("BundleData instance not initialized. Call getInstance with parameters first.");
        }
        return bundleData;
    }
    public static BundleData getInstance(BundleData data) {
        if (bundleData == null) {
            bundleData= data;
        }
        return bundleData;
    }
    public static void setInstance(BundleData data) {
        bundleData = data;
    }
}
