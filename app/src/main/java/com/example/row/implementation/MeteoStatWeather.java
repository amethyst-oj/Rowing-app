//Class gets weather conditions based on co-ordinates doesn't give UV
package com.example.row.implementation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.Scanner;
import com.google.gson.Gson;

public class MeteoStatWeather {
    public static MeteoStatWeather fetchHourly(double lat, double lon, String date, String hourT) {
        /*Scanner scanner = new Scanner(System.in);
        /*System.out.print("Enter latitude: ");
        double lat = scanner.nextDouble();
        double lat = 52.212529;
        System.out.print("Enter longitude: ");
        double lon = scanner.nextDouble();
        double lon = 0.128339;
        scanner.close();
        String date = "2025-05-23";
        String hourT = "18"; */
        String FormatTime = date +" "+ hourT +":00:00";
        System.out.println(FormatTime);


        try {
            String url = "https://meteostat.p.rapidapi.com/point/hourly?lat=" + lat + "&lon=" + lon + "&start="+ date +"&end=" + date + "&tz=Europe/London";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-rapidapi-key", "ad408a7d15mshbb454150c0f91b1p11fc6ajsne65ffcf6bca3");
            conn.setRequestProperty("x-rapidapi-host", "meteostat.p.rapidapi.com");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            /* Debugging Test
            System.out.println("response:");
            System.out.println(response.toString());*/

            Gson gson = new Gson();
            WeatherResponse parsed = gson.fromJson(response.toString(), WeatherResponse.class);

            for (HourlyDataMeteo hour : parsed.data) {
                //System.out.println("x");
                //System.out.println(hour.time);
                if (hour.time != null && hour.time.contains( FormatTime)) {
                    // Test System.out.println("Hi");
                    System.out.println("\n--- Weather at " +hourT+ ":00:00 ---");
                    System.out.println("Time: " + hour.time);
                    System.out.println("Temp: " + hour.temp + " °C");
                    System.out.println("Humidity: " + hour.rhum + " %");
                    System.out.println("Wind speed: " + hour.wspd + " km/h");
                    System.out.println("Wind direction: " + hour.wdir + " °");
                    System.out.println("Pressure: " + hour.pres + " hPa");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

