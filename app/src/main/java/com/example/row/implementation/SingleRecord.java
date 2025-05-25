package com.example.row.implementation;

import java.time.LocalDateTime;

public class SingleRecord {
    private LocalDateTime time;
    private int distance;
    private int timeTaken;
    private double speed;

    public SingleRecord(LocalDateTime time, int distance, int timeTaken) {
        this.time = time;
        this.distance = distance;
        this.timeTaken = timeTaken;
        this.speed = (double) distance / ((double) timeTaken); // Speed = distance over time
    }

    public LocalDateTime getDateTime() { return time; }
    public int getDistance() { return distance; } // Commonly 500, 1000, 2000
    public int getTimeTaken() { return timeTaken; } // in seconds
    public double getSpeed() { return speed; }

}
