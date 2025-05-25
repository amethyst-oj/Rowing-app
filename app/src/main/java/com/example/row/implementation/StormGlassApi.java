package com.example.row.implementation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import com.google.gson.*;

public abstract class StormGlassApi implements Weather {

    private static final String API_KEY = "8b3d4364-390f-11f0-be54-0242ac130003-8b3d43be-390f-11f0-be54-0242ac130003";
    public static Double getWaterTemperatureData(double lat, double lon) {
        return fetchWaterTemperature(lat, lon, LocalDateTime.now(java.time.Clock.systemUTC()));
    }
    public static Double getWaterPerformanceImpact(double waterTemp) {
        return 0.59 * Math.sqrt(waterTemp);
    }

    public static Double fetchWaterTemperature(double lat, double lon, LocalDateTime dateTimeUTC) {
        try {
            String startISO = dateTimeUTC.withMinute(0).withSecond(0).withNano(0)
                    .format(DateTimeFormatter.ISO_DATE_TIME);
            String urlString = String.format(
                    "https://api.stormglass.io/v2/weather/point?lat=%.4f&lng=%.4f&params=waterTemperature&start=%s&end=%s",
                    lat, lon, startISO, startISO);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", API_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray hours = json.getAsJsonArray("hours");
            if (hours.size() > 0) {
                JsonObject firstHour = hours.get(0).getAsJsonObject();
                JsonObject tempObj = firstHour.getAsJsonObject("waterTemperature");
                if (tempObj.has("sg")) {
                    return tempObj.get("sg").getAsDouble();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
