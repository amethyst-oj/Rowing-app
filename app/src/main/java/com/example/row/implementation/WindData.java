package com.example.row.implementation;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpRequest;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;




public class WindData implements Wind{
    private double windSpeed;
    private double windDirection;

    public WindData(){
        getWindData();
    }
    //extractDouble takes a string and a key you are trying to locate, fins key and then returns value associated with key
    private static double extractDouble(String text, String key) {
        int start = text.indexOf(key) + key.length();
        int end = text.indexOf(",", start);
        if (end == -1) {
            end = text.indexOf("}", start);
        }

        return Double.parseDouble(text.substring(start, end).trim());
    }
    @Override
    public void getWindData() {
        String apiKey = "9ae03fe30b1582eb943b576d73e32620";
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=52.2&lon=0.117&appid=" + apiKey + "&units=metric";

        try {
            //Getting data from API
            URL url_wind= new URL(url);
            HttpURLConnection urlConnection= (HttpURLConnection) url_wind.openConnection();
            try{
                BufferedReader input= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                urlConnection.setRequestMethod("GET");
                int responseCode= urlConnection.getResponseCode();

                StringBuilder inLine= new StringBuilder();
                String body = "";
                if (responseCode==HttpURLConnection.HTTP_OK){
                    String in;
                    while((in=input.readLine()) != null){
                        inLine.append(in);
                    }
                    body = String.valueOf(inLine);
                }
                JSONObject json = new JSONObject(body);
                windSpeed = json.getJSONObject("wind").getDouble("speed");
                windDirection = json.getJSONObject("wind").getDouble("deg");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } finally {
                urlConnection.disconnect();
            }
            // Extract values
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getWindDirection(){
        //Data has been retrieved in getWindData function so simple to get
        return windDirection;
    }

    public double getWindSpeed(){
        //Same as with getWindDirection
        return windSpeed;
    }
    public String getWindImpactMagnitudeColour(double startX, double startY, double endX, double endY){  //using doubles, issue?
        //Taking a set of coordinates and using the wind data, here we are calculating the magnitude of the impact of the wind
        double riverAngle = Math.atan((double) (endY - startY) /(endX-startX));//Finding the angle of the stretch of the river modelled by the coordinates
        double windAngle = Math.toRadians(270 - windDirection);//Changing from bearing to vector
        //Split wind into x and y components
        double xWind = Math.cos(windAngle) * windSpeed;
        double yWind = Math.sin(windAngle) * windSpeed;
        //Now determine the effect of the wind parallel and perpendicular to the direction of the river
        double parallel = Math.cos(riverAngle)*xWind + Math.sin(riverAngle)*yWind;
        double perpendicular = Math.sin(riverAngle)*xWind - Math.cos(riverAngle)*yWind;
        //So the boat will be slowed down by the parallel effect
        //Boat will be pushed to the side by the perpendicular effect which considers right to be positive and left to be negative
        //Not going to use perpendicular in this case but useful to have if any changes were made
        if (parallel < -7.5) return "#FF0000";
        else if (parallel>7.5) return "#00FF00";
        else return "#FFFF00";
    }

    public List<Double> getWindImpactMagnitudeVector(int startX, int startY, int endX, int endY){
        double windAngle = Math.toRadians(270 - getWindDirection());//Changing from bearing to vector
        double windSpeed = getWindSpeed();
        return Arrays.asList(windAngle, windSpeed);

    }


}
