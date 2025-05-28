//Current Weather Conditions
package com.example.row.implementation;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class CurrentWeatherApi{
    private WeatherResponse weatherdata;
    private static Map<LocalTime,String> forecastdata= new HashMap<>();
    private static Map<LocalTime,String> nextTemps=new HashMap<>();
    public CurrentWeatherApi (String apiKey, String location) {
        this.weatherdata = fetchCurrentWeather(apiKey, location);
    }
    public static Map<LocalTime,String> getGeneralConditions(String apiKey, String location){
        getForecast( apiKey,  location);
        return forecastdata;
    }
    public static WeatherResponse fetchCurrentWeather(String apiKey, String location) {
        //apiKey = "c283fac38f0347adb3b154902252705";
        //location = "Cambridge";
        String urlString = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + location;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int x= conn.getResponseCode();
            if (x == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();

                return gson.fromJson(response.toString(), WeatherResponse.class);

            } else {
                System.out.println("GET request failed. Response Code: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Other causes");
            e.printStackTrace();
        }
        return null;
    }

    public static void getForecast(String apiKey, String location) {
        //apiKey = "c283fac38f0347adb3b154902252705";
        //location = "Cambridge";
        String urlString = "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + location + "&days=2";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int x= conn.getResponseCode();
            if (x == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                LocalTime now = LocalTime.now().withMinute(0).withSecond(0).withNano(0);
                Gson gson = new Gson();
                WeatherResponse forecast = gson.fromJson(response.toString(), WeatherResponse.class);
                int hoursNeeded= 5;
                Current current= forecast.current;
                outerloop:
                for (Forecastday day: forecast.forecast.forecastday){
                    for (Hour hour: day.hour){
                        if (hour.time_epoch >= current.time_epoch || hour.time_epoch - current.time_epoch > -3600){
                            forecastdata.put(LocalTime.parse(hour.time.split(" ")[1]),hour.condition.text);
                            nextTemps.put(LocalTime.parse(hour.time.split(" ")[1]), String.valueOf((int) Math.round(hour.temp_c)));
                            if (hoursNeeded==forecastdata.size()){
                                break outerloop;
                            }
                        }
                    }
                }
            } else {
                System.out.println("GET request failed. Response Code: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Other causes");
            e.printStackTrace();
        }
        return;
    }
    public Map<LocalTime, Double> getExternalTempData() {
        Map<LocalTime, Double> temp = new HashMap<>();
        if (weatherdata != null && weatherdata.current != null) {
            LocalTime now = LocalTime.now().withMinute(0).withSecond(0).withNano(0);
            temp.put(now, weatherdata.current.temp_c);
        }
        return temp;
    }
    public Map<LocalTime, Double> getUVData() {
        Map<LocalTime, Double> UV = new HashMap<>();
        if (weatherdata != null && weatherdata.current != null) {
            LocalTime now = LocalTime.now().withMinute(0).withSecond(0).withNano(0);
            UV.put(now, weatherdata.current.uv);
        }
        return UV;
    }

}


