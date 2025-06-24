//Past Weather Conditions index (date + time)
package com.example.row.implementation;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FutureWeatherResponse {

    public WeatherResponse ftwForecast;
    private int index;

    private int hour;
    private static HashMap<LocalTime, String> forecastdata = new HashMap<>();
    private static HashMap<LocalTime, Long> nextTemps = new HashMap<>();

    public FutureWeatherResponse(String apiKey, String location, int index) {

        this.hour = java.time.LocalTime.now().getHour();
        String urlString = "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + location + "&days=13";
        this.index= index;
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
                this.ftwForecast = gson.fromJson(response.toString(), WeatherResponse.class);
            } else {
                System.out.println("Error: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public  HashMap<LocalTime, String> getGeneralConditions (String apiKey, String location){
            getForecast(apiKey, location);
            return forecastdata;
        }

        public  void getForecast(String apiKey, String location){
            int hoursNeeded = 5;
            List<Forecastday> forecasts = ftwForecast.forecast.forecastday.subList(index, index + 2);
            Current current = ftwForecast.current;
            LocalDateTime now = LocalDateTime.now().plusDays(index);
            outerloop:
            for (Forecastday day : forecasts) {
                for (Hour hour : day.hour) {
                    LocalDateTime hourInLocalTime = LocalDateTime.parse(hour.time);
                    if (now.isAfter(hourInLocalTime)) {
                        continue;
                    }
                    if (hour.time_epoch >= current.time_epoch || hour.time_epoch - current.time_epoch > -3600) {
                        forecastdata.put(LocalTime.from(hourInLocalTime), hour.condition.icon);
                        nextTemps.put(LocalTime.from(hourInLocalTime), Math.round(hour.temp_c));
                        if (hoursNeeded == forecastdata.size()) {
                            break outerloop;
                        }
                    }
                }
            }
        }

        public HashMap<LocalTime, Double> getExternalTempData() {
            HashMap<LocalTime, Double> temps = new HashMap<>();
            for (Map.Entry<LocalTime, Long> entry : nextTemps.entrySet()) {
                temps.put(entry.getKey(),entry.getValue().doubleValue());
            }
            return temps;
        }

        public Double getWindDirection() {
            return ftwForecast != null ? ftwForecast.current.wind_degree : 0.0;
        }

        public Double getWindSpeed() {
            return ftwForecast != null ? ftwForecast.current.wind_kph : 0.0;
        }

        public HashMap<LocalTime, Double> getUVData() {
            HashMap<LocalTime, Double> UV = new HashMap<>();
            Forecastday fday = ftwForecast != null ? ftwForecast.forecast.forecastday.get(index) : null;
            if (fday != null) {
                LocalTime now = LocalTime.now().withMinute(0).withSecond(0).withNano(0);
                UV.put(now, fday.hour.get(hour).uv);
            }
            return UV;
        }

        public Integer getChanceOfRain() {
            return ftwForecast != null ? ftwForecast.forecast.forecastday.get(index).hour.get(hour).chance_of_rain : 0;
        }

        public String getSunrise() {
            return ftwForecast != null ? ftwForecast.forecast.forecastday.get(index).astro.sunrise : null;
        }

        public String getSunset() {
            return ftwForecast != null ? ftwForecast.forecast.forecastday.get(index).astro.sunset : null;
        }
}



