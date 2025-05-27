package com.example.row.implementation;

import java.util.List;

public interface Wind {
    void getWindData() throws Exception;
    double getWindDirection(); // 0 - 359
    double getWindSpeed(); // > 0


    /**
     * Impact can be positive or negative but communicate the range with whomever is doing the map so that
     * they can convert that into colours and vectors accordingly. This calculates the impact of the wind on
     * each straight section of the river. The input parameters are in pixels with the bottom left = (0,0).
     */
    String getWindImpactMagnitudeColour(double startX, double startY, double endX, double endY);

    List<Double> getWindImpactMagnitudeVector(int startX, int startY, int endX, int endY);







}
