package com.example.row.implementation;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;  //TODO seems like android doesnt have java libraries after java 8
import java.net.http.HttpRequest;  //TODO i think android has its own web library
import java.util.Arrays;
import java.util.List;




public class WindData implements Wind{
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
    public WindDataPoint getWindData() {
        try {
            //Getting data from API
            String apiKey = "9ae03fe30b1582eb943b576d73e32620";
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=52.2&lon=0.117&appid=" + apiKey + "&units=metric";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            // Extract values
            double windSpeed = extractDouble(body, "\"speed\":");
            double windDirection = extractDouble(body, "\"deg\":");
            return new WindDataPoint(windSpeed, windDirection);
        } catch (IOException| InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    public double getWindDirection(){
        //Data has been retrieved in getWindData function so simple to get
        return getWindData().windDirection;
    }

    public double getWindSpeed(){
        //Same as with getWindDirection
        return getWindData().windSpeed;
    }
    public String getWindImpactMagnitudeColour(int startX, int startY, int endX, int endY){  //TODO could you make it work with double latitude and longitudes? any harm in just swapping int for double?
        //Taking a set of coordinates and using the wind data, here we are calculating the magnitude of the impact of the wind
        double riverAngle = Math.atan((double) (endY - startY) /(endX-startX));//Finding the angle of the stretch of the river modelled by the coordinates
        double windAngle = Math.toRadians(270 - getWindDirection());//Changing from bearing to vector
        double windSpeed = getWindSpeed(); //TODO can we move this to a field or sth, so that only one api call? unless im misunderstanding the method, maybe run api call on constructor?
        //Split wind into x and y components
        double xWind = Math.cos(windAngle) * windSpeed;
        double yWind = Math.sin(windAngle) * windSpeed;
        //Now determine the effect of the wind parallel and perpendicular to the direction of the river
        double parallel = Math.cos(riverAngle)*xWind + Math.sin(riverAngle)*yWind;
        double perpendicular = Math.sin(riverAngle)*xWind - Math.cos(riverAngle)*yWind;
        //So the boat will be slowed down by the parallel effect
        //Boat will be pushed to the side by the perpendicular effect which considers right to be positive and left to be negative
        //Not going to use perpendicular in this case but useful to have if any changes were made
        if (parallel < -7.5) return "Red";
        else if (parallel>7.5) return "Green";
        else return "Yellow";
    }

    public List<Double> getWindImpactMagnitudeVector(int startX, int startY, int endX, int endY){
        double windAngle = Math.toRadians(270 - getWindDirection());//Changing from bearing to vector
        double windSpeed = getWindSpeed();
        return Arrays.asList(windAngle, windSpeed);

    }


}
