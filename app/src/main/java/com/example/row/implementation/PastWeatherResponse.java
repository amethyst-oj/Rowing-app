//Past Weather Conditions index (date + time)
package com.example.row.implementation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class PastWeatherResponse {
    public static WeatherResponse fetchPastWeather(String apiKey, String location, String day, String hour)  {
        /*String apiKey = "d8f21a5e71ff4fb99bc131927252005";  // your API key
        String location = "Cambridge"; // change to any city
        String day = "2025-05-10";
        String hour = "13"; */
        String urlString = "http://api.weatherapi.com/v1/history.json?key=" + apiKey + "&q=" + location + "&dt=" + day;
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

               WeatherResponse weather = gson.fromJson(response.toString(), WeatherResponse.class);

                // Get the first forecast day and hour 13
                Hour hour13 = weather.forecast.forecastday.get(0).hour.get(13);

                /*System.out.println("Time: " + hour13.time);
                System.out.println("Temperature (°C): " + hour13.temp_c);
                System.out.println("Humidity: " + hour13.humidity);
                System.out.println("Condition: " + hour13.condition.text);
                System.out.println("Wind Speed (kph): " + hour13.wind_kph);
                System.out.println("Wind Direction: " + hour13.wind_dir);
                System.out.println("Wind Degree: " + hour13.wind_degree);
                System.out.println("Gust (kph): " + hour13.gust_kph);*/
                System.out.println("UV Index: " + hour13.uv);
            } else {
                System.out.println("Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

