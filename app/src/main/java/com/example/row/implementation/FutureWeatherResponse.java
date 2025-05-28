//Past Weather Conditions index (date + time)
package com.example.row.implementation;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FutureWeatherResponse {
    private WeatherResponse Forecast;
    private int hour;

    public FutureWeatherResponse(String apiKey, String location) {
        String day = java.time.LocalDate.now().toString();
        this.hour = java.time.LocalTime.now().getHour();
        String urlString = "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + location + "&dt=" + day;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                this.Forecast = gson.fromJson(response.toString(), WeatherResponse.class);
            } else {
                System.out.println("Error: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getChanceOfRain() {
        return Forecast != null ? Forecast.forecast.forecastday.get(0).hour.get(hour).chance_of_rain : 0;
    }

    public String getSunrise() {
        return Forecast != null ? Forecast.forecast.forecastday.get(0).astro.sunrise : null;
    }

    public String getSunset() {
        return Forecast != null ? Forecast.forecast.forecastday.get(0).astro.sunset : null;
    }
}



